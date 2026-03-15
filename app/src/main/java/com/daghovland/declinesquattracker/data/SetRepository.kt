package com.daghovland.declinesquattracker.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

// Repository is the single source of truth for data — it owns the "what counts as today"
// logic so neither the DAO nor the ViewModel needs to know about it.
class SetRepository(private val dao: SquatSetDao) {

    fun getTodaySets(): Flow<List<SquatSet>> {
        val startOfDay = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())  // midnight in the device's local timezone
            .toInstant()
            .toEpochMilli()
        return dao.getTodaySets(startOfDay)
    }

    suspend fun logSet() = dao.insert(SquatSet())
}
