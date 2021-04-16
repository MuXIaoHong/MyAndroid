package com.allfun.doublenetwork

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.arashivision.sdkcamera.camera.InstaCameraManager
import com.arashivision.sdkcamera.camera.callback.ICameraChangedCallback
import com.liulishuo.filedownloader.FileDownloader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(), ICameraChangedCallback {
    var isConnectCamera = false
    var isCellEnable = false
    var isWifiEnable = false
    val TAG = "DoubleNetwork"
    val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InstaCameraManager.getInstance().registerCameraChangedCallback(this)
        initSOO()
        phone_info.text = "手机型号_系统版本：" + getModel()
        et_name.run {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }

        btn_connect_device_wifi.setOnClickListener {
            openWifiSettings()
        }

        btn_test.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {

                try {

                    btn_test.isEnabled = false
                    btn_test.text = "正在连接相机..."

                    tryUseWifiBind()
                    var times = 0
                    while (!isWifiEnable) {
                        delay(500)
                        if (times++ == 10) {
                            btn_test.text = "拍摄失败,wifi不可用，点我重试！"
                            btn_test.isEnabled = true
                            return@launch
                        }
                    }

                    while (!isConnectCamera) {
                        delay(500)
                        //连接wifi
                        InstaCameraManager.getInstance()
                            .openCamera(InstaCameraManager.CONNECT_TYPE_WIFI)
                    }

                    if (isWifiEnable) {
                        btn_test.text = "拍摄中,请稍候..."
                        btn_test.isEnabled = false
                        takePhoto()
                    } else {
                        btn_test.text = "拍摄失败,wifi不可用，点我重试！"
                        btn_test.isEnabled = true
                    }


                } catch (e: Exception) {
                    btn_test.text = "拍摄失败,点我重试！"
                    btn_test.isEnabled = true
                    e.printStackTrace()
                }
            }
        }

    }

    var fileName = ""
    var path = ""
    var times = 0;
    private suspend fun takePhoto() = withContext(Dispatchers.Default) {
        var id: String? = null
        while (id == null) {
            CameraUtils.setHDRCaptureMode()
            delay(500)
            id = CameraUtils.startCapture()
            Log.d(TAG, "takePhoto id===> $id")
        }
        var fileUrl: String? = null
        for (i in 0..59) {
            //循环读取，避免一开始取不到
            val oscBean: CameraUtils.OSCBean? = CameraUtils.queryResult(id)
            if (oscBean != null) {

                fileUrl = oscBean.getFileUrl()
                break
            } else {
                Thread.sleep(100)
            }
        }
        Log.d(TAG, "takePhoto  fileUrl===> $fileUrl")

        fileName = "${getModel()}_${et_name.text}_${System.currentTimeMillis().dateStr}.jpg"
        path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path + "/$fileName"
        Log.d(TAG, "takePhoto  localPath===> $path")
        CameraUtils.downFile(path, fileUrl)
        //上传
        tryUseCellBind()
        var times = 0;
        while (!isCellEnable) {
            delay(500)
            if (times++ == 10) {
                runOnUiThread {
                    btn_test.text = "拍摄失败,移动数据不可用，点我重试！"
                    btn_test.isEnabled = true
                }
                return@withContext
            }
        }
        uploadWithOSS()

    }

    override fun onCameraStatusChanged(enabled: Boolean) {
        isConnectCamera = enabled
        if (enabled) {
            FileDownloader.setup(this@MainActivity)
        } else {
            btn_test.text = "连接相机失败,点我重试！"
            btn_test.isEnabled = true
        }
        Log.d(TAG, "onCameraStatusChanged $enabled")
    }

    override fun onCameraConnectError() {
        btn_test.text = "连接相机失败,点我重试！"
        btn_test.isEnabled = true
        Log.d(TAG, "onCameraStatusChanged false")
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun tryUseWifiBind() {
        Log.d(TAG, "tryUseWifi start")
        val requestBuilder = NetworkRequest.Builder()
            .apply {
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        connectivityManager.requestNetwork(requestBuilder.build(), wifiCallback)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun tryUseCellBind() {
        Log.d(TAG, "tryUseCellBind start")
        val requestBuilder = NetworkRequest.Builder()
            .apply {
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }

        connectivityManager.requestNetwork(requestBuilder.build(), cellcallback)

    }


    val cellcallback =
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        ConnectivityManager.setProcessDefaultNetwork(network)
                        Log.d(TAG, "tryUseCellBind setProcessDefaultNetwork")
                    } else {
                        Log.d(TAG, "tryUseCellBind bindProcessToNetwork")
                        connectivityManager.bindProcessToNetwork(network)
                    }

                    isCellEnable = true

                } catch (e: IllegalStateException) {
                    isCellEnable = false
                    Log.e(
                        TAG,
                        "ConnectivityManager.NetworkCallback.onAvailable: ",
                        e
                    )
                }


            }

            override fun onUnavailable() {
                isCellEnable = false
                btn_test.text = "没有可用的移动数据网络"
            }
        }

    val wifiCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    ConnectivityManager.setProcessDefaultNetwork(network)
                    Log.d(TAG, "tryUseWifi setProcessDefaultNetwork")
                } else {
                    Log.d(TAG, "tryUseWifi bindProcessToNetwork")
                    connectivityManager.bindProcessToNetwork(network)
                }
                isWifiEnable = true
            } catch (e: IllegalStateException) {
                isWifiEnable = false
                Log.e(
                    TAG,
                    "ConnectivityManager.NetworkCallback.onAvailable: ",
                    e
                )
            }


        }

        override fun onUnavailable() {
            btn_test.text = "没有可用的wifi"
            isWifiEnable = false
        }

    }


    private fun getEndpoint(): EndpointBean.DataBean? {
        val url =
            "http://user.3dnest.cn/nest/USER_OssList?platform=Android&version=1&userName=yanghai@3dnest.cn&type=1"
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        val newCall = okHttpClient.newCall(request)
        val execute = newCall.execute()
        return execute.body()?.let {
            val endpoint = GsonUtils.fromJson(it.string(), EndpointBean::class.java)
            endpoint.data[0]
        }
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
        tryUseCellBind()

        lifecycleScope.launch(Dispatchers.Default) {
            var times = 0;
            while (!isCellEnable) {
                delay(500)
                if (times++ == 10) {
                    runOnUiThread {
                        btn_test.text = "OSS初始化失败！"
                        btn_test.isEnabled = true
                    }
                    return@launch
                }
            }

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
            } else {
                Log.e(TAG, "uploadWithOSS OSS init fail")
            }
        }
    }

    private fun uploadWithOSS() {
        val put =
            PutObjectRequest("nest-pano-pjt", "double_test/$fileName", path).apply {
                this.setProgressCallback { request, currentSize, totalSize ->
                    runOnUiThread {
                        btn_test.text = "上传进度：${currentSize * 10 / totalSize}%"
                        Log.d(TAG, "${btn_test.text}")
                    }
                }

            }
        oss?.putObject(put)
        runOnUiThread {
            btn_test.text = "测试成功"
            btn_test.isEnabled = true
        }

    }

    fun openWifiSettings() {
        DNApplication.context.startActivity(
            Intent(Settings.ACTION_WIFI_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }


    fun getModel(): String {
        return Build.BRAND+"_"+Build.MODEL + "_Android " + Build.VERSION.RELEASE
    }

    override fun onDestroy() {
        super.onDestroy()
        InstaCameraManager.getInstance().unregisterCameraChangedCallback(this)
    }
}

val Long.dateStr: String
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(this))