package com.gmkornilov.commentpage.brick_navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alphicc.brick.DataContainer
import com.alphicc.brick.Screen
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.brick_navigation.Dependency
import com.gmkornilov.brick_navigation.DependencyProvider
import com.gmkornilov.brick_navigation.ScreenFactory
import com.gmkornilov.commentpage.view.CommentPage
import com.gmkornilov.commentpage.view.CommentPageViewModel
import com.gmkornilov.commentpage.view.CommentpageListener
import com.gmkornilov.comments.repostiory.CommentRepository
import com.gmkornilov.strings.StringsProvider
import com.gmkornilov.view_model.BaseViewModel
import dagger.BindsInstance
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

private const val COMMENT_KEY = "comments"

class PostCommentPageFactory @Inject constructor(
    override val dependency: Deps,
) : DependencyProvider<PostCommentPageFactory.Deps> {
    private inner class Factory(
        val postCommentArgument: PostCommentArgument,
    ) : ScreenFactory() {
        override fun buildKey(): String {
            return "${COMMENT_KEY}_${postCommentArgument.postId}"
        }

        override fun onCreate(
            flow: SharedFlow<DataContainer>,
            arg: DataContainer
        ): BaseViewModel<*, *> {
            val component =
                DaggerPostCommentPageFactory_Component.builder()
                    .argument(postCommentArgument)
                    .deps(dependency).build()
            return component.viewModel
        }

        @Composable
        override fun Content(arg: DataContainer) {
            val viewModel = arg.get<CommentPageViewModel>()
            CommentPage(viewModel = viewModel, modifier = Modifier.fillMaxSize())
        }
    }

    fun build(postCommentArgument: PostCommentArgument, prevPath: String): Screen<*> {
        return Factory(postCommentArgument).build(prevPath)
    }

    interface Deps : Dependency {
        val commentRepository: CommentRepository
        val authInteractor: AuthInteractor
        val commentpageListener: CommentpageListener
        val stringsProvider: StringsProvider
    }

    @dagger.Component(
        dependencies = [Deps::class]
    )
    internal interface Component {
        val viewModel: CommentPageViewModel

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun argument(postCommentArgument: PostCommentArgument): Builder

            fun deps(dependency: Deps): Builder

            fun build(): Component
        }
    }
}