package com.mygdx.network

import com.mygdx.game.model.Field
import com.mygdx.network.action.Action
import com.mygdx.network.gameState.GameState
import com.mygdx.network.gameState.GameStateFactory
import com.mygdx.network.network.NetworkFactory
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense
import org.nd4j.linalg.api.ndarray.INDArray
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
class LearningThread(private val field: Field) : Runnable {

    private val log = LoggerFactory.getLogger(LearningThread::javaClass.name)

    override fun run() {
        println("Started learning thread")
        // Give a name to the network we are about to train
        val randomNetworkName = "network-" + System.currentTimeMillis() + ".zip"

        // Create our training environment
        val mdp = Environment(field)
        val dql: QLearningDiscreteDense<GameState> = QLearningDiscreteDense(
            mdp,
            NetworkFactory.buildDQNFactory(),
            NetworkFactory.buildConfig()
        )

        // Start the training
        dql.train()
        mdp.close()

        if (LearningSettings.SaveNetwork) {
            try {
                dql.neuralNet.save(randomNetworkName)
            } catch (e: IOException) {
                log.error(e.message, e)
            }
        }

        // Evaluate just trained network
        evaluateNetwork(field, randomNetworkName)
    }

    private fun evaluateNetwork(field: Field, randomNetworkName: String) {
        val multiLayerNetwork: MultiLayerNetwork = NetworkFactory.loadNetwork(randomNetworkName)
        var highscore = 0
        for (i in 0..LearningSettings.EvaluationIteration) {

            // Reset the game
            field.reset()

            var score = 0
            while (!field.terminated) {
                try {
                    val state: GameState = GameStateFactory.buildGameState(field)

                    val output: INDArray = multiLayerNetwork.output(state.getMatrix(), false)

                    val data: DoubleArray = output.data().asDouble()
                    val maxValueIndex: Int = GameStateFactory.getActionId(data)

                    field.performAction(Action.fromId(maxValueIndex))
                    score = field.score
                    field.syncSnake.perception = GameStateFactory.buildGameState(field).toArray()

                    // Needed so that we can see easier what is the game doing
                    Thread.sleep(LearningSettings.EvaluationStepDelayMillis)
                } catch (e: Exception) {
                    log.error(e.message, e)
                    Thread.currentThread().interrupt()
                    field.terminate()
                }
            }
            log.info("Score of iteration '{}' was '{}'", i, score)
            if (score > highscore) {
                highscore = score
            }

        }
        log.info("Finished evaluation of the network, highscore was '{}'", highscore)
    }
}