package com.gmkornilov.design.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.constraintlayout.widget.Placeholder

@Composable
fun PasswordTextField(
    value: String,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    isPasswordVisible: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onPasswordVisibleChange: (Boolean) -> Unit = {},
    colors: TextFieldColors? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val icon = if (isPasswordVisible) {
                Icons.Filled.VisibilityOff
            } else {
                Icons.Filled.Visibility
            }
            IconButton(onClick = { onPasswordVisibleChange(!isPasswordVisible) }) {
                Icon(
                    imageVector = icon,
                    contentDescription = ""
                )
            }
        },
        colors = colors ?: TextFieldDefaults.outlinedTextFieldColors(),
        modifier = modifier
    )
}