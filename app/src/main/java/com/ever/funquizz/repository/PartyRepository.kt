package com.ever.funquizz.model

import android.content.Context

private const val PREF_NAME = "party_pref"
private const val KEY_LIST  = "party_list"

class PartyRepository(context: Context) {

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /* ajoute une partie */
    fun saveParty(party: Party) {
        val current = getAllParties().toMutableList()
        current.add(party)
        prefs.edit()
            .putString(KEY_LIST, PartySerializer.toJsonList(current))
            .apply()
    }

    /* toutes les parties, triées score décroissant */
    fun getAllParties(): List<Party> =
        PartySerializer.fromJsonList(
            prefs.getString(KEY_LIST, "[]") ?: "[]"
        ).sortedByDescending { it.score }

    /* ID auto-incrément */
    fun nextId(): Int = (getAllParties().maxOfOrNull { it.idParty } ?: 0) + 1
}