package com.example.vikaslandge.googleplaces.beans

data class PlacesAPIBeans(
        val html_attributions: List<Any>,
        val next_page_token: String,
        val results: List<Result>,
        val status: String
)