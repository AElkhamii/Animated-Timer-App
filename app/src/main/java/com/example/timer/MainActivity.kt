package com.example.timer


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timer.ui.theme.TimerTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        color = Color(0xFF101010),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ){
                            Timer(
                                totalTime = 100L * 1000L,
                                handelColor = Color.Green,
                                inactiveBarColor = Color.DarkGray,
                                activeBarColor = Color(0xFF37B900),
                                modifier = Modifier.size(300.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    totalTime: Long,
    handelColor: Color,
    inactiveBarColor:Color,
    activeBarColor:Color,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
    ){
    /* When we created a box and our timer will be located in,
     * then we can use modifier function of that box called on size changed
     * which will be called when the size of our composable changes */
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var percentageTimerValue by remember {
        mutableStateOf(initialValue)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning){
            delay(1000L)
            currentTime -= 1000L
            percentageTimerValue = currentTime / totalTime.toFloat()
        }
    }
    
    Box(
        // Center the timer
        contentAlignment = Alignment.Center,
        // to get the size of the box and set that size in state variable called size
        modifier = modifier.onSizeChanged {
            size = it
        }
    ){
        //Arc for  non-active bar
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        //Arc for visible active bar
        Canvas(modifier = modifier) {
            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * percentageTimerValue,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            // Center position of the Box
            val center = Offset(size.width/2f,size.height/2f)
            // Get the angle of the Node attached to the arc bar and after that convert this degree value to radiant
            val beta = (250f * percentageTimerValue + 145f) * (PI/180f).toFloat()
            val radius = size.width/2f
            val aSide = cos(beta) * radius
            val bSide = sin(beta) * radius

            drawPoints(
                listOf(Offset(center.x + aSide, center.y + bSide)),
                pointMode = PointMode.Points,
                color = handelColor,
                strokeWidth = (strokeWidth * 3f).toPx(), cap = StrokeCap.Round
            )
        }
        Text(
            text = (currentTime/1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Button(
            onClick = {
                      if(currentTime <= 0){
                          currentTime = totalTime
                          isTimerRunning = true
                        }else{
                            isTimerRunning =!isTimerRunning
                        }
                      },
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(!isTimerRunning || currentTime <= 0L){
                    Color.Green
                }else{
                    Color.Red
                }
            )
        ) {
            Text(
                text = if (isTimerRunning && currentTime >= 0L) "Stop"
                else if(!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }

}

//@Preview(showBackground = true,showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    TimerTheme {
//        java.util.
//    }
//}