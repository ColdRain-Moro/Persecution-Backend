package io.github.rain.persecution.utils

import io.ktor.http.content.*

/**
 * io.github.rain.persecution.utils.Utils
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/6 16:58
 **/
suspend fun MultiPartData.value(name: String) =
    try { (readAllParts().filter { it.name == name }[0] as? PartData.FormItem)?.value } catch(e: Exception) { null }

suspend fun MultiPartData.file(name: String) =
    try { readAllParts().filter { it.name == name }[0] as? PartData.FileItem } catch(e: Exception) { null }