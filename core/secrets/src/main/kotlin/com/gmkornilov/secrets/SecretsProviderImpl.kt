package com.gmkornilov.secrets

import javax.inject.Inject

class SecretsProviderImpl @Inject constructor(): SecretsProvider {
    override val googleApiKey = BuildConfig.GOOGLE_API_KEY
    override val facebookAppId = BuildConfig.FACEBOOK_APP_ID
    override val facebookAppSecret = BuildConfig.FACEBOOK_APP_SECRET
    override val facebookClientToken = BuildConfig.FACEBOOK_CLIENT_TOKEN
}