package com.d_gaming.myapplication2

import Cube
import android.content.Context
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.view.MotionEvent
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private lateinit var cube: Cube
    private var angleX: Float = 0f
    private var angleY: Float = 0f
    private val mvpMatrix = FloatArray(16)


    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.setIdentityM(mvpMatrix, 0)
        Matrix.rotateM(mvpMatrix, 0, angleX, 1f, 0f, 0f)
        Matrix.rotateM(mvpMatrix, 0, angleY, 0f, 1f, 0f)
        cube.draw(mvpMatrix)
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        cube = Cube()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
    fun rotateModel(dx: Float, dy: Float) {
        angleX += dx * 0.5f
        angleY += dy * 0.5f
    }
    fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                angleX += event.y * 0.5f
                angleY += event.x * 0.5f
            }
        }
    }
}