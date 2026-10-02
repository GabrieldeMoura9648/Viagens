package com.example.viagens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viagens.ui.theme.ViagensTheme


class Viagem(
    val data: String,
    val kmInicial: Double,
    val kmFinal: Double,
    val litros: Double,
    val combustivel: String,
    val pedagio: Double,
    val valorCombustivel: Double
) {

    fun calcularDistancia(): Double {
        return kmFinal - kmInicial
    }

    fun calcularCusto(): Double {
        return (litros * valorCombustivel) + pedagio
    }
}


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            ViagensTheme {

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    var data by remember {
                        mutableStateOf("")
                    }

                    var kmInicial by remember {
                        mutableStateOf("")
                    }

                    var kmFinal by remember {
                        mutableStateOf("")
                    }

                    var litros by remember {
                        mutableStateOf("")
                    }

                    var combustivel by remember {
                        mutableStateOf("")
                    }

                    var pedagio by remember {
                        mutableStateOf("")
                    }

                    var valorCombustivel by remember {
                        mutableStateOf("")
                    }

                    val viagens = remember {
                        mutableStateListOf<Viagem>()
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {

                        Text(
                            text = "Controle de Viagens"
                        )

                        Text(
                            text = "Cadastre os dados da viagem",
                            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                        )

                        TextField(
                            value = data,
                            onValueChange = {
                                data = it
                            },
                            label = {
                                Text("Data")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = kmInicial,
                            onValueChange = {
                                kmInicial = it
                            },
                            label = {
                                Text("Km inicial")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = kmFinal,
                            onValueChange = {
                                kmFinal = it
                            },
                            label = {
                                Text("Km final")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = litros,
                            onValueChange = {
                                litros = it
                            },
                            label = {
                                Text("Litros")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = combustivel,
                            onValueChange = {
                                combustivel = it
                            },
                            label = {
                                Text("Combustível")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = pedagio,
                            onValueChange = {
                                pedagio = it
                            },
                            label = {
                                Text("Pedágio")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )

                        TextField(
                            value = valorCombustivel,
                            onValueChange = {
                                valorCombustivel = it
                            },
                            label = {
                                Text("Valor do combustível")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )

                        Button(
                            onClick = {

                                if (
                                    data != "" &&
                                    kmInicial != "" &&
                                    kmFinal != "" &&
                                    litros != "" &&
                                    combustivel != "" &&
                                    pedagio != "" &&
                                    valorCombustivel != ""
                                ) {

                                    val viagem = Viagem(
                                        data = data,
                                        kmInicial = kmInicial.toDouble(),
                                        kmFinal = kmFinal.toDouble(),
                                        litros = litros.toDouble(),
                                        combustivel = combustivel,
                                        pedagio = pedagio.toDouble(),
                                        valorCombustivel = valorCombustivel.toDouble()
                                    )

                                    viagens.add(viagem)

                                    data = ""
                                    kmInicial = ""
                                    kmFinal = ""
                                    litros = ""
                                    combustivel = ""
                                    pedagio = ""
                                    valorCombustivel = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cadastrar viagem")
                        }

                        Text(
                            text = "Viagens cadastradas",
                            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                        )

                        for (viagem in viagens) {

                            Text(
                                text =
                                    "Data: ${viagem.data}\n" +
                                            "Combustível: ${viagem.combustivel}\n" +
                                            "Distância: ${viagem.calcularDistancia()} km\n" +
                                            "Custo: R$ ${viagem.calcularCusto()}",
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        var totalKm = 0.0
                        var totalGasto = 0.0
                        var totalLitros = 0.0

                        for (viagem in viagens) {

                            totalKm += viagem.calcularDistancia()
                            totalGasto += viagem.calcularCusto()
                            totalLitros += viagem.litros
                        }

                        var mediaKmLitro = 0.0

                        if (totalLitros > 0) {
                            mediaKmLitro = totalKm / totalLitros
                        }

                        Text(
                            text = "Resumo",
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )

                        Text(
                            text = "Total gasto: R$ $totalGasto"
                        )

                        Text(
                            text = "Total de km: $totalKm km"
                        )

                        Text(
                            text = "Média: $mediaKmLitro km/l"
                        )
                    }
                }
            }
        }
    }
}