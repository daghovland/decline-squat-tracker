package com.daghovland.declinesquattracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity maps this data class to the "sets" table.
// Room generates the CREATE TABLE DDL from these annotations at compile time.
// Named SquatSet (not Set) to avoid clashing with Kotlin's built-in Set type.
@Entity(tableName = "sets")
data class SquatSet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
)
