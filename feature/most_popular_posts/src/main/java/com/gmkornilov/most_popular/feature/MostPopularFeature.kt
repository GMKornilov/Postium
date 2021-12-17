package com.gmkornilov.most_popular.feature

import com.gmkornilov.feature_api.FeatureApi

interface MostPopularFeature: FeatureApi {
    val route: String
}