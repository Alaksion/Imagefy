package br.com.alaksion.myapplication.ui.home.navigator

sealed class HomeScreen(val route: String) {

    class PhotosList : HomeScreen("photos")
    class PhotoViewer : HomeScreen("photo")
    class AuthorDetails : HomeScreen("author")
    class UserProfile: HomeScreen("user_profile")
    class UserLikedPosts: HomeScreen("user_liked_posts")

}