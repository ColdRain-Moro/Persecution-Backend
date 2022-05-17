package io.github.rain.persecution.utils

import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.http.HttpProtocol
import com.qcloud.cos.region.Region


/**
 * io.github.rain.persecution.utils.COSUtils
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/6 17:05
 **/
private val SECRET_ID = secret().secretId
private val SECRET_KEY = secret().secretKey
private val COS_REGION = secret().cosRegion

private fun initClient(): COSClient {
    val cred = BasicCOSCredentials(SECRET_ID, SECRET_KEY)
    val region = Region(COS_REGION)
    val config = ClientConfig(region)
    config.httpProtocol = HttpProtocol.https
    return COSClient(cred, config)
}

val cosClient by lazy { initClient() }
val BUCKET = secret().cosRegion
