package com.allfun.arcore

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_scene.*

/**
 * 通过ARCore获取手机位置和旋转角度
 */
class SceneActivity : AppCompatActivity() {
    private var arFragment: ArFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkIsSupportedDeviceOrFinish(this)) return
        setContentView(R.layout.activity_scene)
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment?
        tv_content.setOnClickListener {

            val camera = arFragment!!.arSceneView.scene.camera
            val localPosition = camera.localPosition
            val localRotation = camera.localRotation
            val worldPosition = camera.worldPosition
            val worldRotation = camera.worldRotation
            val append = """
                    
                   local location -->：$localPosition
                   local rotation -->：$localRotation
                   
                   world location -->：$worldPosition
                   world rotation -->：$worldRotation
                    
                    """.trimIndent()
            tv_content.text = tv_content.text.toString() + append

        }
    }


    private fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {

            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        val openGlVersionString =
            (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (openGlVersionString.toDouble() < 3.0) {

            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                .show()
            activity.finish()
            return false
        }
        return true
    }
}