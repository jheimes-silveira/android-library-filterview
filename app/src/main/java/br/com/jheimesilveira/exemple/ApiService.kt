package br.com.jheimesilveira.exemple

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

import java.util.ArrayList

//https://github.com/wgenial/br-cidades-estados-nodejs
interface ApiService {

    @GET("/estados")
    fun estados(): Call<ArrayList<Estado>>

    @GET("/estados/{id}/cidades")
    fun cidades(@Path("id") id: String): Call<ArrayList<Cidade>>
}