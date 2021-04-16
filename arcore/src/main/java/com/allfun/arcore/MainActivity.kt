package com.allfun.arcore

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.ar.core.ArCoreApk
import com.google.ar.core.ArCoreApk.InstallStatus
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        maybeEnableArButton()
        btn_test.setOnClickListener {
            checkPermission {
                Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
                looperForFrame()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun looperForFrame() {
        thread {
            while (true) {
                mSession?.let {
                    val frame = it.update()
                    val camera = frame.camera
                    val pose = camera.pose
                    val translationF = pose.translation
                    runOnUiThread {
                        tv_state.text = """
                       x= ${translationF[0]}
                       y= ${translationF[0]}
                       z= ${translationF[0]}
                    """
                    }
                }
                Thread.sleep(1000)
            }
        }
    }

    val mRequestCode = 199


    private fun checkPermission(block: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

                block()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(this, "Rationale", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 199)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            mRequestCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "申请通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "申请未通过", Toast.LENGTH_SHORT).show()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    private var mUserRequestedInstall = true
    private var mSession: Session? = null
    override fun onResume() {
        super.onResume()
        try {
            if (mSession == null) {
                when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    InstallStatus.INSTALLED -> {          // Success, create the AR session.
                        mSession = Session(this)
                        mSession?.resume()
                        Toast.makeText(this, "已安装", Toast.LENGTH_SHORT).show()
                    }
                    InstallStatus.INSTALL_REQUESTED -> {
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        mUserRequestedInstall = false
                        return
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // Display an appropriate message to the user and return gracefully.
            Toast.makeText(this, "TODO: handle exception $e", Toast.LENGTH_LONG).show()
            return
        }
    }


    private fun maybeEnableArButton() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            // Re-query at 5Hz while compatibility is checked in the background.
            window.decorView.postDelayed(Runnable { maybeEnableArButton() }, 200)
        }
        if (availability.isSupported) {
            with(btn_test) {
                visibility = View.VISIBLE
                isEnabled = true
                text = "AR可用"
            }


            // indicator on the button.
        } else { // Unsupported or unknown.
            with(btn_test) {
                visibility = View.INVISIBLE
                isEnabled = false
                text = "AR不可用"
            }
        }
    }


}
