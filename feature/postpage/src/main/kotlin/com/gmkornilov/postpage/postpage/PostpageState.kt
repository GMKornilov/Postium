package com.gmkornilov.postpage.postpage

import com.gmkornilov.postpage.brick_navigation.PostPageArgument

internal sealed class PostpageState(val argument: PostPageArgument) {
    class None(argument: PostPageArgument): PostpageState(argument)

    class Loading(argument: PostPageArgument): PostpageState(argument)

    class Success(content: String, argument: PostPageArgument): PostpageState(argument)

    class Error(e: Exception, argument: PostPageArgument): PostpageState(argument)
}