package com.gmkornilov.postcreatepage.data

data class PostDraft(
    val title: String = "",
    val contents: String = "",
) {
    companion object {
        const val TITLE_KEY = "title"
        const val CONTENTS_KEY = "contents"
    }
}
