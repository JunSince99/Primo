package com.example.primo2

val datePlanList:MutableList<DatePlanInfo> = mutableListOf()

data class DatePlanInfo(
    val dateTitle:String = "",
    val dateStartDate:String ="",
    val dateEndDate:String = "",
    val course:Map<Int,String> = mapOf()
)



