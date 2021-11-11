package com.mygdx.network

import com.mygdx.game.model.Field
import com.mygdx.network.action.Action
import com.mygdx.network.gameState.GameState
import com.mygdx.network.gameState.GameStateFactory
import com.mygdx.network.reward.RewardFactory
import org.deeplearning4j.gym.StepReply
import org.deeplearning4j.rl4j.mdp.MDP
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.space.ObservationSpace

/**
 * Created by vyacheslav.mischeryakov
 * Created 04.11.2021
 */
class Environment(private val field: Field) : MDP<GameState, Int, DiscreteSpace> {
    private val actionSpace = DiscreteSpace(Action.values().size)
    private var currentStep = 0

    override fun getObservationSpace(): ObservationSpace<GameState> {
        return GameObservationSpace()
    }

    override fun getActionSpace(): DiscreteSpace {
        return actionSpace
    }

    override fun reset(): GameState {
        currentStep = 0
        field.reset()
        return GameStateFactory.buildGameState(field)
    }

    override fun close() {}

    override fun step(actionIndex: Int): StepReply<GameState> {
        // Find action based on action index
        val actionToTake: Action = Action.fromId(actionIndex)

        // Change direction based on action and move the snake in that direction
        field.performAction(actionToTake)

        // Get reward
        val reward: Double = RewardFactory.calculatedReward(field)


        // Get current state
        val observation: GameState = GameStateFactory.buildGameState(field)
        field.syncSnake.perception = observation.toArray()
        currentStep++
        if (LearningSettings.VerboseLogging) {
            println("GameState: ${observation.toArray()}. Reward: $reward")
        }

        // If you want to see what is the snake doing while training increase this value
        try {
            Thread.sleep(LearningSettings.TrainStepDelayMillis)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        if (currentStep > LearningSettings.MaxLearningSteps) {
            println("Stuck...")
        }

        return StepReply<GameState>(
            observation,
            reward,
            isDone,
            "SnakeDl4j"
        )
    }

    override fun isDone(): Boolean {
        return field.terminated || currentStep > LearningSettings.MaxLearningSteps
    }

    override fun newInstance(): MDP<GameState, Int, DiscreteSpace> {
        field.reset()
        return Environment(field)
    }


}