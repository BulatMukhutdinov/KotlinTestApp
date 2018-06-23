package ru.bulat.mukhutdinov.kotlintestapp.presentation.ui.photofeed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.bulat.mukhutdinov.kotlintestapp.R
import ru.bulat.mukhutdinov.kotlintestapp.presentation.bindable.PhotoBindable
import ru.bulat.mukhutdinov.kotlintestapp.presentation.util.OnPhotoClickListener


class PhotoViewHolder(itemView: View, private val clickListener: OnPhotoClickListener)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    val image: ImageView = itemView.findViewById(R.id.photo)

    private lateinit var photo: PhotoBindable

    init {
        itemView.setOnClickListener(this)
    }

    companion object {
        fun create(parent: ViewGroup, listener: OnPhotoClickListener): PhotoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.photo_item, parent, false)
            return PhotoViewHolder(view, listener)
        }
    }

    fun bindTo(entity: PhotoBindable) {
        photo = entity

        Picasso.get()
                .load(entity.url)
                .placeholder(R.color.background)
                .into(image)
    }

    override fun onClick(v: View) {
        clickListener.onClick(photo, image)
    }
}