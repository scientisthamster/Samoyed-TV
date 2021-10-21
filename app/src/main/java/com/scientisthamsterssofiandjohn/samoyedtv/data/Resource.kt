package com.scientisthamsterssofiandjohn.samoyedtv.data

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val error: Throwable?
)