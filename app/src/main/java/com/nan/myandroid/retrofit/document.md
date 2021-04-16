### 通用model类
```
data class BaseResponse<T>(
    val msg: String,
    val detail: String,
    val code: Int,
    val data: T
){
    fun isSuccessful()= code==0
}
```
### 通用的带状态的数据加载类
```
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
```