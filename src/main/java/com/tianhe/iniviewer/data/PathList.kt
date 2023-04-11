package com.tianhe.iniviewer.data

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.tianhe.iniviewer.utils.Log
import java.lang.StringBuilder

/**
 * Created by tianhe on 2023/4/8
 */
object PathList {

    private const val TAG = "PathManager"
    private const val SEPARATOR = ";"
    private const val KEY = "com.tianhe.iniviewer.IniPathList"

    val pathList = arrayListOf<String>()
    private lateinit var project: Project

    fun init(project: Project) {
        this.project = project
        restore()
    }

    fun addPath(path: String): Boolean {
        if (pathList.contains(path)) {
            return false
        }
        pathList.add(path)
        Log.d(TAG, "addPath: path=$path")
        store()
        return true
    }

    fun containPath(path: String): Boolean {
        return pathList.contains(path)
    }

    fun removePath(path: String) {
        pathList.remove(path)
        store()
    }


    private fun store() {
        val paths = list2String(pathList)
        PropertiesComponent.getInstance(project).setValue(KEY, paths)
        Log.d(TAG, "store: pathList=$paths")
    }

    private fun restore() {
        val paths = PropertiesComponent.getInstance(project).getValue(KEY)
        Log.d(TAG, "restore: paths=$paths")
        pathList.clear()
        paths?.let {
            string2List(it)
        }?.let { list ->
            list.forEach {
                if (it.isNotEmpty()) {
                    pathList.add(it)
                }
            }
        }
    }


    private fun list2String(list: List<String>): String {
        val sb = StringBuilder()
        list.withIndex().forEach {
            sb.append(it.value)
            if (it.index < list.size - 1) {
                sb.append(SEPARATOR)
            }
        }
        return sb.toString()
    }


    private fun string2List(str: String): List<String> {
        return str.split(SEPARATOR)
    }


    fun getLatestPath(): String? {
        if (pathList.isEmpty()) {
            return null
        }
        return pathList[pathList.size - 1]
    }

}