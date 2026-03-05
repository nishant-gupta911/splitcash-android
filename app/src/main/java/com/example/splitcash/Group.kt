package com.example.splitcash

data class Group(
    val name: String,
    val expenses: MutableList<Expense> = mutableListOf()
)