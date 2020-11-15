package com.levkorol.todo.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.levkorol.todo.R

@Entity
data class Targets(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var description: String,
    var date: Long,
    var targetDone: Boolean,
    var startData: Long,
    var stopData: Long,
    var inArchive: Boolean,
    var background: BackgroundTarget,
    var image: Int,
    var days: Int,
    var time: Int
) {

    constructor() : this(1,"","",1,false,1,1,false,BackgroundTarget.PURPLE,1,1,11)

    enum class BackgroundTarget(
        val value: Int,
        @DrawableRes val res: Int
    ) {
        PURPLE(0, R.color.color_grey_dark),
        WHITE(1, R.color.white),
        DARKER_BLU(2,R.color.colorSelect),
        ORANGE(3,R.color.Orange),
        KIWI(4,R.color.kiwi),
        LIGHT_BLU(5,R.color.light_blu),
        PINK_LIGHT(6,R.color.pink_light)
    }

}

