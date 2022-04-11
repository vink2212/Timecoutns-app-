ackage com.example.timecounts

import android.animation.Animator
import android.content.pm.ActivityInfo
import android.media.Image
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import org.w3c.dom.Text
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//Finds the sum of digits of  a number
        fun sumOfDigits(a: Int) : Int {
            var sum = 0
            var n = a
            while (n > 0) {
                sum += (n % 10).toInt()
                n  = (n/10).toInt()
                 }

            return sum
        }

//tests if the digts are the same
        fun sameDigits(c: Int) : Boolean{
            var c1 = c/10
            var c2 = c%10
            return c2 - c1 == 0
        }

//Tests if the digits are consecutive
        fun consecutiveDigits(b: Int) : Boolean{
            var n = b / 10
            var n2 = b %10
            return abs(n2-n) == 1
        }
//Fade up animation
        var fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.float_up)


//Gives point value for times
        fun pointValue(timea: Int, timeb: Int, timec: Int, obj : TextView) : Int {
            var score = 0

            fadeAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    obj.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(p0: Animation?) {
                    obj.visibility = View.INVISIBLE
                }

                override fun onAnimationRepeat(p0: Animation?) {

                }
            })

            if ((timea % timeb == 0) || (timeb % timea == 0) || (timec % timeb == 0) || (timeb % timec ==0)){
                score += 5
                }
            if ((timea == 4 || timec == 16) && (timeb == 20)){
                score += 69

            }

            if ((sameDigits(timea) && sameDigits( timeb)) || ((sameDigits(timec) && sameDigits( timeb)))){
                score += 6
            }


            if (((timec % 2 == 0) && (timeb %2 == 0)) || ((timea % 2 == 0) && (timeb %2 == 0))){
                score += 1
            }

            if ((timec % timeb == 0 )||(timea % timeb == 0)){
                score += 2
            }

            if ((timea == 11) && (timeb == 11)){
                score += 11
            }
            if ((timec == 17) && (timeb == 38))
            {
                score +=12
            }
            if((sumOfDigits(timea) == sumOfDigits(timeb)) || (sumOfDigits(timec) == sumOfDigits((timeb))))
            {
                score += 4
            }
            if((abs(sumOfDigits(timea)- sumOfDigits(timeb)) == 1) || (abs(sumOfDigits(timec) - sumOfDigits(timeb))== 1)){
                score += 5
            }

            if((consecutiveDigits(timea) && (consecutiveDigits(timeb))) || (consecutiveDigits(timec) && (consecutiveDigits(timeb)))){
                score += 20
            }

            if (score != 0)
            {
                obj.setText("+${score}")

                obj.startAnimation(fadeAnimation)
            }


            return score
        }



// initialises all the objects seen on screen
        val mediaPlayer1 = MediaPlayer.create(this, R.raw.popding)
        val mediaPLayer2 = MediaPlayer.create(this, R.raw.thud)
        val pointsView = findViewById<TextView>(R.id.points)
        val timeView = findViewById<TextView>(R.id.time)
        val timeButton = findViewById<ImageButton>(R.id.button)
        val messageView = findViewById<TextView>(R.id.message)
        val highScoreView = findViewById<TextView>(R.id.highScore)

        val sharedPreferences =getPreferences(MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var highScore = sharedPreferences.getString("highScore", "0")
        highScoreView.setText(highScore)
        if(highScore.isNullOrEmpty()){
            highScore = "0"
        }
// Checks last time and resets points score if data changes
        var lastTime = sharedPreferences.getString("lastTime", "00:00")
        var lastDate = sharedPreferences.getString("lastDate", "1")
        var time1 : Calendar = Calendar.getInstance()
        var date = time1.get(Calendar.DATE)
        if(lastDate.isNullOrEmpty()){
            lastDate = "1"
        }
        if(lastDate.toInt() != date){
            editor.putString("points", "0")
            editor.putString("lastDate", "${date}")
            lastDate = date.toString()
            editor.commit()

        }
        var point =sharedPreferences.getString("points", "0")
        if (point.isNullOrEmpty())
        {
            point = "0"
        }
        pointsView.text = point
        timeView.text = lastTime


//Runs code when the button is pressed.
        timeButton.setOnClickListener{
//            Log.d("points", "${point}")
//            Log.d("lastDate", lastDate)
//            Log.d("date", date.toString())



            var time : Calendar = Calendar.getInstance()


            var timeA = time.get(Calendar.HOUR)
            var timeB = time.get(Calendar.MINUTE)
            var timeC = time.get(Calendar.HOUR_OF_DAY)
            var date = time.get(Calendar.DATE)


            var timeText = "${time.get(Calendar.HOUR_OF_DAY)}:"+"${time.get(Calendar.MINUTE)}"

            if (timeB < 10){
                timeText = "${time.get(Calendar.HOUR_OF_DAY)}:"+"0${time.get(Calendar.MINUTE)}"
            }



            if(timeText != lastTime){
//                Log.d("timeText", timeText)
//                Log.d("lastTime", "${lastTime}")
                point = ((((point!!.toInt()) +  pointValue(timeA, timeB, timeC, messageView)).toString()))
                mediaPlayer1.start()

            }
            else{
                mediaPLayer2.start()
            }

            timeView.setText(timeText)
            editor.putString("lastTime", timeText)
            editor.putString("points", point);
            editor.commit();
            lastTime = sharedPreferences.getString("lastTime", "00:00")

            pointsView.setText("${point}")
            Log.d("point", "${point}")
            Log.d("highScore", "${highScore}")
            if ("${point}".toInt() > "${highScore}".toInt()){
                highScore = point
                editor.putString("highScore", highScore)
                editor.commit()
                highScoreView.setText(highScore)

            }



        }












    }
}
