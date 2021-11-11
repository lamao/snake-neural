package com.mygdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.mygdx.game.MyGdxGame
import com.mygdx.game.Settings
import com.mygdx.game.model.Field
import com.mygdx.network.LearningThread

/**
 * Created by vyacheslav.mischeryakov
 * Created 21.10.2021
 */
fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.title = "Drop"
    config.width = Settings.Width
    config.height = Settings.Height

    val field = Field(Settings.GridWidth, Settings.GridHeight)
    field.reset()

    val applicationAdapter = MyGdxGame(field) {
        LearningThread(field).run()
    }
    LwjglApplication(applicationAdapter, config)


}