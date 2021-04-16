package com.android.oss_speed_test

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.blankj.utilcode.util.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {
    val TAG = "Speed Test"
    val path: String by lazy {
        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + File.separator + "panos.zip"
    }

    val downPath: String by lazy {
        getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!.absolutePath + File.separator + "depths.zip"
    }
    var times=1
    var uploadText=""
    var downloadText=""
    var uploadSpeed=0.0
    var downSpeed=0.0
    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        copyFile2SD()
        initSOO()
        tv_status.setOnClickListener {
            tv_status.isEnabled = false
            lifecycleScope.launch(Dispatchers.IO) {
                val time = measureTime {
                    uploadWithOSS()
                    uploadWithOSS()
                    uploadWithOSS()
                }
                withContext(Dispatchers.Main){
                    uploadSpeed = File(path).length() / time.inSeconds / 1024f / 1024f*3f
                    uploadText="上传大小：${getNoMoreThanTwoDigits((File(path).length()/ 1024f / 1024f).toDouble())}M 上传用时：${getNoMoreThanTwoDigits(time.inSeconds/3f)}S 上传速度=${getNoMoreThanTwoDigits(uploadSpeed)} M/S"
                    tv_status.text = uploadText
                    times=1
                }

                val downTime = measureTime {
                    download()
                    download()
                    download()
                }
                withContext(Dispatchers.Main){
                    delay(1000)
                    downSpeed = File(downPath).length() / downTime.inSeconds / 1024f / 1024f*3f
                    downloadText="下载大小：${getNoMoreThanTwoDigits((File(downPath).length()/ 1024f / 1024f).toDouble())}M 下载用时：${getNoMoreThanTwoDigits(downTime.inSeconds/3f)}S 下载速度=${getNoMoreThanTwoDigits(downSpeed)} M/S"
                    tv_status.isEnabled = true
                    tv_status.text = "$uploadText \n $downloadText"
                    times=1
                }


            }
        }

    }

    fun getNoMoreThanTwoDigits(number: Double): String {
        val format = DecimalFormat("0.##")
        //未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number)
    }

    private fun copyFile2SD() {
        val inputStream: InputStream = assets.open("panos.zip")
        val newFile =
            File(path)
        val fos = FileOutputStream(newFile)

        var len = -1
        val buffer = ByteArray(1024)
        while (inputStream.read(buffer).also { len = it } != -1) {
            fos.write(buffer, 0, len)
        }
        fos.close()
        inputStream.close()
    }

    private fun getEndpoint(): EndpointBean.DataBean? {
        val url =
            "http://user.3dnest.cn/nest/USER_OssList?platform=Android&version=1&userName=yanghai@3dnest.cn&type=1"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        val newCall = okHttpClient.newCall(request)
        var result: EndpointBean.DataBean? = null
        try {
            var execute = newCall.execute()
            execute.body()?.also {
                val endpoint = GsonUtils.fromJson(it.string(), EndpointBean::class.java)
                result = endpoint.data[0]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    private fun getOssToken(): OSSTokenBean.DataBean? {
        val url = "http://user.3dnest.cn/nest/USER_GetOssToken?userName=yanghai@3dnest.cn"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        val newCall = okHttpClient.newCall(request)
        val execute = newCall.execute()
        return execute.body()?.let {
            GsonUtils.fromJson(it.string(), OSSTokenBean::class.java).data
        }
    }


    var oss: OSSClient? = null
    private fun initSOO() {
        lifecycleScope.launch(Dispatchers.IO) {
            val endpoint = getEndpoint()
            val ossToken = getOssToken()
            if (endpoint != null && ossToken != null) {
                Log.e(TAG, "uploadWithOSS start ")
                val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                    ossToken.accessKeyId,
                    ossToken.accessKeySecret,
                    ossToken.securityToken
                )
                val conf = ClientConfiguration()
                conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒
                conf.socketTimeout = 15 * 1000 // socket超时，默认15秒
                conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个
                conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次
                oss = OSSClient(this@MainActivity, endpoint.endPoint, credentialProvider, conf)
                Log.e(TAG, "uploadWithOSS OSS init success")
                withContext(Dispatchers.Main) {
                    tv_status.text = "初始化成功,点我开始测试"
                }
            } else {
                Log.e(TAG, "uploadWithOSS OSS init fail")
                withContext(Dispatchers.Main) {
                    tv_status.text = "初始化失败"
                }
            }
        }
    }

    fun getModel(): String {
        val networkType = NetworkUtils.getNetworkType()
        return Build.BRAND + "_" + Build.MODEL + "_Android " + Build.VERSION.RELEASE + "_" + networkType.name
    }

    val Long.dateStr: String
        get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(this))

    private fun uploadWithOSS() {
        var fileName = "${getModel()}_${System.currentTimeMillis().dateStr}.jpg"
        val put =
            PutObjectRequest("nest-pano-pjt", "speed_test/$fileName", path).apply {
                this.setProgressCallback { request, currentSize, totalSize ->
                    runOnUiThread {
                        tv_status.text = "第${times}次上传。上传进度：${currentSize * 100 / totalSize}%"
                    }
                }

            }
        oss?.putObject(put)
        times++
    }


    private  fun download(){
        val remotePath="https://nest-data-bk.oss-cn-beijing.aliyuncs.com/cs/predict20210330-161133.zip"
        DownLoadManager.downFile(downPath,remotePath){
            runOnUiThread {
                tv_status.text =  "第${times}次下载。下载进度：$it%"
            }
        }
        times++
    }
}
