package com.manus.lara.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.manus.lara.data.local.LaraDao
import com.manus.lara.data.local.TaskEntity
import com.manus.lara.data.local.HouseConfigEntity
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class LaraRepository(private val laraDao: LaraDao, private val apiKey: String) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    private val systemPrompt = """
        Você é a 'Lara', uma assistente de gestão doméstica empática e prática. Seu tom é acolhedor, nunca crítico. 
        Ao organizar tarefas, considere que eletrodomésticos como robôs aspiradores e lava-louças reduzem o tempo humano. 
        Se houver imprevistos, priorize o descanso da usuária em vez da produtividade tóxica. 
        Responda sempre em JSON estruturado para o app processar.
        
        Formato de saída esperado para rotina:
        [
          {"title": "Nome da Tarefa", "description": "Dica empática", "category": "Diária", "effortLevel": 1},
          ...
        ]
        
        Formato de saída para imprevisto:
        [
          {"title": "Nome da Tarefa", "description": "Dica empática", "category": "Reorganizada", "effortLevel": 1},
          ...
        ]
    """.trimIndent()

    suspend fun generateInitialRoutine(rooms: String, hasKids: Boolean, hasPets: Boolean, appliances: String): List<TaskEntity> {
        val prompt = "Crie uma rotina doméstica para uma casa com: $rooms. Crianças: $hasKids, Pets: $hasPets. Eletrodomésticos: $appliances. Retorne apenas o JSON com 5 tarefas prioritárias para hoje."
        
        val response = generativeModel.generateContent(
            content {
                text(systemPrompt)
                text(prompt)
            }
        )

        val jsonString = response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val tasks = mutableListOf<TaskEntity>()
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            tasks.add(
                TaskEntity(
                    title = obj.getString("title"),
                    description = obj.optString("description"),
                    category = obj.getString("category"),
                    effortLevel = obj.getInt("effortLevel"),
                    date = today
                )
            )
        }
        
        // Save config
        laraDao.saveHouseConfig(HouseConfigEntity(rooms = rooms, hasKids = hasKids, hasPets = hasPets, appliances = appliances))
        // Save tasks
        laraDao.insertTasks(tasks)
        
        return tasks
    }

    suspend fun handleUnforeseen(event: String): List<TaskEntity> {
        val config = laraDao.getHouseConfig()
        val prompt = "A usuária relatou um imprevisto: '$event'. Com base na configuração da casa ($config), reorganize as tarefas de hoje, priorizando o essencial e o descanso. Retorne o novo JSON de 5 tarefas para hoje."
        
        val response = generativeModel.generateContent(
            content {
                text(systemPrompt)
                text(prompt)
            }
        )

        val jsonString = response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: "[]"
        val jsonArray = JSONArray(jsonString)
        val tasks = mutableListOf<TaskEntity>()
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        // Delete current unfinished tasks for today
        laraDao.deleteUnfinishedTasks(today)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            tasks.add(
                TaskEntity(
                    title = obj.getString("title"),
                    description = obj.optString("description"),
                    category = obj.getString("category"),
                    effortLevel = obj.getInt("effortLevel"),
                    date = today
                )
            )
        }
        
        laraDao.insertTasks(tasks)
        return tasks
    }

    fun getTasksForToday(date: Long): Flow<List<TaskEntity>> = laraDao.getTasksByDate(date)
    
    fun getCompletedTasks(): Flow<List<TaskEntity>> = laraDao.getCompletedTasks()

    suspend fun updateTask(task: TaskEntity) = laraDao.updateTask(task)
}
