package com.example.primo2.adapter

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.primo2.PostInfo
import com.example.primo2.R

class PostAdapter (private val activity: Activity, private val dataSet: ArrayList<PostInfo>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val postLayout: View

        init {
            postLayout = view.findViewById(R.id.postLayout)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_post, viewGroup, false)
        val viewHolder:ViewHolder = ViewHolder(view)
        viewHolder.postLayout.setOnClickListener{
           // selectProfile(dataSet[viewHolder.adapterPosition].title)
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        //val imageView:ImageView = viewHolder.cardView.findViewById(R.id.profileimageView)

        //Glide.with(this.activity).load(dataSet[position]).centerCrop().override(500).into(imageView);
        val postTitle: TextView = viewHolder.postLayout.findViewById(R.id.postTitle)
        postTitle.text = dataSet[position].title

        val postImage: ImageView = viewHolder.postLayout.findViewById<ImageView>(R.id.postImage)
        Glide.with(this.activity).load(dataSet[position].Contents[0]).centerCrop().override(500).into(postImage);
        Log.e("에러",""+dataSet[position].Contents)
        val postText: TextView = viewHolder.postLayout.findViewById<TextView>(R.id.postText)
        postText.text = dataSet[position].Comments

        val profileName: TextView = viewHolder.postLayout.findViewById<TextView>(R.id.profileName)
        profileName.text = "이름 : " + dataSet[position].Writer
    }


    override fun getItemCount() = dataSet.size
    private fun selectProfile(dataSet: String){
        val resultIntent = Intent()
        resultIntent.putExtra("profilePath", dataSet)
        this.activity.setResult(Activity.RESULT_OK, resultIntent)
        this.activity.finish()
    }
}