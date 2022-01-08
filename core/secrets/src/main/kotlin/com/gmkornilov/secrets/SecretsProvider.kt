package com.gmkornilov.secrets

interface SecretsProvider {
    val googleApiKey: String

    val facebookAppId: String
    val facebookAppSecret: String
    val facebookClientToken: String
}