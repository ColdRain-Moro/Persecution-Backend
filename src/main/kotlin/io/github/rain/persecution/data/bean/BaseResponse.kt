package io.github.rain.persecution.data.bean

import kotlinx.serialization.Serializable

/**
 * io.github.rain.persecution.data.bean.BaseResponse
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 14:38
 **/
@Serializable
data class BaseResponse<T>(
    // 正常时为0
    val errorCode: Int,
    val message: String,
    val data: T
)