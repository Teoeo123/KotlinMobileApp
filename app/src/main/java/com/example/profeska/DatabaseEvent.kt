package com.example.profeska

data class DatabaseEvent(
    val name: String?=null,
    val description: String?=null,
    val slots: Int?=null,
    val city: String?=null,
    val street: String?=null,
    val num: String?=null,
    val photo: String?=null,
    val date: String?=null
)
