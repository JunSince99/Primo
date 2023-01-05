package com.example.primo2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.primo2.R

class GalleryAdapter (private val activity: Activity, private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val cardView: CardView

        init {
            cardView = view.findViewById(R.id.galleryCardView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_gallery, viewGroup, false)
        val viewHolder:ViewHolder = ViewHolder(view)
        viewHolder.cardView.setOnClickListener{
            selectProfile(dataSet[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val imageView:ImageView = viewHolder.cardView.findViewById(R.id.profileimageView)

        Glide.with(this.activity).load(dataSet[position]).centerCrop().override(500).into(imageView);
        // 이미지 리사이징 관련 라이브러리 Glide를 씀, 관련 가이드  https://bumptech.github.io/glide/doc/download-setup.html
    }


    override fun getItemCount() = dataSet.size
    private fun selectProfile(dataSet: String){
        val resultIntent = Intent()
        resultIntent.putExtra("profilePath", dataSet)
        this.activity.setResult(Activity.RESULT_OK, resultIntent)
        this.activity.finish()
    }
}