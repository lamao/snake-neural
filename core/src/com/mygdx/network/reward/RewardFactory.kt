package com.mygdx.network.reward

import com.mygdx.game.model.Field
import com.mygdx.game.model.Point
import com.mygdx.network.util.DirectionsUtil
import java.util.*
import kotlin.math.abs

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
object RewardFactory {

    private const val Death = -100.0
    private const val Eat = 100.0
    private const val MoveToApple = 1.0
    private const val MoveFromApple = -1.0

    /**
     * Used to calculate reward for taken action.
     *
     * @param action Action that was taken.
     * @param snakePosition Current snake position.
     * @param foodPosition Current food position.
     * @return Returns calculated reward value.
     */
    fun calculatedReward(field: Field): Double {
        val snake = field.syncSnake
        val nextPosition: Point = snake.position + snake.moveDirection

        if (field.isOutside(nextPosition) || field.syncSnake.isIntersectsTail(nextPosition)) {
            return Death
        }
        if (nextPosition == field.syncApple.position) {
            return Eat
        }

        return if (DirectionsUtil.isPositionCloserToFoodPosition(nextPosition, field.syncApple.position, snake.moveDirection)) {
            MoveToApple
        } else {
            MoveFromApple
        }
    }


}