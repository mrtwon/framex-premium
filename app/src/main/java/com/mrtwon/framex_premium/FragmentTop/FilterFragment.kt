package com.mrtwon.framex_premium.FragmentTop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.RatingEnum
import com.mrtwon.framex_premium.R
import kotlinx.android.synthetic.main.filter_content_layout.view.*

class FilterFragment: BottomSheetDialogFragment() {

    val mainTopVM: MainTopVM by lazy {ViewModelProvider(requireParentFragment()).get(MainTopVM::class.java) }
    lateinit var chipGroupRating: ChipGroup
    lateinit var chipGroupGenres: ChipGroup
    lateinit var localFilter: Filter
    lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.filter_content_layout, container, false)
        localFilter = mainTopVM.filterLiveData.value!!.copy()
        chipGroupGenres = mView.chip_group_genres
        chipGroupRating = mView.chip_group_rating
        //listenerCheckedChanged()
        //localFilter.rating = RatingEnum.Imdb
        recoveryStateGenres()
        recoveryStateRating()
        return mView
    }

    fun printChild(){
        for(i in 0 until chipGroupRating.childCount){
            val chip = chipGroupRating.getChildAt(i) as Chip
            log("chip text: ${chip.text}")
        }
    }

    fun recoveryStateRating(){
        val currentStateRating = localFilter.rating.toString()
        for(i in 0 until chipGroupRating.childCount){
            val chip = chipGroupRating.getChildAt(i) as Chip
            if(chip.text == currentStateRating) chip.isChecked = true
        }
    }

    fun recoveryStateGenres(){
        val currentStateGenres = localFilter.genres?.toString() ?: return
        for(i in 0 until chipGroupGenres.childCount){
            val chip = chipGroupGenres.getChildAt(i) as Chip
            if(chip.text == currentStateGenres) chip.isChecked = true
        }
    }

    fun saveStateRating(){
        val chip = mView.findViewById<Chip>(chipGroupRating.checkedChipId)
        when(chip.text){
            RatingEnum.Imdb.toString() -> localFilter.rating = RatingEnum.Imdb
            RatingEnum.Kinopoisk.toString() -> localFilter.rating = RatingEnum.Kinopoisk
        }
    }
    fun saveStateGenres(){
        val chip = mView.findViewById<Chip>(chipGroupGenres.checkedChipId)
        if(chip == null){
            localFilter.genres = null
            return
        }
        for(elem in GenresEnum.values()){
            if(elem.toString() == chip.text){
                localFilter.genres = elem
                break
            }
        }
    }

    override fun onDestroyView() {
        saveStateGenres()
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

    override fun onDestroy() {
        log("onDestroy()")
        super.onDestroy()
    }


    fun listenerCheckedChanged(){
        chipGroupRating.setOnCheckedChangeListener { group, checkedId ->
            log("checkedId: $checkedId | imdb ${R.id.imdb_chip} kp ${R.id.kp_chip}")
        }
        chipGroupGenres.setOnCheckedChangeListener{ group, checkedId ->
            log("other")
        }
    }
    fun log(s: String){
        Log.i("self-filter", s)
    }
}