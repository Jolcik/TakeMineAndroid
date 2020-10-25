package com.pkostrzenski.takemine.api

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


data class ErrorResponse(
    val status: Int,
    val errorCode: Int?,
    val message: String
){
    companion object{

        private fun empty() = ErrorResponse(-1, null, "")

        fun <T: Any> parseError(response: Response<T>): ErrorResponse {
            val converter: Converter<ResponseBody, ErrorResponse> = ApiFactory.retrofit()
                .responseBodyConverter(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))

            return response.errorBody()?.let {
                try {
                    converter.convert(it)
                } catch (e: IOException) { empty() }
            } ?: empty()

        }
    }
}