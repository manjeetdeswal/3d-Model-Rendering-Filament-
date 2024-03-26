import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Cube {

    private val vertexShaderCode =
        """
        attribute vec4 vPosition;
        uniform mat4 uMVPMatrix;
        varying vec4 vColor;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
            vColor = vPosition * 0.5 + 0.5;
        }
        """.trimIndent()

    private val fragmentShaderCode =
        """
        precision mediump float;
        varying vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
        """.trimIndent()

    private val vertices = floatArrayOf(
        -1.0f, -1.0f, -1.0f,  // 0
        1.0f, -1.0f, -1.0f,   // 1
        1.0f, 1.0f, -1.0f,    // 2
        -1.0f, 1.0f, -1.0f,   // 3
        -1.0f, -1.0f, 1.0f,   // 4
        1.0f, -1.0f, 1.0f,    // 5
        1.0f, 1.0f, 1.0f,     // 6
        -1.0f, 1.0f, 1.0f     // 7
    )

    private val indices = shortArrayOf(
        0, 4, 5, 0, 5, 1,  // Bottom face
        1, 5, 6, 1, 6, 2,  // Front face
        2, 6, 7, 2, 7, 3,  // Top face
        3, 7, 4, 3, 4, 0,  // Back face
        4, 7, 6, 4, 6, 5,  // Right face
        3, 0, 1, 3, 1, 2   // Left face
    )

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertices.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(indices.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(indices)
                position(0)
            }
        }

    private val program: Int

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)
        val positionHandle: Int = GLES20.glGetAttribLocation(program, "vPosition")
        val colorHandle: Int = GLES20.glGetUniformLocation(program, "vColor")
        val mvpMatrixHandle: Int = GLES20.glGetUniformLocation(program, "uMVPMatrix")

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle, 3, GLES20.GL_FLOAT, false,
            3 * 4, vertexBuffer
        )

        GLES20.glUniform4f(colorHandle, 0.5f, 0.5f, 0.5f, 1.0f)
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, indices.size,
            GLES20.GL_UNSIGNED_SHORT, drawListBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}