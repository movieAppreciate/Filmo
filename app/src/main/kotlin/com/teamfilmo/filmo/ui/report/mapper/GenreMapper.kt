package com.teamfilmo.filmo.ui.report.mapper

// 영화 장르 매핑
object GenreMapper {
    val genreMap =
        mapOf(
            28 to "액션",
            12 to "모험",
            16 to "애니메이션",
            35 to "코미디",
            80 to "범죄",
            99 to "다큐멘터리",
            18 to "드라마",
            10751 to "가족",
            14 to "판타지",
            36 to "역사",
            27 to "공포",
            10402 to "음악",
            9648 to "미스터리",
            10749 to "로맨스",
            878 to "SF",
            10770 to "TV 영화",
            53 to "스릴러",
            10752 to "전쟁",
            37 to "서부",
        )

    fun getGenreName(id: Int): String = genreMap[id] ?: "알 수 없음"
}
