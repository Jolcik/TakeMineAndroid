package com.pkostrzenski.takemine.models

data class Location(
    val name: String,
    val lat: Double,
    val lng: Double,
    val fromHour: String,
    val toHour: String,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
)