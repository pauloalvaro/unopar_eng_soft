package com.manus.lara.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manus.lara.R
import com.manus.lara.data.local.TaskEntity
import com.manus.lara.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    tasks: List<TaskEntity>,
    onToggleTask: (TaskEntity) -> Unit,
    onUnforeseen: (String) -> Unit,
    onNavigateToTada: () -> Unit
) {
    var showUnforeseenDialog by remember { mutableStateOf(false) }
    var eventText by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showUnforeseenDialog = true },
                containerColor = UrgencySoft,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Imprevisto")
            }
        },
        bottomBar = {
            Button(
                onClick = onNavigateToTada,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPastel)
            ) {
                Text(stringResource(R.string.tada_list_title), color = TextDark)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tudo limpo por hoje! Aproveite seu descanso.", color = TextDark)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(tasks.take(5)) { task ->
                        TaskCard(task, onToggleTask)
                    }
                }
            }
        }

        if (showUnforeseenDialog) {
            AlertDialog(
                onDismissRequest = { showUnforeseenDialog = false },
                title = { Text(stringResource(R.string.imprevisto_button)) },
                text = {
                    OutlinedTextField(
                        value = eventText,
                        onValueChange = { eventText = it },
                        placeholder = { Text(stringResource(R.string.imprevisto_hint)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        onUnforeseen(eventText)
                        showUnforeseenDialog = false
                        eventText = ""
                    }) {
                        Text(stringResource(R.string.imprevisto_submit), color = SuccessMint)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUnforeseenDialog = false }) {
                        Text("Cancelar", color = TextDark)
                    }
                }
            )
        }
    }
}

@Composable
fun TaskCard(task: TaskEntity, onToggleTask: (TaskEntity) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isCompleted) Color.Gray else TextDark
                )
                if (task.description != null) {
                    Text(
                        text = task.description,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            
            IconButton(onClick = { onToggleTask(task) }) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Concluir",
                    tint = if (task.isCompleted) SuccessMint else Color.LightGray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
