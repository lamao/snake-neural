package com.mygdx.game.model

import com.badlogic.gdx.math.MathUtils
import com.mygdx.game.DirectionCalculator
import com.mygdx.network.action.Action

/**
 * Created by vyacheslav.mischeryakov
 * Created 31.10.2021
 */
class Field(private val width: Int, private val height: Int) {

    private var snake: Snake = Snake(Point(0, 0))

    private var apple: GameObject = GameObject(Point(0, 0))

    val syncSnake
        @Synchronized get() = snake
    val syncApple
        @Synchronized get() = apple

    var score: Int = 0

    var terminated: Boolean = false;

    fun isOutside(pos: Point): Boolean = pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height

    fun reset() {
        score = 0
        terminated = false
        spawnSnake()
        spawnApple()
    }

    fun terminate() {
        terminated = true
    }

    @Synchronized
    fun performAction(action: Action) {
        if (action == Action.TurnLeft) {
            snake.moveDirection = DirectionCalculator.getLeftDirection(snake.moveDirection)
        } else if (action == Action.TurnRight) {
            snake.moveDirection = DirectionCalculator.getRightDirection(snake.moveDirection)
        }

        val newPosition = snake.position + snake.moveDirection
        if (newPosition == apple.position) {
            snake.eat()
        } else {
            snake.move()
        }

        if (snake.isIntersectsTail(snake.position)) {
            terminate()
        } else if (isOutside(snake.position)) {
            terminate()
        }


        if (snake.position == apple.position) {
            score++
            spawnApple()
        }
    }

    private fun spawnSnake() {
        snake = Snake(Point(width / 2, height / 2))
    }

    private fun spawnApple() {
        var applePosition = getRandomPosition()
        while (snake.isIntersects(applePosition)) {
            applePosition = getRandomPosition()
        }

        apple = GameObject(applePosition)
    }

    private fun getRandomPosition(): Point {
        return Point(
            x = MathUtils.random(0, width - 1),
            y = MathUtils.random(0, height - 1)
        )
    }

}