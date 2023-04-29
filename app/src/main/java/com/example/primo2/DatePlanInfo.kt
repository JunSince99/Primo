package com.example.primo2


data class DatePlanInfo(
    val dateTitle:String = "",
    val dateStartDate:String = "",
    val dateEndDate:String = "",
    val course:MutableList<String> = arrayListOf(),
    val specialDay:Int = 0
)



