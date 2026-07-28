package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RainHighRisk
import com.example.ui.theme.RainLowRisk
import com.example.ui.theme.RainMedRisk
import com.example.ui.theme.SkyPrimary

data class CloudGuideItem(
    val name: String,
    val latinName: String,
    val rainRisk: String,
    val riskColor: Color,
    val featureDescription: String,
    val rainIconTip: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudGuideSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val guideItems = listOf(
        CloudGuideItem(
            name = "积雨云",
            latinName = "Cumulonimbus",
            rainRisk = "极大 (80% - 100%)",
            riskColor = RainHighRisk,
            featureDescription = "庞大高耸的铁砧状黑云，顶部平坦，底部黑沉沉低压，常伴随雷电。",
            rainIconTip = "15-30分钟内必有阵雨或暴雨，请立即归避。"
        ),
        CloudGuideItem(
            name = "雨层云",
            latinName = "Nimbostratus",
            rainRisk = "高 (70% - 90%)",
            riskColor = RainHighRisk,
            featureDescription = "灰暗无光、连续覆盖整个天空的低厚云层，看不清太阳位置。",
            rainIconTip = "持续性降雨（细雨或中雨），将连绵不绝降下。"
        ),
        CloudGuideItem(
            name = "高层云 / 层云",
            latinName = "Altostratus",
            rainRisk = "中等 (40% - 60%)",
            riskColor = RainMedRisk,
            featureDescription = "灰白色或浅灰色幕状云，太阳如同隔着毛玻璃看一般。",
            rainIconTip = "云层若逐渐加厚变暗，约1-2小时内开始下雨。"
        ),
        CloudGuideItem(
            name = "卷积云 / 高积云",
            latinName = "Altocumulus",
            rainRisk = "较低 (20% - 35%)",
            riskColor = RainLowRisk,
            featureDescription = "鱼鳞状或瓦片状的小云块排成行，俗称“鱼鳞天”。",
            rainIconTip = "所谓“鱼鳞天，不雨也风颠”，预示天气可能在24小时内变化。"
        ),
        CloudGuideItem(
            name = "淡积云 / 卷云",
            latinName = "Cumulus / Cirrus",
            rainRisk = "极大可能无雨 (0% - 15%)",
            riskColor = RainLowRisk,
            featureDescription = "像棉花朵朵或羽毛丝缕悬挂在明亮蓝天之中，边缘清晰洁白。",
            rainIconTip = "天气稳定晴朗，适合户外出行与晾晒。"
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = null,
                    tint = SkyPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "云层识雨科普指南",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "关闭")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(guideItems) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${item.latinName})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    shape = CircleShape,
                                    color = item.riskColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = item.rainRisk,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = item.riskColor,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "外观特征：" + item.featureDescription,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "雨趋势：" + item.rainIconTip,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = SkyPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
