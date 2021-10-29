package com.mrtwon.framex_premium.FragmentTop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mrtwon.framex_premium.Content.RatingEnum
import com.mrtwon.framex_premium.R
import kotlinx.android.synthetic.main.filter_content_layout.view.*

class FilterRatingFragment: BottomSheetDialogFragment() {
    val mainTopVM: MainTopVM by lazy { ViewModelProvider(requireParentFragment()).get(MainTopVM::class.java) }
    lateinit var chipGroupRating: ChipGroup
    lateinit var localFilter: Filter
    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.filter_content_layout, container, false)
        mView.layout_genres.visibility = View.GONE
        mView.divider.visibility = View.GONE
        localFilter = mainTopVM.filterLiveData.value!!.copy()
        chipGroupRating = mView.chip_group_rating
        recoveryStateRating()
        return mView
    }

    private fun recoveryStateRating(){
        val currentStateRating = localFilter.rating.toString()
        for(i in 0 until chipGroupRating.childCount){
            val chip = chipGroupRating.getChildAt(i) as Chip
            if(chip.text == currentStateRating) chip.isChecked = true
        }
    }


    private fun saveStateRating(){
        val chip = mView.findViewById<Chip>(chipGroupRating.checkedChipId)
        when(chip.text){
            RatingEnum.Imdb.toString() -> localFilter.rating = RatingEnum.Imdb
            RatingEnum.Kinopoisk.toString() -> localFilter.rating = RatingEnum.Kinopoisk
        }
    }
    override fun onDestroyView() {
        saveStateRating()
        log("local: $localFilter |  in vm: ${mainTopVM.filterLiveData.value}")
        if(mainTopVM.filterLiveData.value!!.equals(localFilter)){
            log("equals")
        }else{
            log("not equals")
            mainTopVM.filterLiveData.postValue(localFilter)
        }
        log("onDestroyView()")
        super.onDestroyView()
    }

    fun log(s: String){
        Log.i("self-filter", s)
    }
}