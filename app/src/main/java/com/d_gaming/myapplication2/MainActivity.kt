package com.d_gaming.myapplication2

import android.content.res.Resources
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var surfaceView: SurfaceView
    init {
        Utils.init()
    }
    private var isModel1 = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val relativeLayout = RelativeLayout(this)
        val renderer = ModelRenderer()

        surfaceView = SurfaceView(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                2000
            )
        }
        val character = "models/james.glb"
        val shirt = "models/t_shirt.glb"
        renderer.onSurfaceAvailable(surfaceView,lifecycle)
        val button = Button(this).apply {
            text = "Change"
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                addRule(RelativeLayout.CENTER_HORIZONTAL)
                bottomMargin = 16.dpToPx() // Adjust margin as needed
            }

            setOnClickListener {
                isModel1 = !isModel1
                val modelName = if (isModel1) character else shirt

               renderer.createRenewables(modelName)

            }
        }

        // Add the SurfaceView to the RelativeLayout
        relativeLayout.addView(surfaceView)

        // Add the Button to the RelativeLayout
        relativeLayout.addView(button)

        // Set the content view to the RelativeLayout
        setContentView(relativeLayout)


    //    renderer.onSurfaceAvailable(surfaceView,lifecycle)


    }




    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}