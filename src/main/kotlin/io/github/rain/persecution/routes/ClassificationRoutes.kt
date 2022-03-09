package io.github.rain.persecution.routes

import com.qcloud.cos.model.PutObjectRequest
import io.github.rain.persecution.data.bean.BaseResponse
import io.github.rain.persecution.data.bean.ClassificationData
import io.github.rain.persecution.data.bean.Pager
import io.github.rain.persecution.data.bean.SingleImageData
import io.github.rain.persecution.data.db.DBHandler
import io.github.rain.persecution.data.db.TableClassificationContent
import io.github.rain.persecution.data.db.TableClassificationInfo
import io.github.rain.persecution.utils.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ktorm.dsl.*
import org.ktorm.support.mysql.rand
import java.io.File
import java.util.*
import kotlin.random.Random

/**
 * io.github.rain.persecution.routes.ClassificationRoutes
 * persecution
 *
 * @author 寒雨
 * @since 2022/3/5 14:05
 **/
fun Routing.setupClassificationRoutes() {
    // 上传图片
    // 将图片上传到一个储存桶中，再储存url
    @Suppress("BlockingMethodInNonBlockingContext")
    post("/upload") {
        val params = call.receiveMultipart()
        val image = params.file("image") ?: return@post let {
            call.respond(
                BaseResponse(
                    ErrorCode.WRONG_PARAMS,
                    "参数错误",
                    ""
                )
            )
        }
        val key = UUID.randomUUID().toString()
        val file = File("temp${File.pathSeparator}${key + "-" + image.originalFileName}")
        file.mkdirs()
        file.createNewFile()
        // 写入文件
        image.streamProvider().use { its ->
            file.outputStream().buffered().use {
                its.copyTo(it)
            }
        }
        // 关闭
        image.dispose()
        // 上传到对象储存桶
        val request = PutObjectRequest(BUCKET, key, file)
        cosClient.putObject(request)
        // 获取上传图片的url
        val url = cosClient.getObjectUrl(BUCKET, key).toString()
        // 删除临时文件
        file.delete()
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                url
            )
        )
    }

    // 通过id或name获取分类
    get("/classification") {
        var id = call.request.queryParameters["id"]?.toIntOrNull()
        if (id == null) {
            val name = call.request.queryParameters["name"] ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.MISSING_PARAMS,
                        "参数缺失",
                        ""
                    )
                )
            }
            id = DBHandler.database
                .from(TableClassificationInfo)
                .select(TableClassificationInfo.id)
                .where { TableClassificationInfo.name eq name }
                .limit(1)
                .map { it[TableClassificationInfo.id] }
                .firstOrNull() ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.WRONG_PARAMS,
                        "不存在与该名称匹配的参数",
                        ""
                    )
                )
            }
        }
        val result = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .where { TableClassificationInfo.id eq id }
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
            .firstOrNull() ?: return@get let {
                call.respond(
                    BaseResponse(
                        ErrorCode.WRONG_PARAMS,
                        "参数错误",
                        ""
                    )
                )
        }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                result
            )
        )
    }

    post("/classification/upload") {
        val params = call.receiveParameters()
        val cid = params["cid"]?.toIntOrNull() ?: return@post let {
            call.respond(
                BaseResponse(
                    ErrorCode.WRONG_PARAMS,
                    "参数错误",
                    ""
                )
            )
        }
        val image = params["image"] ?: return@post let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数缺失",
                    ""
                )
            )
        }
        val find = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .where { TableClassificationInfo.id eq cid }
            .limit(1)
            .iterator()
            .hasNext()
        if (!find) {
            call.respond(
                BaseResponse(
                    ErrorCode.WRONG_PARAMS,
                    "分类不存在",
                    ""
                )
            )
            return@post
        }
        DBHandler.database.useTransaction {
            DBHandler.database
                .insert(TableClassificationContent) {
                    set(it.cid, cid)
                    set(it.image, image)
                }
        }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                ""
            )
        )
    }

    // 获取分类图片
    // 做下分页处理
    get("/classification/images") {
        val id = call.request.queryParameters["id"]?.toIntOrNull()
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 20
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val results = DBHandler.database
            .from(TableClassificationContent)
            .select()
            .apply { if (id != null) where { TableClassificationContent.cid eq id } }
            .limit(limit, offset)
            .map {
                SingleImageData(
                    it[TableClassificationContent.id]!!,
                    it[TableClassificationContent.cid]!!,
                    it[TableClassificationContent.image]!!
                )
            }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                Pager(
                    size = results.size,
                    offset = offset,
                    data = results
                )
            )
        )
    }

    // 创建分类
    post("/classification/create") {
        val params = call.receiveParameters()
        if (!assertArgsNonNull(params, "name")) return@post
        // 使用事务
        DBHandler.database.useTransaction {
            DBHandler.database
                .insert(TableClassificationInfo) {
                    set(it.name, params["name"])
                    set(it.avatar, params["avatar"] ?: "https://tse2-mm.cn.bing.net/th/id/OIP-C.CN79D_9Jx71T6Ugg0Tpx2AHaHa?pid=ImgDet&rs=1")
                    set(it.description, params["description"] ?: "没有介绍喵~")
                }
        }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                ""
            )
        )
    }

    // 删除分类
    post("/classification/remove") {
        val params = call.receiveParameters()
        val id = params["id"]?.toIntOrNull() ?: return@post let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数错误或不完整",
                    null
                )
            )
        }
        DBHandler.database.useTransaction {
            DBHandler.database
                .delete(TableClassificationInfo) { it.id eq id }
        }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "操作成功",
                ""
            )
        )
    }

    // 获取分类列表
    get("/classification/list") {
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val list = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .limit(limit, offset)
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                list
            )
        )
    }

    // 搜索
    get("/classification/query") {
        val query = call.request.queryParameters["query"] ?: return@get let {
            call.respond(
                BaseResponse(
                    ErrorCode.MISSING_PARAMS,
                    "参数不完整",
                    ""
                )
            )
        }
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val list = DBHandler.database
            .from(TableClassificationInfo)
            .select()
            .where { TableClassificationInfo.name like query }
            .limit(limit, offset)
            .map { ClassificationData(it[TableClassificationInfo.id]!!, it[TableClassificationInfo.name]!!, it[TableClassificationInfo.avatar]!!, it[TableClassificationInfo.description]!!) }
        call.respond(
            BaseResponse(
                ErrorCode.OK,
                "",
                Pager(
                    size = list.size,
                    offset = offset,
                    data = list
                )
            )
        )
    }
}

suspend fun PipelineContext<Unit, ApplicationCall>.assertArgsNonNull(params: Parameters, vararg args: String): Boolean {
    return args
        .all { params[it] != "" }
        .also {
            if (!it) {
                call.respond(
                    BaseResponse(
                        ErrorCode.MISSING_PARAMS,
                        "参数不完整",
                        ""
                    )
                )
            }
        }
}