package com.gmkornilov.root_screen

import com.alphicc.brick.TreeRouter
import com.gmkornilov.authorizarion.data.AuthInteractor
import com.gmkornilov.authorizarion.domain.UserResultHandler
import com.gmkornilov.authorization.feature_flow.AuthorizationFlowScreenFactory
import com.gmkornilov.bottom_navigation_items.*
import com.gmkornilov.categories.model.Category
import com.gmkornilov.commentpage.brick_navigation.PostCommentArgument
import com.gmkornilov.commentpage.brick_navigation.PostCommentPageFactory
import com.gmkornilov.commentpage.view.CommentpageListener
import com.gmkornilov.comments.model.CommentPreviewData
import com.gmkornilov.mainpage.brick_navigation.MainpageScreenFactory
import com.gmkornilov.mainpage.mainpage.MainPageListener
import com.gmkornilov.playlists.model.Playlist
import com.gmkornilov.post.model.PostPreviewData
import com.gmkornilov.post_categories.categories_list.CategoriesListScreenFactory
import com.gmkornilov.post_categories.categories_list.view.CategoriesListener
import com.gmkornilov.post_categories.categories_posts.CategoriesPostsScreenFactory
import com.gmkornilov.post_list.view.PostsListListener
import com.gmkornilov.postcreatepage.brick_navigation.PostCreatePageScreenFactory
import com.gmkornilov.postcreatepage.view.PostCreateListener
import com.gmkornilov.postpage.brick_navigation.PostPageScreenFactory
import com.gmkornilov.postpage.view.PostpageListener
import com.gmkornilov.user_playlists.playlist_create.PlaylistCreateScreenFactory
import com.gmkornilov.user_playlists.playlist_create.domain.PlaylistCreateResultHandler
import com.gmkornilov.user_playlists.playlist_create.domain.merge
import com.gmkornilov.user_playlists.playlist_create.view.PlaylistCreateListener
import com.gmkornilov.user_playlists.playlist_list.PlaylistListScreenFactory
import com.gmkornilov.user_playlists.playlist_list.view.PlaylistListListener
import com.gmkornilov.user_playlists.playlist_posts.PlaylistPostsScreenFactory
import com.gmkornilov.userpage.brick_navigation.UserPageArgument
import com.gmkornilov.userpage.brick_navigation.UserPageScreenFactory
import com.gmkornilov.userpage.view.UserPageListener
import com.gmkornilov.view_model.BaseViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

class RootViewModel @Inject constructor(
    val bottomNavigationItems: List<@JvmSuppressWildcards BottomNavigationItem>,

    private val authInteractor: AuthInteractor,
    private val parentRouter: TreeRouter,

    private val categoryPostsScreenFactory: CategoriesPostsScreenFactory,
    private val categoriesListScreenFactory: CategoriesListScreenFactory,
    private val authorizationFlowScreenFactory: AuthorizationFlowScreenFactory,
    private val postPageScreenFactory: PostPageScreenFactory,
    private val userPageScreenFactory: UserPageScreenFactory,
    private val postCreatePageScreenFactory: PostCreatePageScreenFactory,
    private val mainpageScreenFactory: MainpageScreenFactory,
    private val commentScreenFactory: PostCommentPageFactory,
    private val playlistListScreenFactory: PlaylistListScreenFactory,
    private val playlistCreateScreenFactory: PlaylistCreateScreenFactory,
    private val playlistPostsScreenFactory: PlaylistPostsScreenFactory,
) : BaseViewModel<RootState, Nothing>(), UserPageListener, MainPageListener, PostpageListener,
    PostCreateListener, CommentpageListener, CategoriesListener,
    PlaylistListListener, PlaylistCreateListener, PostsListListener {
    private var currentRouterIndex = 0

    private val currentRouter
        get() = bottomNavigationItems[currentRouterIndex].router

    private val currentKey
        get() = currentRouter.screen.value?.key.orEmpty()

    private val userProfileResultHandler = UserResultHandler {
        val arg = UserPageArgument.LoadHeader(it.getUid())
        val screen = userPageScreenFactory.build(this, arg, currentKey)
        currentRouter.addScreen(screen)
    }

    private val playlistsResultHandler = UserResultHandler {
        val screen = playlistListScreenFactory.build(this, currentKey)
        currentRouter.addScreen(screen)
    }

    private val playlistCreateHandler = PlaylistCreateResultHandler {
        parentRouter.back()
    }

    fun onMenuItemClicked(index: Int) = intent {
        val item = bottomNavigationItems[index]
        currentRouterIndex = index

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
                is CategoriesBottomNavigationItem -> {
                    item.router.newRootScreen(
                        categoriesListScreenFactory.build(this@RootViewModel, currentKey)
                    )
                }
                is PlaylistsBottomNavigationItem -> {
                    val user = authInteractor.getPostiumUser()
                    user?.let {
                        playlistsResultHandler.handleResult(it)
                    } ?: startAuthorizationFlow(playlistsResultHandler)
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

    override fun exitRootScreen() {
        parentRouter.back()
    }

    override fun getBaseState(): RootState {
        return RootState(0, bottomNavigationItems.first().router)
    }

    override fun openCategory(category: Category) {
        val screen = categoryPostsScreenFactory.build(this, category, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun openPlaylist(playlist: Playlist) {
        val screen = playlistPostsScreenFactory.build(this, playlist, currentKey)
        currentRouter.addScreen(screen)
    }

    override fun createPlaylist(playlistCreateResultHandler: PlaylistCreateResultHandler) {
        val screen = playlistCreateScreenFactory.build(
            this,
            playlistCreateHandler.merge(playlistCreateResultHandler),
            currentKey
        )
        parentRouter.addChild(screen)
    }
}