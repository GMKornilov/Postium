package com.gmkornilov.root_screen

import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.bottom_navigation_items.BottomNavigationItem
import com.gmkornilov.bottom_navigation_items.HomeBottomNavigationItem
import com.gmkornilov.bottom_navigation_items.ProfileBottomNavigationItem
import com.gmkornilov.commentpage.brick_navigation.PostCommentArgument
import com.gmkornilov.commentpage.brick_navigation.PostCommentPageFactory
import com.gmkornilov.commentpage.view.CommentpageListener
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.comments.model.CommentPreviewData
import com.gmkornilov.postcreatepage.brick_navigation.PostCreatePageScreenFactory
import com.gmkornilov.postcreatepage.view.PostCreateListener
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.userpage.brick_navigation.UserPageArgument
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import com.gmkornilov.userpage.view.UserPageListener
import com.gmkornilov.view_model.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class RootViewModel @Inject constructor(
    val bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>,

    private val authInteractor: AuthInteractor,

    private val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory,
    private val postPageScreenFactory: PostPageScreenFactory,
    private val userPageScreenFactory: UserPageScreenFactory,
    private val postCreatePageScreenFactory: PostCreatePageScreenFactory,
    private val mainpageScreenFactory: MainpageScreenFactory,
    private val commentScreenFactory: PostCommentPageFactory,
) : BaseViewModel<RootState, Nothing>(), UserPageListener, MainPageListener, PostpageListener,
    PostCreateListener, CommentpageListener {
    private val routerIndexFlow = MutableStateFlow(0)

    private val currentRouter
        get() = bottomNavigationItems[routerIndexFlow.value].router

    private val currentKey
        get() = currentRouter.screen.value?.key.orEmpty()

    private val userProfileResultHandler = UserResultHandler {
        val arg = UserPageArgument.LoadHeader(it.getUid())
        val screen = userPageScreenFactory.build(this, arg, currentKey)
        currentRouter.addScreen(screen)
    }

    fun onMenuItemClicked(index: Int) = intent {
        val item = bottomNavigationItems[index]
        routerIndexFlow.value = index

        if (item.router.isEmpty()) {
            when (item) {
                is HomeBottomNavigationItem -> {
                    item.router.newRootScreen(
                        mainpageScreenFactory.build(
                            this@RootViewModel,
                            currentKey
                        )
                    )
                }
                is ProfileBottomNavigationItem -> {
                    val user = authInteractor.getPostiumUser()
                    user?.let {
                        userProfileResultHandler.handleResult(user)
                    } ?: startAuthorizationFlow(userProfileResultHandler)
                }
            }
        }

        reduce { RootState(index, item.router) }
    }

    override fun startAuthorizationFlow(userResultHandler: UserResultHandler) {
        authorizationFlowScreenFactory.start(userResultHandler, currentRouter)
    }

    override fun openUserProfile(comment: CommentPreviewData) {
        val userPageArgument = comment.toUserPageArgument()
        val screen = userPageScreenFactory.build(this, userPageArgument, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun openPost(postPreviewData: PostPreviewData) {
        val argument = postPreviewData.toPostPageArgument()
        val screen = postPageScreenFactory.build(this, argument, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun openUserProfile(postPreviewData: PostPreviewData) {
        val userPageArgument = postPreviewData.toUserPageArgument()
        val screen = userPageScreenFactory.build(this, userPageArgument, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun openComments(postId: String) {
        val commentPageArgument = PostCommentArgument(postId)
        val screen = commentScreenFactory.build(commentPageArgument, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun createPost() {
        val screen = postCreatePageScreenFactory.build(this, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun exitScreen() {
        currentRouter.back()
    }

    private fun getRouter(index: Int) = bottomNavigationItems[index].router

    override fun getBaseState(): RootState {
        return RootState(0, bottomNavigationItems.first().router)
    }
}