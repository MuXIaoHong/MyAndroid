package com.nan.myandroid.keep_alive

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.nan.myandroid.MyApplication


/**
 *author：93289
 *date:2020/5/18
 *dsc:
 */
object WhiteListUtils {
    fun enterWhiteListSetting() {
        try {
            getSettingIntent()
        } catch (e: Exception) {
        }
    }

    private fun getSettingIntent() {
        var componentNameList = mutableListOf<ComponentName>()

        val brand = Build.BRAND
        when (brand.toLowerCase()) {
            "samsung" -> {
                componentNameList.add(
                    ComponentName(
                        "com.samsung.android.sm",
                        "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity"
                    )
                )

            }

            "huawei" -> {
                componentNameList.add(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                    )
                )
            }
            "xiaomi" -> {
                componentNameList.add(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                )
            }
            "vivo" -> {
//                componentNameList.add(
//                    ComponentName(
//                        "com.iqoo.secure",
//                        "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"
//                    )
//                )
//                componentNameList.add(
//                    ComponentName(
//                        "com.vivo.permissionmanager",
//                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
//                    )
//                )
                val add = ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.PurviewTabActivity"
                )
                componentNameList.add(
                    add
                )
            }

            "oppo" -> {

//                componentNameList.add(
//                    ComponentName(
//                        "com.coloros.safecenter",
//                        "com.coloros.safecenter.startupapp.StartupAppListActivity"
//                    )
//                )
//                componentNameList.add(
//                    ComponentName(
//                        "com.coloros.oppoguardelf",
//                        "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity"
//                    )
//                )
//                componentNameList.add(
//                    ComponentName(
//                        "com.coloros.safe",
//                        "com.coloros.safe.permission.startup.StartupAppListActivity"
//                    )
//                )
//                componentNameList.add(
//                    ComponentName(
//                        "com.coloros.safe",
//                        "com.coloros.safe.permission.startupapp.StartupAppListActivity"
//                    )
//                )
//                componentNameList.add(
//                    ComponentName(
//                        "com.coloros.safecenter",
//                        "com.coloros.safecenter.startup.StartupAppListActivity"
//                    )
//                )
                componentNameList.add(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.privacypermissionsentry.PermissionTopActivity"
                    )
                )
            }
            "360" -> {
                componentNameList.add(
                    ComponentName(
                        "com.yulong.android.coolsafe",
                        "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity"
                    )
                )
            }
            "meizu" -> {
                componentNameList.add(
                    ComponentName(
                        "com.meizu.safe",
                        "com.meizu.safe.permission.SmartBGActivity"
                    )
                )

            }
            "oneplus" -> {
                componentNameList.add(
                    ComponentName(
                        "com.oneplus.security",
                        "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity"
                    )
                )
            }
            else -> {
            }
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        for (component in componentNameList) {
            intent.component = component
            if (MyApplication.context.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            ) {
                MyApplication.context.startActivity(intent)
                break
            }

        }


    }
}