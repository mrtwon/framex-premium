package com.mrtwon.framex_premium.domain.entity

import com.mrtwon.framex_premium.data.entity.RecentDao
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum

data class Recent(
    var id: Int = 0,
    var idContent: Int,
    var contentType: ContentEnum,
    var posterPreview: String? = null,
    var time: Int
){
    companion object{
        fun fromRecentDao(recentDao: RecentDao?): Recent?{
            if(recentDao == null) return null
            return Recent(
                id = recentDao.id,
                idContent = recentDao.contentId,
                contentType = ContentEnum.getEnum(recentDao.contentType),
                posterPreview = recentDao.posterPreview,
                time = recentDao.time
            )
        }
    }
}