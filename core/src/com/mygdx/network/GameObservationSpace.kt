package com.mygdx.network

import com.mygdx.network.gameState.GameState
import com.mygdx.network.gameState.InputName
import com.mygdx.network.network.NetworkFactory
import org.deeplearning4j.rl4j.space.ObservationSpace
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
/**
 * Game observation space. Shape is [1, 4] as we observe 4 inputs. Starting from the snake head we "look" at position
 * that is UP, RIGHT, DOWN and LEFT of the head.
 *
 * @author mirza
 */
class GameObservationSpace : ObservationSpace<GameState> {

    override fun getName(): String {
        return "GameObservationSpace"
    }

    override fun getShape(): IntArray {
        return intArrayOf(
            1, InputName.values().size
        )
    }

    override fun getLow(): INDArray {
        return Nd4j.create(LOWS)
    }

    override fun getHigh(): INDArray {
        return Nd4j.create(HIGHS)
    }

    companion object {
        private val LOWS = createValueArray(NetworkFactory.LowValue)
        private val HIGHS = createValueArray(NetworkFactory.HighValue)
        private fun createValueArray(value: Double): DoubleArray {
            val values = DoubleArray(InputName.values().size)
            for (i in 0 until InputName.values().size) {
                values[i] = value
            }
            return values
        }
    }
}