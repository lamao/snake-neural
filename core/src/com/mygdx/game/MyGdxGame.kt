package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.TimeUtils
import com.mygdx.game.event.GameEvent
import com.mygdx.game.model.Field
import com.mygdx.game.model.Point
import com.mygdx.network.LearningThread
import com.mygdx.network.action.Action
import kotlin.concurrent.thread


/**
 * Created by vyacheslav.mischeryakov
 * Created 21.10.2021
 */
class MyGdxGame (val field: Field, val controller: () -> Unit): ApplicationAdapter() {

    private val StepInterval = 300
    private val ReactionToKeyInterval = 150
    private val OneMilliseccond = 1000_000


    private val GridToDisplayRatioX: Float = Settings.Width.toFloat() / (Settings.GridWidth + 2)
    private val GridToDisplayRatioY: Float = Settings.Height.toFloat() / (Settings.GridHeight + 2)

    private val PaddingX = GridToDisplayRatioX / 5
    private val PaddingY = GridToDisplayRatioY / 5


    private val Transparent = Color(0f, 0f, 0f, 0.5f)
    private val GoodColor: Color = Color.GREEN.sub(Transparent)
    private val NeutralColor: Color = Color.YELLOW.sub(Transparent)
    private val BadColor: Color = Color.RED.sub(Transparent)


    private lateinit var camera: OrthographicCamera
    private lateinit var batch: SpriteBatch
    private lateinit var appleImage: Texture
    private lateinit var bodyImage: Texture
    private lateinit var headImage: Texture
    private lateinit var shapeRenderer: ShapeRenderer

    private var lastStepTime = 0L
    private var newAction: Action = Action.KeepMoving
    private var lastKeyPressTime = 0L

    private lateinit var controllerThread:Thread

    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        camera.setToOrtho(false, Settings.Width.toFloat(), Settings.Height.toFloat())
        appleImage = Texture(Gdx.files.internal("apple.png"))
        headImage = Texture(Gdx.files.internal("head.png"))
        bodyImage = Texture(Gdx.files.internal("body.png"))
        shapeRenderer = ShapeRenderer()

        controllerThread = thread(start = true, block = controller)
    }

    override fun render() {
//        val currentTime = TimeUtils.nanoTime()

//        if (currentTime - lastKeyPressTime > ReactionToKeyInterval * OneMilliseccond) {
//            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//                newAction = Action.TurnLeft
//            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//                newAction = Action.TurnRight
//            }
//            lastKeyPressTime = currentTime
//        }
//
//        if (currentTime - lastStepTime > StepInterval * OneMilliseccond) {
//            lastStepTime = currentTime
//
//            field.performAction(newAction)
//        }


        ScreenUtils.clear(Color.BLACK)
        camera.update()
        batch.projectionMatrix = camera.combined


        batch.begin()

        val apple = field.syncApple
        batch.draw(
            appleImage,
            toDisplayX(apple.position.x),
            toDisplayY(apple.position.y),
            GridToDisplayRatioX,
            GridToDisplayRatioY
        )

        val snake = field.syncSnake
        batch.draw(
            headImage,
            toDisplayX(snake.position.x),
            toDisplayY(snake.position.y),
            GridToDisplayRatioX,
            GridToDisplayRatioY
        )
        for (position in snake.syncTail.toList()) {
            batch.draw(
                bodyImage,
                toDisplayX(position.x),
                toDisplayY(position.y),
                GridToDisplayRatioX,
                GridToDisplayRatioY
            )
        }

        batch.end()

        Gdx.gl.glEnable(GL30.GL_BLEND)
        // draw game state
        val snakePosition = snake.position
        val snakePerception = snake.perception
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        drawRectangle(snakePosition.x, snakePosition.y + 1, toPerceptionColor(snakePerception[0]))
        drawRectangle(snakePosition.x + 1, snakePosition.y, toPerceptionColor(snakePerception[1]))
        drawRectangle(snakePosition.x, snakePosition.y - 1, toPerceptionColor(snakePerception[2]))
        drawRectangle(snakePosition.x -1, snakePosition.y, toPerceptionColor(snakePerception[3]))
        shapeRenderer.end()

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(toDisplayX(0), toDisplayY(0), toDisplayX(Settings.GridWidth - 1), toDisplayY(Settings.GridHeight - 1))
        shapeRenderer.end()

    }

    override fun dispose() {
        batch.dispose()
        appleImage.dispose()
        bodyImage.dispose()
        controllerThread.interrupt()
    }

    private fun toDisplayX(gridX: Int): Float = (gridX + 1) * GridToDisplayRatioX

    private fun toDisplayY(gridY: Int): Float = (gridY + 1) * GridToDisplayRatioY

    private fun drawRectangle(gridX: Int, gridY: Int, color: Color) {

        shapeRenderer.color = color
        shapeRenderer.rect(
            toDisplayX(gridX) + PaddingX, toDisplayY(gridY) + PaddingY,
            GridToDisplayRatioX - 2 * PaddingX, GridToDisplayRatioY - 2 * PaddingY
        )

    }

    private fun toPerceptionColor(perception: Double): Color {
        return if (perception > 0.5)
            GoodColor
        else if (perception < -0.5)
            BadColor
        else
            NeutralColor
    }

    private fun printEvent(event: GameEvent) {
        println("Event: ${event.type}. Award: ${event.award}. Message: ${event.message}. Total score: ${field.score}")
    }

}