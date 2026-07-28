package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

data class SampleSkyItem(
    val id: String,
    val title: String,
    val description: String,
    val cloudCategory: String,
    val uri: Uri
)

object SampleSkyData {

    fun getSampleSkyItems(context: Context): List<SampleSkyItem> {
        val stormUri = createSampleSkyImage(
            context,
            "sample_storm.jpg",
            bgColorTop = Color.rgb(20, 25, 35),
            bgColorBottom = Color.rgb(45, 55, 70),
            cloudColor = Color.rgb(30, 35, 45),
            hasRainShaft = true
        )

        val overcastUri = createSampleSkyImage(
            context,
            "sample_overcast.jpg",
            bgColorTop = Color.rgb(120, 130, 140),
            bgColorBottom = Color.rgb(180, 190, 200),
            cloudColor = Color.rgb(140, 150, 160),
            hasRainShaft = false
        )

        val sunnyUri = createSampleSkyImage(
            context,
            "sample_sunny.jpg",
            bgColorTop = Color.rgb(14, 165, 233),
            bgColorBottom = Color.rgb(125, 211, 252),
            cloudColor = Color.rgb(250, 250, 250),
            hasRainShaft = false
        )

        val sunsetUri = createSampleSkyImage(
            context,
            "sample_sunset.jpg",
            bgColorTop = Color.rgb(249, 115, 22),
            bgColorBottom = Color.rgb(236, 72, 153),
            cloudColor = Color.rgb(253, 186, 116),
            hasRainShaft = false
        )

        return listOf(
            SampleSkyItem(
                id = "storm",
                title = "雷暴乌云 (强降雨压顶)",
                description = "浓黑重压的积雨云，局地伴随阵雨",
                cloudCategory = "积雨云 Cumulonimbus",
                uri = stormUri
            ),
            SampleSkyItem(
                id = "overcast",
                title = "阴天高层云 (近期可能有雨)",
                description = "灰暗均匀的阴云，透光度低",
                cloudCategory = "雨层云 / 高层云 Altostratus",
                uri = overcastUri
            ),
            SampleSkyItem(
                id = "sunny",
                title = "晴空朵朵 (无雨宜人)",
                description = "湛蓝天空伴随絮状淡积云",
                cloudCategory = "淡积云 Fair-weather Cumulus",
                uri = sunnyUri
            ),
            SampleSkyItem(
                id = "sunset",
                title = "晚霞霞光 (天晴干爽)",
                description = "夕阳西下的高空卷云",
                cloudCategory = "卷云 Cirrus",
                uri = sunsetUri
            )
        )
    }

    private fun createSampleSkyImage(
        context: Context,
        fileName: String,
        bgColorTop: Int,
        bgColorBottom: Int,
        cloudColor: Int,
        hasRainShaft: Boolean
    ): Uri {
        val file = File(context.cacheDir, fileName)
        if (!file.exists()) {
            val width = 800
            val height = 600
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Draw Sky Gradient
            val bgPaint = Paint().apply {
                shader = LinearGradient(
                    0f, 0f, 0f, height.toFloat(),
                    bgColorTop, bgColorBottom, Shader.TileMode.CLAMP
                )
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

            // Draw Clouds
            val cloudPaint = Paint().apply {
                color = cloudColor
                isAntiAlias = true
            }

            // Cloud 1
            canvas.drawCircle(250f, 220f, 130f, cloudPaint)
            canvas.drawCircle(380f, 180f, 160f, cloudPaint)
            canvas.drawCircle(520f, 220f, 120f, cloudPaint)
            canvas.drawRect(180f, 220f, 600f, 320f, cloudPaint)

            // Secondary cloud layer
            val secondCloudPaint = Paint().apply {
                color = Color.argb(
                    180,
                    Color.red(cloudColor),
                    Color.green(cloudColor),
                    Color.blue(cloudColor)
                )
                isAntiAlias = true
            }
            canvas.drawCircle(150f, 350f, 110f, secondCloudPaint)
            canvas.drawCircle(650f, 320f, 140f, secondCloudPaint)
            canvas.drawRect(100f, 350f, 720f, 440f, secondCloudPaint)

            // Draw Rain Shafts if storm
            if (hasRainShaft) {
                val rainPaint = Paint().apply {
                    color = Color.argb(120, 180, 210, 240)
                    strokeWidth = 3f
                    isAntiAlias = true
                }
                for (i in 0..60) {
                    val rx = 200f + (i * 8f)
                    val ry = 360f + (i % 5 * 10f)
                    canvas.drawLine(rx, ry, rx - 15f, ry + 120f, rainPaint)
                }
            }

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
        }

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}
