package com.ever.funquizz.repository

import com.ever.funquizz.model.Level

class LevelRepository {

    fun getLevels(levelsStrings :List<String>): List<Level>{
        val levels : MutableList<Level> = mutableListOf()
        for ((id, level) in levelsStrings.withIndex()){
            levels.add(Level(id+1, level))
        }
        return levels.toList()
    }
}