package com.gmkornilov.authorization.feature_api

import com.gmkornilov.feature_api.FeatureApi

interface AuthorizationFlowFeature: FeatureApi {
    val route: String
}