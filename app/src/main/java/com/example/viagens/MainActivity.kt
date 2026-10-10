package com.example.viagens

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.viagens.ui.theme.ViagensTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "configuracoes_viagens")
private val NOME_MOTORISTA = stringPreferencesKey("nome_motorista")

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
                ControleViagens()
            }
        }
    }
}

@Composable
fun ControleViagens() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val nomeMotoristaSalvo by context.dataStore.data
        .map { preferences -> preferences[NOME_MOTORISTA] ?: "" }
        .collectAsState(initial = "")

    var nomeMotoristaInput by remember { mutableStateOf("") }

    LaunchedEffect(nomeMotoristaSalvo) {
        nomeMotoristaInput = nomeMotoristaSalvo
    }

    var data by remember { mutableStateOf("") }
    var kmInicial by remember { mutableStateOf("") }
    var kmFinal by remember { mutableStateOf("") }
    var litros by remember { mutableStateOf("") }
    var combustivel by remember { mutableStateOf("") }
    var pedagio by remember { mutableStateOf("") }
    var valorCombustivel by remember { mutableStateOf("") }

    val viagens = remember {
        mutableStateListOf<Viagem>()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Controle de Viagens",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Motorista",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )

            if (nomeMotoristaSalvo.isNotBlank()) {
                Text(
                    text = "Motorista atual: $nomeMotoristaSalvo",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            TextField(
                value = nomeMotoristaInput,
                onValueChange = { nomeMotoristaInput = it },
                label = { Text("Nome do motorista") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    scope.launch {
                        context.dataStore.edit { preferences ->
                            preferences[NOME_MOTORISTA] = nomeMotoristaInput
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Salvar nome do motorista")
            }

            HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

            Text(
                text = "Cadastre os dados da viagem",
                modifier = Modifier.padding(
                    bottom = 16.dp
                )
            )

            FormularioViagem(
                data = data,
                kmInicial = kmInicial,
                kmFinal = kmFinal,
                litros = litros,
                combustivel = combustivel,
                pedagio = pedagio,
                valorCombustivel = valorCombustivel,

                onDataChange = { data = it },
                onKmInicialChange = { kmInicial = it },
                onKmFinalChange = { kmFinal = it },
                onLitrosChange = { litros = it },
                onCombustivelChange = { combustivel = it },
                onPedagioChange = { pedagio = it },
                onValorCombustivelChange = {
                    valorCombustivel = it
                },

                onCadastrar = {

                    val kmInicialNumero = kmInicial.toDoubleOrNull()
                    val kmFinalNumero = kmFinal.toDoubleOrNull()
                    val litrosNumero = litros.toDoubleOrNull()
                    val pedagioNumero = pedagio.toDoubleOrNull()
                    val valorCombustivelNumero =
                        valorCombustivel.toDoubleOrNull()

                    if (
                        data.isNotBlank() &&
                        combustivel.isNotBlank() &&
                        kmInicialNumero != null &&
                        kmFinalNumero != null &&
                        litrosNumero != null &&
                        pedagioNumero != null &&
                        valorCombustivelNumero != null &&
                        kmFinalNumero >= kmInicialNumero &&
                        litrosNumero > 0 &&
                        pedagioNumero >= 0 &&
                        valorCombustivelNumero >= 0
                    ) {
                        val viagem = Viagem(
                            data = data,
                            kmInicial = kmInicialNumero,
                            kmFinal = kmFinalNumero,
                            litros = litrosNumero,
                            combustivel = combustivel,
                            pedagio = pedagioNumero,
                            valorCombustivel = valorCombustivelNumero
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
                }
            )

            ListaViagens(viagens)

            var totalKm = 0.0
            var totalGasto = 0.0
            var totalLitros = 0.0

            for (viagem in viagens) {
                totalKm += viagem.calcularDistancia()
                totalGasto += viagem.calcularCusto()
                totalLitros += viagem.litros
            }

            val mediaKmLitro = if (totalLitros > 0) {
                totalKm / totalLitros
            } else {
                0.0
            }

            ResumoViagens(
                totalKm = totalKm,
                totalGasto = totalGasto,
                mediaKmLitro = mediaKmLitro
            )
        }
    }
}

@Composable
fun FormularioViagem(
    data: String,
    kmInicial: String,
    kmFinal: String,
    litros: String,
    combustivel: String,
    pedagio: String,
    valorCombustivel: String,
    onDataChange: (String) -> Unit,
    onKmInicialChange: (String) -> Unit,
    onKmFinalChange: (String) -> Unit,
    onLitrosChange: (String) -> Unit,
    onCombustivelChange: (String) -> Unit,
    onPedagioChange: (String) -> Unit,
    onValorCombustivelChange: (String) -> Unit,
    onCadastrar: () -> Unit
) {

    TextField(
        value = data,
        onValueChange = onDataChange,
        label = { Text("Data") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = kmInicial,
        onValueChange = onKmInicialChange,
        label = { Text("Km inicial") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = kmFinal,
        onValueChange = onKmFinalChange,
        label = { Text("Km final") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = litros,
        onValueChange = onLitrosChange,
        label = { Text("Litros") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = combustivel,
        onValueChange = onCombustivelChange,
        label = { Text("Combustível") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = pedagio,
        onValueChange = onPedagioChange,
        label = { Text("Pedágio") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

    TextField(
        value = valorCombustivel,
        onValueChange = onValorCombustivelChange,
        label = { Text("Valor do combustível") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    )

    Button(
        onClick = onCadastrar,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Cadastrar viagem")
    }
}

@Composable
fun ListaViagens(
    viagens: List<Viagem>
) {

    Text(
        text = "Viagens cadastradas",
        modifier = Modifier.padding(
            top = 20.dp,
            bottom = 8.dp
        )
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
}

@Composable
fun ResumoViagens(
    totalKm: Double,
    totalGasto: Double,
    mediaKmLitro: Double
) {

    Text(
        text = "Resumo",
        modifier = Modifier.padding(
            top = 8.dp,
            bottom = 8.dp
        )
    )

    Text("Total gasto: R$ $totalGasto")

    Text("Total de km: $totalKm km")

    Text("Média: $mediaKmLitro km/l")
}

@Preview(showBackground = true)
@Composable
fun PreviewControleViagens() {
    ViagensTheme {
        ControleViagens()
    }
}