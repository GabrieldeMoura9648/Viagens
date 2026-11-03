package com.example.viagens

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ViagemDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATA TEXT,
                $COLUMN_KM_INICIAL REAL,
                $COLUMN_KM_FINAL REAL,
                $COLUMN_LITROS REAL,
                $COLUMN_COMBUSTIVEL TEXT,
                $COLUMN_PEDAGIO REAL,
                $COLUMN_VALOR_COMBUSTIVEL REAL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun inserirViagem(viagem: Viagem): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATA, viagem.data)
            put(COLUMN_KM_INICIAL, viagem.kmInicial)
            put(COLUMN_KM_FINAL, viagem.kmFinal)
            put(COLUMN_LITROS, viagem.litros)
            put(COLUMN_COMBUSTIVEL, viagem.combustivel)
            put(COLUMN_PEDAGIO, viagem.pedagio)
            put(COLUMN_VALOR_COMBUSTIVEL, viagem.valorCombustivel)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun listarViagens(): List<Viagem> {
        val listaViagens = mutableListOf<Viagem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val data = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA))
                val kmInicial = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KM_INICIAL))
                val kmFinal = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_KM_FINAL))
                val litros = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LITROS))
                val combustivel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMBUSTIVEL))
                val pedagio = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PEDAGIO))
                val valorCombustivel = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_VALOR_COMBUSTIVEL))

                val viagem = Viagem(
                    data = data,
                    kmInicial = kmInicial,
                    kmFinal = kmFinal,
                    litros = litros,
                    combustivel = combustivel,
                    pedagio = pedagio,
                    valorCombustivel = valorCombustivel
                )
                listaViagens.add(viagem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaViagens
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Viagens.db"
        const val TABLE_NAME = "viagem"
        const val COLUMN_ID = "id"
        const val COLUMN_DATA = "data"
        const val COLUMN_KM_INICIAL = "kmInicial"
        const val COLUMN_KM_FINAL = "kmFinal"
        const val COLUMN_LITROS = "litros"
        const val COLUMN_COMBUSTIVEL = "combustivel"
        const val COLUMN_PEDAGIO = "pedagio"
        const val COLUMN_VALOR_COMBUSTIVEL = "valorCombustivel"
    }
}
