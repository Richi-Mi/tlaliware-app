package com.richi_mc.tlaliwareapp.ui

import kotlinx.serialization.Serializable

@Serializable
object Main

@Serializable
object Plants

@Serializable
data class FlowerPootDevice(
    val name: String,
    val address: String
)
