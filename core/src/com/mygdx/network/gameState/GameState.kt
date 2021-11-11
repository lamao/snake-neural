package com.mygdx.network.gameState

import org.deeplearning4j.rl4j.space.Encodable
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
class GameState(inputs: DoubleArray) : Encodable {



    private val inputs: DoubleArray

    override fun toArray(): DoubleArray {
        return inputs
    }

    override fun isSkipped(): Boolean {
        return false
    }

    override fun getData(): INDArray {
        return Nd4j.create(inputs)
    }

    fun getMatrix(): INDArray {
        return Nd4j.create(
            arrayOf(
                inputs
            )
        )
    }

    override fun dup(): Encodable? {
        return null
    }

    init {
        this.inputs = inputs
    }
}