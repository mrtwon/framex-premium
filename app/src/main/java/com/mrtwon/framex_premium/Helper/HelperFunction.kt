package com.mrtwon.framex_premium.Helper

import android.content.res.Resources
import android.util.Log
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import java.util.*

class HelperFunction {
    /*
    Helper function for work with data
     */
    companion object{

        //round rating and return String result
        fun roundRating(rating: String?): String{
            if(rating == null) return "0.0"
            return if(rating.length > 3) rating.substring(0, 3) else rating
        }

        fun getSecondTime(): Int{
            return (Date().time / 1000).toInt()
        }
        fun getYear(): Int {
            val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            calendar.time = Date()
            return calendar.get(Calendar.YEAR)
        }

        //round rating and return Double result
       /*fun helperConvertDouble(rating: Double?): Double{
            if (rating == null) return 0.0
            return BigDecimal(rating.toString()).setScale(1, BigDecimal.ROUND_DOWN).toDouble()
        }*/


        // 350 dp - рейтинги по центру нужны.
        // до 500dp история просмотров должна быть match parents
        fun buildDeepLink(contentEnum: ContentEnum, id: Int): String?{
            val contentResult = when(contentEnum){
                ContentEnum.Serial -> "s"
                ContentEnum.Movie -> "m"
                else -> "undefined"
            }
            return "http://framex.application/$contentResult/$id"
        }


        // substring title with count chars > 27
        fun substringTitle(str: String): String{
            if(str.length > 27){
                return str.substring(0, 27) + "..."
            }
            return str
        }

        fun cutNotificationTitle(title: String): String{
            val metrics = Resources.getSystem().displayMetrics
            val widthDp = (metrics.widthPixels / metrics.density).toInt()
            log(widthDp.toString())
            if(widthDp in 300..350 && title.length > 24) return title.substring(0, 24) + "..."
            if(widthDp >= 350 && title.length > 27) return title.substring(0, 24) + "..."
            return title
        }

        fun cutTopTitle(title: String): String{
            return if(title.length > 30) title.substring(0, 30)+"..." else title
        }

        fun log(s: String){
            Log.i("self-help-function", s)
        }
    }

}