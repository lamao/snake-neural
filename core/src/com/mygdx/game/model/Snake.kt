package com.mygdx.game.model

/**
 * Created by vyacheslav.mischeryakov
 * Created 31.10.2021
 */
class Snake(position: Point) : GameObject(position) {

    var moveDirection: Point = Point(0, 1)

    private var tail: MutableList<Point> = MutableList(1) { position - Point(0, 1) }

    var perception: DoubleArray = DoubleArray(4)

    val syncTail
        @Synchronized get() = tail

    fun move() {
        if (syncTail.isNotEmpty()) {
            syncTail.add(0, position)
            syncTail.removeLast()
        }
        this.position = position + moveDirection
    }

    fun eat() {
        syncTail.add(0, position)
        this.position = position + moveDirection
    }

    fun isIntersects(pos: Point): Boolean {
        return position == pos || syncTail.any { it == pos }
    }

    fun isIntersectsTail(pos: Point): Boolean {
        return syncTail.any { it == pos }
    }
}