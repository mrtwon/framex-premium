package com.mrtwon.framex_premium.Helper

import android.icu.math.BigDecimal
import com.mrtwon.framex_premium.Content.ContentTypeEnum

class HelperFunction {
    /*
    Helper function for work with data
     */
    companion object{

        //round rating and return String result
        fun roundRating(rating: Double?): String{
            if (rating == null) return "0.0"
            return BigDecimal(rating.toString()).setScale(1, BigDecimal.ROUND_DOWN).toString()
        }

        //round rating and return Double result
        fun helperConvertDouble(rating: Double?): Double{
            if (rating == null) return 0.0
            return BigDecimal(rating.toString()).setScale(1, BigDecimal.ROUND_DOWN).toDouble()
        }

        fun buildDeepLink(contentTypeEnum: ContentTypeEnum, id: Int): String?{
            val contentResult = when(contentTypeEnum){
                ContentTypeEnum.SERIAL ->{
                    "s"
                }
                ContentTypeEnum.MOVIE -> {
                    "m"
                }
            }
            return "http://framex.application/$contentResult/$id"
        }
    }

}