package com.example.splitcash

data class Expense(
    val title: String,
    val totalAmount: Double,
    val settlements: List<String>
)