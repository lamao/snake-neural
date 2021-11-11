package com.mygdx.network.network

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense
import org.nd4j.linalg.learning.config.RmsProp
import java.io.File

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
/**
 * Util class containing methods to build the neural network and its configuration.
 *
 * @author mirza
 */
object NetworkFactory {
    /**
     * Lowest value of the observation (e.g. player will die -1, nothing will happen 0, will move closer to the food 1)
     */
    const val LowValue = -1.0

    /**
     * Highest value of the observation (e.g. player will die -1, nothing will happen 0, will move closer to the food 1)
     */
    const val HighValue = 1.0

    fun buildConfig(): QLearningConfiguration {
        return QLearningConfiguration.builder()
            .seed(123L)
            .maxEpochStep(200)
            .maxStep(15000)
            .expRepMaxSize(150000)
            .batchSize(128)
            .targetDqnUpdateFreq(500)
            .updateStart(10)
            .rewardFactor(0.01)
            .gamma(0.99)
            .errorClamp(1.0)
            .minEpsilon(0.1)
            .epsilonNbStep(1000)
            .doubleDQN(true)
            .build()
    }

    fun buildDQNFactory(): DQNFactoryStdDense {
        val build = DQNDenseNetworkConfiguration.builder()
            .l2(0.001)
            .updater(RmsProp(0.000025))
            .numHiddenNodes(100)
            .numLayers(2)
            .build()
        return DQNFactoryStdDense(build)
    }

    fun loadNetwork(networkName: String): MultiLayerNetwork {
        return MultiLayerNetwork.load(File(networkName), true)
    }

}