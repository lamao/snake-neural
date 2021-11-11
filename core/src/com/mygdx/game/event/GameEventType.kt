package com.mygdx.game.event

/**
 * Created by vyacheslav.mischeryakov
 * Created 03.11.2021
 */
enum class GameEventType(val award:Int) {
    GameOver(-100),
    Eat(10)
}