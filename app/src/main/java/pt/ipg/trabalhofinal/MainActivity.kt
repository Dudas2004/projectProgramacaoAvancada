package pt.ipg.trabalhofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val bandeiras = mapOf(
        "EUR" to R.drawable.eur_flag,
        "BRL" to R.drawable.br_flag,
        "USD" to R.drawable.us_flag,
        "AED" to R.drawable.ae_flag,
        "CNY" to R.drawable.cn_flag
    )

    var valor by remember { mutableStateOf("") }
    var moedaOrigem by remember { mutableStateOf("EUR") }
    var moedaDestino by remember { mutableStateOf("USD") }
    var resultado by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Conversor de Moedas — Duarte Martins") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = {
                    if (it.matches(Regex("^\\d*[\\.,]?\\d*\$"))) {
                        valor = it.replace(",", ".")
                    }
                },
                label = { Text("Valor a converter") },
                modifier = Modifier.fillMaxWidth()
            )


            DropdownMenuMoeda(
                label = "Moeda de Origem",
                opcoes = moedas,
                selecionada = moedaOrigem,
                bandeiras = bandeiras,
                onSelecionar = { moedaOrigem = it }
            )

            DropdownMenuMoeda(
                label = "Moeda de Destino",
                opcoes = moedas,
                selecionada = moedaDestino,
                bandeiras = bandeiras,
                onSelecionar = { moedaDestino = it }
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

@Composable
fun DropdownMenuMoeda(
    label: String,
    opcoes: List<String>,
    selecionada: String,
    bandeiras: Map<String, Int>,
    modifier: Modifier = Modifier,
    onSelecionar: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label)
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = bandeiras[selecionada] ?: R.drawable.ic_launcher_foreground),
                        contentDescription = "Bandeira",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(selecionada)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opcoes.forEach { moeda ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = bandeiras[moeda] ?: R.drawable.ic_launcher_foreground),
                                    contentDescription = "Bandeira $moeda",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(moeda)
                            }
                        },
                        onClick = {
                            onSelecionar(moeda)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversorMoedasPreview() {
    TrabalhoFinalTheme {
        ConversorMoedasApp()
    }
}
