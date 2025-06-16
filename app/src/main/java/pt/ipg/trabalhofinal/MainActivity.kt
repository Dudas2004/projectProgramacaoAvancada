package pt.ipg.trabalhofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipg.trabalhofinal.ui.theme.TrabalhoFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrabalhoFinalTheme {
                ConversorMoedasApp()
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversorMoedasApp() {
    val moedas = listOf("EUR", "BRL", "USD", "AED", "CNY")
    val taxas = mapOf(
        "EUR" to 1.0,
        "BRL" to 5.5,
        "USD" to 1.1,
        "AED" to 4.05,
        "CNY" to 7.8
    )

    var valor by remember { mutableStateOf("") }
    var moedaOrigem by remember { mutableStateOf("EUR") }
    var moedaDestino by remember { mutableStateOf("USD") }
    var resultado by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("💱 Conversor de Moedas — Duarte Martins") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = { valor = it },
                label = { Text("Valor a converter") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val valorDouble = valor.toDoubleOrNull()
                    if (valorDouble != null) {
                        val emEuros = valorDouble / (taxas[moedaOrigem] ?: 1.0)
                        val convertido = emEuros * (taxas[moedaDestino] ?: 1.0)
                        resultado = "%.2f $moedaOrigem = %.2f $moedaDestino".format(valorDouble, convertido)
                    } else {
                        resultado = "Valor inválido."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Converter", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = resultado,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrabalhoFinalTheme {
        ConversorMoedasApp()
    }
}