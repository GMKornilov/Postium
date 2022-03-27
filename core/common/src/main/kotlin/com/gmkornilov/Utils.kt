package com.gmkornilov

fun <T, R> T.letIf(condition: Boolean, block: (T) -> R): R? {
    return if (condition) {
        run(block)
    } else null
}

fun <T, R> T.letIf(condition: (T) -> Boolean, block: (T) -> R): R? {
    return if (run(condition)) {
        run(block)
    } else null
}

fun <T, R> T.letIf(condition: Boolean, block: (T) -> R, elseBlock: (T) -> R): R {
    return if (condition) {
        run(block)
    } else {
        run(elseBlock)
    }
}

fun <T, R> T.letIf(condition: (T) -> Boolean, block: (T) -> R, elseBlock: (T) -> R): R {
    return if (run(condition)) {
        run(block)
    } else {
        run(elseBlock)
    }
}