package com.pkostrzenski.takemine.repository

import android.util.Log
import com.pkostrzenski.takemine.api.ErrorResponse
import com.pkostrzenski.takemine.api.Result
import retrofit2.Response

open class BaseRepository {

    suspend fun <T: Any> safeApiCall(call: suspend () -> Response<T>): T?{
        try {
            call.invoke().apply {
                return if(isSuccessful) body() else null
            }
        } catch (e: Exception){ // timeout
            Log.d("API", e.toString())
            return null
        }
    }

    suspend fun <T: Any> safeApiCallWithError(call: suspend () -> Response<T>): Result<T>? {
        return try {
            val response = call.invoke()
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else
                Result.Error(ErrorResponse.parseError(response))

        } catch (e: Exception){
            Log.d("API", e.toString())
            null
        }
    }
}