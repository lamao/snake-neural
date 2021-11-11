package com.mygdx.game

import com.mygdx.game.model.Point

/**
 * Created by vyacheslav.mischeryakov
 * Created 03.11.2021
 */
object DirectionCalculator {

    private val clockwiseDirections = arrayOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))


    fun getLeftDirection(direction: Point): Point {
        val directionIndex = clockwiseDirections.indexOf(direction)
        if (directionIndex == 0) {
            return clockwiseDirections.last()
        }
        return clockwiseDirections[directionIndex - 1]
    }

    fun getRightDirection(direction: Point): Point {
        val directionIndex = clockwiseDirections.indexOf(direction)
        if (directionIndex == clockwiseDirections.size - 1) {
            return clockwiseDirections.first()
        }
        return clockwiseDirections[directionIndex + 1]
    }
}