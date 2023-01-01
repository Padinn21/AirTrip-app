package com.binarfp.airtrip.data.local.preference

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreDataSource @Inject constructor(private val dataStoreManager: DataStoreManager){
    suspend fun setAccessToken(token:String):Boolean{
        return dataStoreManager.setAccessToken(token)
    }
    fun getAccessToken():Flow<String>{
        return dataStoreManager.accessToken
    }
    suspend fun setImageString(sImage:String):Boolean{
        return dataStoreManager.setImageString(sImage)
    }
    fun getImageString():Flow<String>{
        return dataStoreManager.imageString
    }
}