package com.mygdx.game.model

/**
 * Created by vyacheslav.mischeryakov
 * Created 01.11.2021
 */
class Grid(val width:Int, val height:Int, init: (Int, Int) -> GameObject) {

    private val grid: Array<Array<GameObject>> = Array(size = height, init = {
            y -> Array(size = width, init = { x -> init(x,y) })
    })

    fun get(x:Int, y:Int):GameObject = grid[y][x]
}