package com.example.primo2.adapter

import com.example.primo2.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.example.primo2.placeList
import com.example.primo2.screen.informationPlace
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter


class placeAdapter(@NonNull context: Context) :
    DefaultViewAdapter(context) {
    private val mContext: Context

    init {
        mContext = context
    }

    @NonNull
    override fun getContentView(@NonNull infoWindow: InfoWindow): View {
        val view: View =View.inflate(mContext, R.layout.information_place, null)
        val placePicture: ImageView = view.findViewById(R.id.placePicture) as ImageView
        val txtTitle = view.findViewById(R.id.title) as TextView
        val txtInfoWindow = view.findViewById(R.id.information) as TextView
        val num:Int = infoWindow.marker?.tag.toString().toInt()
        txtTitle.text = placeList[num].name
        txtInfoWindow.text = placeList[num].information
        placePicture.setImageResource(placeList[num].imageResource)
        return view
    }
}