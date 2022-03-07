package io.github.rain.persecution.utils

import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.http.HttpProtocol
import com.qcloud.cos.region.Region
import io.ktor.http.content.*
import io.ktor.util.pipeline.*


/**
 * io.github.rain.persecution.utils.COSUtils
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/6 17:05
 **/
private const val SECRET_ID = "AKIDGMCm69vQfdKkunPjmZ5JGwNXP1MPBftq"
private const val SECRET_KEY = "h7G7h03pMcbKq52j8G7i24pJWR5SZPyG"
private const val COS_REGION = "ap-chongqing"

private fun initClient(): COSClient {
    val cred = BasicCOSCredentials(SECRET_ID, SECRET_KEY)
    val region = Region(COS_REGION)
    val config = ClientConfig(region)
    config.httpProtocol = HttpProtocol.https
    return COSClient(cred, config)
}

val cosClient by lazy { initClient() }
const val BUCKET = "persecution-1301196908"
