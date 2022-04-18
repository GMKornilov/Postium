package com.gmkornilov.postcreatepage.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal const val DB_NAME = "drafts"

private val titleKey = stringPreferencesKey(PostDraft.TITLE_KEY)
private val contentsKey = stringPreferencesKey(PostDraft.CONTENTS_KEY)

internal class PostDraftRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
)  {
    suspend fun getPostDraft(): PostDraft {
        val prefs = dataStore.data.first()

        val title = prefs[titleKey] ?: ""
        val contents = prefs[contentsKey] ?: ""

        return PostDraft(title, contents)
    }

    suspend fun updateDraft(postDraft: PostDraft) {
        dataStore.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                set(titleKey, postDraft.title)
                set(contentsKey, postDraft.contents)
            }
        }
    }
}