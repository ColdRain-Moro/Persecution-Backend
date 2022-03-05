package io.github.rain.persecution.data.bean

import kotlinx.serialization.Serializable

/**
 * io.github.rain.persecution.data.bean.ClassificationData
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/6 0:38
 **/
@Serializable
data class ClassificationData(
    val id: Int,
    val name: String,
    val avatar: String,
    val description: String
)