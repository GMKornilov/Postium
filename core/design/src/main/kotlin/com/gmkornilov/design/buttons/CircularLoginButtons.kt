package com.gmkornilov.design.buttons


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmkornilov.design.R
import com.gmkornilov.design.theme.Apple
import com.gmkornilov.design.theme.Facebook
import com.gmkornilov.design.theme.Google
import com.gmkornilov.design.theme.Vk

@Composable
fun CircularSignInButton(
    backgroundColor: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit = { },
    icon: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(50.dp)
            .height(8.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        content = { icon() },
    )
}

@Preview
@Composable
fun CircularVkButton(onClick: () -> Unit = { }) {
    CircularSignInButton(backgroundColor = Vk.BackgroundColor, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_icons_vk),
            contentDescription = "",
            tint = Vk.ForegroundColor,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun CircularGoogleButton(onClick: () -> Unit = { }) {
    CircularSignInButton(backgroundColor = Google.BackgroundColor, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_icons_google),
            contentDescription = "",
            tint = Google.ForegroundColor,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun CircularAppleButton(onClick: () -> Unit = { }) {
    CircularSignInButton(backgroundColor = Apple.BackgroundColor, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_icons_apple),
            contentDescription = "",
            tint = Apple.ForegroundColor,
            modifier = Modifier.size(48.dp)
        )
    }
}

@Preview
@Composable
fun CircularFacebookButton(onClick: () -> Unit = { }) {
    CircularSignInButton(backgroundColor = Facebook.BackgroundColor, onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_icons_facebook),
            contentDescription = "",
            tint = Facebook.ForegroundColor,
            modifier = Modifier.size(48.dp)
        )
    }
}