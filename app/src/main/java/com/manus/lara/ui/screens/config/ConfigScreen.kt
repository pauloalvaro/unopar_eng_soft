package com.manus.lara.ui.screens.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(onSave: (String) -> Unit) {
    var apiKey by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.config_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Para começar, precisamos conectar a inteligência da Lara. Insira sua chave da Gemini API abaixo.",
            fontSize = 16.sp,
            color = TextDark,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text(stringResource(R.string.api_key_hint)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = SuccessMint,
                cursorColor = SuccessMint
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { if (apiKey.isNotBlank()) onSave(apiKey) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessMint)
        ) {
            Text(
                text = stringResource(R.string.save_button),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
