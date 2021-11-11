package com.mygdx.network.action

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
enum class Action {

    TurnLeft,
    KeepMoving,
    TurnRight;

    companion object {
        fun fromId(id: Int): Action = values()[id]
    }
}