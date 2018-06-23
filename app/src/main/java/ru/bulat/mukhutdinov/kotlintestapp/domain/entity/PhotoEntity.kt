package ru.bulat.mukhutdinov.kotlintestapp.domain.entity

import ru.bulat.mukhutdinov.kotlintestapp.data.dto.PhotoDto

class PhotoEntity(val id: Long,
                  val owner: String,
                  val secret: String,
                  val server: Int,
                  val farm: Int,
                  val title: String,
                  val isPublic: Int,
                  val isFriend: Int,
                  val isFamily: Int) {


    constructor(dto: PhotoDto) : this(dto.id,
            dto.owner,
            dto.secret,
            dto.server,
            dto.farm,
            dto.title,
            dto.isPublic,
            dto.isFriend,
            dto.isFamily)

    val url = "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg"
}
