package com.mrtwon.framex_premium.data.networkDataSource.firebaseSource

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

data class WrapperResponse(val task: Task<QuerySnapshot>, val fromCache: Boolean)