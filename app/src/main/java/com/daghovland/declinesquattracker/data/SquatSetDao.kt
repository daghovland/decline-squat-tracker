package com.daghovland.declinesquattracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// @Dao (Data Access Object) — the interface you write; Room generates the implementation.
// Think of it as your repository interface, but Room implements it via KSP code generation.
@Dao
interface SquatSetDao {

    // Flow<List<SquatSet>> is a reactive stream — the UI auto-updates whenever rows change.
    // Equivalent to a backend SSE/WebSocket subscription, but local.
    // Room re-emits on every INSERT/DELETE that touches the "sets" table.
    @Query("SELECT * FROM sets WHERE timestamp >= :startOfDay ORDER BY timestamp ASC")
    fun getTodaySets(startOfDay: Long): Flow<List<SquatSet>>

    // suspend = runs on a background coroutine; Room enforces that DB writes
    // never happen on the main thread (it would throw otherwise).
    @Insert
    suspend fun insert(set: SquatSet)
}
