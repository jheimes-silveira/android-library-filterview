package br.com.jheimesilveira.exemple

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WebService {

    private var retrofit = Retrofit.Builder()
        .baseUrl("https://br-cidade-estado-nodejs.glitch.me")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun estados(sucesso: (estados: ArrayList<Estado>) -> Unit, erro: (t: Throwable) -> Unit) {
        val service = retrofit.create(ApiService::class.java)
        val repos = service.estados()
        return repos.enqueue(object : Callback<ArrayList<Estado>> {
            override fun onResponse(call: Call<ArrayList<Estado>>, response: Response<ArrayList<Estado>>) {
                sucesso(response.body() as ArrayList<Estado>)
            }

            override fun onFailure(call: Call<ArrayList<Estado>>, t: Throwable) {
                erro(t)
            }
        })
    }

    fun cidades(estadoId: String, sucesso: (cidades: ArrayList<Cidade>) -> Unit, erro: (t: Throwable) -> Unit) {
        val service = retrofit.create(ApiService::class.java)
        val repos = service.cidades(estadoId)
        return repos.enqueue(object : Callback<ArrayList<Cidade>> {
            override fun onResponse(call: Call<ArrayList<Cidade>>, response: Response<ArrayList<Cidade>>) {
                sucesso(response.body() as ArrayList<Cidade>)
            }

            override fun onFailure(call: Call<ArrayList<Cidade>>, t: Throwable) {
                erro(t)
            }
        })
    }
}