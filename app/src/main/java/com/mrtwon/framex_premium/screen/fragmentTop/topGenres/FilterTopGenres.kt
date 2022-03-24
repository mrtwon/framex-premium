import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.screen.fragmentTop.MainTopVM
import com.mrtwon.framex_premium.screen.fragmentTop.topGenres.TopGenresVM
import kotlinx.android.synthetic.main.filter_content_layout.view.*

class FilterTopGenres: BottomSheetDialogFragment() {
    private val mainTopVM: MainTopVM by lazy {
            ViewModelProvider(requireParentFragment()).get(MainTopVM::class.java)
    }
    private lateinit var chipGroupRating: ChipGroup
    private lateinit var chipGroupGenres: ChipGroup
    private lateinit var mView: View
    private lateinit var currentFilter: TopGenresVM.Filter
    private lateinit var originalFilter: TopGenresVM.Filter

    override fun onCreate(savedInstanceState: Bundle?) {
        currentFilter = mainTopVM.filterGenresLD.value!!.copy()
        originalFilter = mainTopVM.filterGenresLD.value!!
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.filter_content_layout, container, false)
        chipGroupGenres = mView.chip_group_genres
        chipGroupRating = mView.chip_group_rating
        recoveryStateRating()
        mView.findViewById<LinearLayout>(R.id.divider).visibility = View.GONE
        mView.findViewById<LinearLayout>(R.id.layout_genres).visibility = View.GONE
        return mView
    }

    private fun recoveryStateRating(){
        val currentStateRating = currentFilter.sortBy._name
        for(i in 0 until chipGroupRating.childCount){
            val chip = chipGroupRating.getChildAt(i) as Chip
            if(chip.text == currentStateRating) chip.isChecked = true
        }
    }

    private fun saveStateRating(){
        val chip = mView.findViewById<Chip>(chipGroupRating.checkedChipId)
        currentFilter.sortBy = RatingEnum.getEnum(chip.text.toString())
    }

    override fun onDestroyView() {
        saveStateRating()
        if(originalFilter != currentFilter)
            mainTopVM.setFilterGenres(currentFilter)
        super.onDestroyView()
    }


    fun log(s: String){
        Log.i("self-filter", s)
    }
}