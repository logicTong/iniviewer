package com.tianhe.iniviewer.inifile

import com.tianhe.iniviewer.data.model.IniFile
import com.tianhe.iniviewer.utils.Log
import java.awt.EventQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by tianhe on 2023/4/9
 */
object IniDataLoader {
    const val TAG = "IniDataLoader"

    private val executor = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue())

    fun readIniFile(path: String, success: ReadSuccess, fail: ReadFail) {
        executor.execute {
            try {
                val result = IniFileReader.readIniFile(path)
                result?.let {
                    EventQueue.invokeLater {
                        Log.d(TAG, "readIniFile: success, path = $path")
                        success(IniFile(path, result))
                    }
                    return@execute
                }
            } catch (e: Exception) {
                Log.e(TAG, "readIniFile: error, path = $path", e)
                EventQueue.invokeLater {
                    fail(path, e)
                }
            }
        }
    }

    fun loadAllIniFile(paths: List<String>, success: ReadSuccess, fail: ReadFail, allSuccess: () -> Unit) {
        val totalSize = paths.size
        var count = 0
        val startTime = System.currentTimeMillis()
        paths.forEach {
            readIniFile(it, success = {
                success(it)
                count++
                if (count == totalSize) {
                    Log.d(TAG, "loadAllIniFile: finish, cast time =${System.currentTimeMillis() - startTime}")
                    allSuccess()
                }
            }, fail = { path, e ->
                count++
                fail(path, e)
            })
        }
    }

}

typealias ReadFail = (path:String, error: Exception) -> Unit
typealias ReadSuccess = (iniFile: IniFile) -> Unit