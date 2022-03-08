package io.github.rain.persecution.data.bean

import kotlinx.serialization.Serializable

/**
 * io.github.rain.persecution.data.bean.SingleImageData
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 23:50
 **/
@Serializable
data class SingleImageData(
    // 图片id
    val id: Int,
    // 分类id
    val cid: Int,
    // 图片url
    val image: String
)