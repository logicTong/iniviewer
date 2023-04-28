package com.tianhe.iniviewer.data

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.tianhe.iniviewer.logic.Consts
import com.tianhe.iniviewer.utils.Log
import java.io.File

/**
 * Created by tianhe on 2023/4/26
 */
object ImageDir {
    const val TAG = "ImageDir"

    private val KEY = "${Consts.getProjectKey()}.ImageDir"
    private lateinit var project: Project

    var directory: String? = null
        set(value) {
            field = value
            writeDirectory()
        }

    fun init(project: Project) {
        this.project = project
        readDirectory()
    }

    private fun readDirectory() {
        directory = PropertiesComponent.getInstance(project).getValue(KEY)
    }


    private fun writeDirectory() {
        PropertiesComponent.getInstance(project).setValue(KEY, directory)
        Log.d(TAG, "writeDirectory: directory = $directory")
    }

    fun hasImageDir(): Boolean {
        return directory?.isNotEmpty() ?: false
    }

    fun findFile(fileName: String): File? {
        val dir = directory ?: return null
        val dirFile = File(dir)
        if (dirFile.isDirectory && dirFile.exists()) {
            val matchFiles = dirFile.listFiles { dir, name ->
                name.equals(fileName)
            }
            return matchFiles?.takeIf { it.isNotEmpty() }?.get(0)
        }
        return null
    }


}