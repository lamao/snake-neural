package com.mygdx.network.util

import com.mygdx.game.model.Point
import kotlin.math.abs

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
object DirectionsUtil {

    fun isPositionCloserToFoodPosition(
        position: Point,
        foodPosition: Point,
        nextDirection: Point
    ): Boolean {

        val currentDistance = foodPosition - position
        val nextDistance = foodPosition - position - nextDirection

        return abs(nextDistance.x) + abs(nextDistance.y) < abs(currentDistance.x) + abs(currentDistance.y)
    }
}