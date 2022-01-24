package com.example.weatherapp.view.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.business.model.GeoCodeModel
import com.example.weatherapp.databinding.ItemCityListBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.util.*

class CityListAdapter: BaseAdapter<GeoCodeModel>() {

    lateinit var binding: ItemCityListBinding
    lateinit var clickListener: SearchItemClickListener




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    binding = ItemCityListBinding.inflate(inflater, parent, false)

        return CityListViewHolder(binding)
    }



    inner class CityListViewHolder(binding: ItemCityListBinding): BaseAdapter.BaseViewHolder(binding.root) {
    val mCity: MaterialTextView = binding.searchCity
    val mCountry: MaterialTextView = binding.searchCountry
    val mState: MaterialTextView = binding.state
    val mFavorite: MaterialButton = binding.favorite
    val mLocation: MaterialCardView = binding.location




    override fun bindView(position: Int) {
        mLocation.setOnClickListener {
            clickListener.showWeatherIn(mData[position])
        }

        mFavorite.setOnClickListener {
            val item = mData[position]
            when((it as MaterialButton).isChecked){
                true ->{
                    item.isFavorite = true
                    clickListener.addToFavorite(item)
                }
                false ->{
                    item.isFavorite = false
                    clickListener.removeFromFavorites(item)
                }
            }
        }
        mData[position].apply {
            mState.text = if(!state.isNullOrEmpty()) binding.root.context.getString(R.string.comma, state) else ""
            mCity.text = when (Locale.getDefault().displayLanguage) {
                "русский" -> local_names.ru?: name
                "English" -> local_names.en?: name
                else -> "non"                }
            mCountry.text = Locale("", country).displayName
            mFavorite.isChecked = isFavorite
        }
    }


    }


    interface SearchItemClickListener{
        fun addToFavorite(item: GeoCodeModel)

        fun removeFromFavorites(item: GeoCodeModel)

        fun showWeatherIn(item: GeoCodeModel)
    }

}