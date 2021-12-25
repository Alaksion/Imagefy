package br.com.alaksion.myapplication.ui.navigator

sealed class HomeScreen(val route: String) {

    class Splash: HomeScreen("splash")
    class Login: HomeScreen("login")
    class AuthHandler: HomeScreen("auth_handler")
    class PhotosList : HomeScreen("photos")
    class SearchPhotos : HomeScreen("search_photos")
    class PhotoViewer : HomeScreen("photo")
    class AuthorDetails : HomeScreen("author")

}