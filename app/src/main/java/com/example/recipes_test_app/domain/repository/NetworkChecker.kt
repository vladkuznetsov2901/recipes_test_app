package com.example.recipes_test_app.domain.repository

interface NetworkChecker {

    fun isNetworkAvailable(): Boolean

}