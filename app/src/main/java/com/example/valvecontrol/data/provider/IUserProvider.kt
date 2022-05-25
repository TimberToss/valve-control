package com.example.valvecontrol.data.provider

import com.example.valvecontrol.base.provider.IBaseProvider
import kotlinx.coroutines.flow.Flow

interface IUserProvider : IBaseProvider {

    fun getUserFirebaseToken(): Flow<String?>

    suspend fun setUserFirebaseToken(token: String)

}