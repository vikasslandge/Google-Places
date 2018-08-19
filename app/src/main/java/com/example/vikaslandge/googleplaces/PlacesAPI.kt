package com.example.vikaslandge.googleplaces

import com.example.vikaslandge.googleplaces.beans.PlacesAPIBeans
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesAPI {
    @GET("maps/api/place/nearbysearch/json?radius=1000&key=AIzaSyDdCGdR2cnWw0AB0LeN3jOTjKmkKag4tew")
    fun getPlacesAPIBeans(@Query("location") loc:String,
                  @Query("type") type:String):Call<PlacesAPIBeans>
}