# 迫害API

> v0.0.1

# 分类

## GET 获取分类

GET /classification

### 请求参数

| 名称 | 位置  | 类型    | 必选  | 说明            |
| ---- | ----- | ------- | ----- | --------------- |
| id   | query | integer | false | 分类id 二选一   |
| name | query | string  | false | 分类名称 二选一 |

## GET 获取图片

GET /image

### 请求参数

| 名称 | 位置  | 类型    | 必选  | 说明            |
| ---- | ----- | ------- | ----- | --------------- |
| id   | query | integer | true | 图片id   |

## POST 获取图片

POST /image/remove

### 请求参数

| 名称 | 位置  | 类型    | 必选  | 说明            |
| ---- | ----- | ------- | ----- | --------------- |
| id   | body | integer | true | 图片id   |


## POST 上传图片到分类

POST /classification/upload

### 请求参数

| 名称 | 位置  | 类型    | 必选  | 说明            |
| ---- | ----- | ------- | ----- | --------------- |
| cid   | body | integer | false | 分类id   |
| image | body | string  | false | 图片url |

## GET 获取分类中的图片

GET /classification/images

### 请求参数

| 名称   | 位置  | 类型    | 必选  | 说明             |
| ------ | ----- | ------- | ----- | ---------------- |
| id     | query | integer | false  | 分类id, 不填则全局查询 |
| limit  | query | integer | false | 数量限制 默认20  |
| offset | query | integer | false | 数据偏移量 默认0 |

## POST 创建分类

POST /classification/create

### 请求参数

| 名称        | 位置 | 类型   | 必选  | 说明     |
| ----------- | ---- | ------ | ----- | -------- |
| name        | body | string | true  | 分类名称 |
| avatar      | body | string | false | 分类头像 |
| description | body | string | false | 分类介绍 |

## POST 删除分类

POST /classification/remove

### 请求参数

| 名称 | 位置 | 类型    | 必选 | 说明   |
| ---- | ---- | ------- | ---- | ------ |
| id   | body | integer | true | 分类id |

## GET 获取全部分类

GET /classification/list

### 请求参数

| 名称   | 位置  | 类型    | 必选  | 说明             |
| ------ | ----- | ------- | ----- | ---------------- |
| limit  | query | integer | false | 数量限制 默认20  |
| offset | query | integer | false | 数据偏移量 默认0 |

## GET 搜索分类

GET /classification/query

### 请求参数

| 名称   | 位置  | 类型    | 必选  | 说明            |
| ------ | ----- | ------- | ----- | --------------- |
| query  | query | string  | true  | 关键词          |
| limit  | query | integer | false | 数量限制 默认20 |
| offset | query | integer | false | 数据偏移 默认0  |

# 其他

## POST 上传图片

POST /upload

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|image|body| image   |true| 图片   |

