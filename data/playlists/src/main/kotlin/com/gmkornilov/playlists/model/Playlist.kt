package com.gmkornilov.playlists.model

import com.google.firebase.firestore.PropertyName

data class Playlist(
    val id: String = "",
    val name: String = "",

    @get:PropertyName("posts")
    @set:PropertyName("posts")
    var postIds: List<String> = emptyList(),
)
