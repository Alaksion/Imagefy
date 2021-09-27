package br.com.alaksion.myapplication.ui.home.navigator

sealed class HomeScreens(val route: String) {

    class PhotosList() : HomeScreens("photos")
    class PhotoDetails() : HomeScreens("photo")
    class AuthorDetails() : HomeScreens("author")

}