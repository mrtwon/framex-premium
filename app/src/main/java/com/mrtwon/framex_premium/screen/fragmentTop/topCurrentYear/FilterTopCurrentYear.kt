package com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.screen.fragmentTop.MainTopVM
import kotlinx.android.synthetic.main.filter_content_layout.view.*

class FilterTopCurrentYear: BottomSheetDialogFragment() {

    private val mainTopVM: MainTopVM by lazy {ViewModelProvider(requireParentFragment()).get(MainTopVM::class.java) }
    private lateinit var chipGroupRating: ChipGroup
    private lateinit var chipGroupGenres: ChipGroup
    private lateinit var mView: View
    private lateinit var currentFilter: TopCurrentYearVM.Filter
    private lateinit var originalFilter: TopCurrentYearVM.Filter
    private lateinit var filterCallback: FilterCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        originalFilter = mainTopVM.filterCurrentYearLD.value!!
        currentFilter = mainTopVM.filterCurrentYearLD.value!!.copy()
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.filter_content_layout, container, false)
        chipGroupGenres = mView.chip_group_genres
        chipGroupRating = mView.chip_group_rating
        recoveryStateGenres()
        recoveryStateRating()
        return mView
    }

    private fun recoveryStateRating(){
        val currentStateRating = currentFilter.sortBy._name
        for(i in 0 until chipGroupRating.childCount){
            val chip = chipGroupRating.getChildAt(i) as Chip
            if(chip.text == currentStateRating) chip.isChecked = true
        }
    }

    private fun recoveryStateGenres(){
        val currentStateGenres = currentFilter.genres._name
        for(i in 0 until chipGroupGenres.childCount){
            val chip = chipGroupGenres.getChildAt(i) as Chip
            if(chip.text == currentStateGenres) chip.isChecked = true
        }
    }


    private fun saveStateRating(){
        val chip = mView.findViewById<Chip>(chipGroupRating.checkedChipId)
        Log.i("self-debug","text rating - ${chip.text}")
        currentFilter.sortBy = RatingEnum.getEnum(chip.text.toString())
    }
    private fun saveStateGenres(){
        val chip = mView.findViewById<Chip>(chipGroupGenres.checkedChipId)
        currentFilter.genres = GenresEnum.getEnum(chip?.text?.toString())
       /* if(chip == null){
            Log.i("self-debug-filter", "chip is null")
            currentFilter.genres = GenresEnu
        }else{
            currentFilter.genres = GenresEnum.getEnum(chip.text.toString())
        }*/
    }

    override fun onDestroyView() {
        saveStateGenres()
        saveStateRating()
        if(originalFilter != currentFilter){
            Log.i("self-debug","new Filter Data (${currentFilter.genres}, ${currentFilter.sortBy})")
            mainTopVM.setFilterCurrentYear(currentFilter)
        }
        super.onDestroyView()
    }


    fun log(s: String){
        Log.i("self-filter", s)
    }
}