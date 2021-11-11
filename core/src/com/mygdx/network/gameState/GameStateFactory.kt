package com.mygdx.network.gameState

import com.mygdx.game.model.Field
import com.mygdx.game.model.Point
import com.mygdx.network.gameState.InputName.*
import com.mygdx.network.util.DirectionsUtil
import java.util.*

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
object GameStateFactory {

    private const val NoWay = -1.00
    private const val Neutral = 0.00
    private const val AppleAhead = 1.00

    fun buildGameState(field: Field): GameState {
        val flags = DoubleArray(values().size)
        flags[TopCellRate.ordinal] = construct(field, Point(0, 1))
        flags[RightCellRate.ordinal] = construct(field, Point(1, 0))
        flags[BottomCellRate.ordinal] = construct(field, Point(0, -1))
        flags[LeftCellRate.ordinal] = construct(field, Point(-1, 0))

        val moveDirection = field.syncSnake.moveDirection
        flags[DirectionX.ordinal] = moveDirection.x.toDouble()
        flags[DirectionY.ordinal] = moveDirection.y.toDouble()

        return GameState(flags)
    }

    /**
     * Calculate the state for given direction. Based on forwarded direction we check if head (or snake) can move in
     * that direction, and is food in that direction also. -AppleAhead indicates that snake cant go that way, AppleAhead indicates
     * that food is in that direction, and Neutral indicates that we can go that way but there is not food.
     *
     * @param snakePosition Current snake position.
     * @param foodPosition Current food position.
     * @param direction Direction in which we are checking.
     * @return Returns calculated state value.
     */
    private fun construct(field: Field, direction: Point): Double {
        // Position of snakes head
        val head: Point = field.syncSnake.position
        val nextPosition: Point = head + direction
        if (field.isOutside(nextPosition) || field.syncSnake.isIntersectsTail(nextPosition)) {
            return NoWay
        }

        return if (DirectionsUtil.isPositionCloserToFoodPosition(head, field.syncApple.position, direction)) {
            AppleAhead
        } else {
            Neutral
        }
    }

    /**
     * Get the index of max value in the array.
     *
     * @param values Array of values.
     * @return Returns found index.
     */
    fun getActionId(values: DoubleArray): Int {
        var result = 0
        for (i in values.indices) {
            if (values[i] > values[result]) {
                result = i
            }
        }
        return result
    }

}