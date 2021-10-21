package com.scientisthamsterssofiandjohn.samoyedtv.util

class QueryHelper {

    companion object {
        fun popularMovies(): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["sort_by"] = "popularity.desk"
            map["include_video"] = "false"
            return map
        }

        fun topRateMovies(): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["sort_by"] = "popularity.desk"
            map["include_video"] = "false"
            return map
        }

        fun trendingMovies(): HashMap<String, String> {
            val map = HashMap<String, String>()
            map["time_window"] = "week"
            return map
        }
    }
}