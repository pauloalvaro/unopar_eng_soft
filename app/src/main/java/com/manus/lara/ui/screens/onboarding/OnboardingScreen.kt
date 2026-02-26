package com.manus.lara.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.manus.lara.ui.theme.BackgroundColor
import com.manus.lara.ui.theme.SuccessMint
import com.manus.lara.ui.theme.TextDark

@Composable
fun OnboardingScreen(onComplete: (rooms: String, hasKids: Boolean, hasPets: Boolean, appliances: String) -> Unit) {
    var rooms by remember { mutableStateOf("") }
    var hasKids by remember { mutableStateOf(false) }
    var hasPets by remember { mutableStateOf(false) }
    var appliances by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.onboarding_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.onboarding_subtitle),
            fontSize = 18.sp,
            color = TextDark,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Rooms Input
        OutlinedTextField(
            value = rooms,
            onValueChange = { rooms = it },
            label = { Text("Quais cômodos tem na sua casa?") },
            placeholder = { Text("Ex: Sala, Cozinha, 2 Quartos...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Kids and Pets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tem crianças em casa?", color = TextDark)
            Switch(
                checked = hasKids,
                onCheckedChange = { hasKids = it },
                colors = SwitchDefaults.colors(checkedThumbColor = SuccessMint)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tem pets em casa?", color = TextDark)
            Switch(
                checked = hasPets,
                onCheckedChange = { hasPets = it },
                colors = SwitchDefaults.colors(checkedThumbColor = SuccessMint)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Appliances
        OutlinedTextField(
            value = appliances,
            onValueChange = { appliances = it },
            label = { Text("Quais eletrodomésticos facilitam sua vida?") },
            placeholder = { Text("Ex: Lava-louças, Robô Aspirador, AirFryer...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { if (rooms.isNotBlank()) onComplete(rooms, hasKids, hasPets, appliances) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessMint)
        ) {
            Text(
                text = "Criar minha rotina com a Lara",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
