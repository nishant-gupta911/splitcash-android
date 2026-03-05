package com.example.splitcash

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import java.text.NumberFormat

class SplitCashViewModel : ViewModel() {

    var amountInput by mutableStateOf("")
    var tipInput by mutableStateOf("")
    var newPersonName by mutableStateOf("")

    var groups by mutableStateOf(listOf<Group>())
        private set

    var currentGroupIndex by mutableStateOf<Int?>(null)
    var people by mutableStateOf(listOf<Person>())

        private set

    var expenseHistory by mutableStateOf(listOf<Expense>())
        private set

    fun addPerson() {
        if (newPersonName.isNotBlank()) {
            people = people + Person(newPersonName)
            newPersonName = ""
        }
    }

    fun removePerson(index: Int) {
        people = people.toMutableList().also {
            it.removeAt(index)
        }
    }

    fun createGroup(name: String) {
        if (name.isBlank()) return

        groups = groups + Group(name)

        currentGroupIndex = groups.lastIndex
    }

    fun selectGroup(index: Int) {
        currentGroupIndex = index
    }

    fun updatePayment(index: Int, value: String) {
        val paid = value.toDoubleOrNull() ?: 0.0
        people = people.toMutableList().also {
            it[index] = it[index].copy(amountPaid = paid)
        }
    }

    fun calculateTotal(): Double {
        val amount = amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
        return amount + (tipPercent / 100 * amount)
    }

    fun calculateShare(): Double {
        if (people.isEmpty()) return 0.0
        return calculateTotal() / people.size
    }

    fun calculateSettlements(): List<String> {

        if (people.isEmpty()) return emptyList()

        val total = calculateTotal()
        val share = total / people.size

        val creditors = mutableListOf<Pair<String, Double>>()
        val debtors = mutableListOf<Pair<String, Double>>()

        people.forEach {
            val balance = it.amountPaid - share
            if (balance > 0) creditors.add(it.name to balance)
            else if (balance < 0) debtors.add(it.name to -balance)
        }

        val results = mutableListOf<String>()

        var i = 0
        var j = 0

        while (i < debtors.size && j < creditors.size) {

            val debtor = debtors[i]
            val creditor = creditors[j]

            val amount = minOf(debtor.second, creditor.second)

            results.add(
                "${debtor.first} pays ${creditor.first} ${
                    NumberFormat.getCurrencyInstance().format(amount)
                }"
            )

            debtors[i] = debtor.first to (debtor.second - amount)
            creditors[j] = creditor.first to (creditor.second - amount)

            if (debtors[i].second == 0.0) i++
            if (creditors[j].second == 0.0) j++
        }

        return results
    }

    fun saveExpense() {

        if (people.isEmpty()) return

        val expense = Expense(
            title = "Expense ${expenseHistory.size + 1}",
            totalAmount = calculateTotal(),
            settlements = calculateSettlements()
        )

        expenseHistory = expenseHistory + expense

        currentGroupIndex?.let { index ->
            val updatedGroups = groups.toMutableList()
            updatedGroups[index].expenses.add(expense)
            groups = updatedGroups
        }

        resetAll()
    }

    fun resetAll() {
        amountInput = ""
        tipInput = ""
        newPersonName = ""
        people = emptyList()
    }
}