# Imagefy
> Imagefy is an application developed with Android Jetpack Compose along with the Unsplash Api to serve as a Compose code sample, this project is still in development, so new features and code refactors will oftenly occur.

<img src="https://user-images.githubusercontent.com/30579274/138508180-b6e2cf10-2801-43af-96e0-287eecb228a2.jpg" width=25% height=25%/>
<img src="https://user-images.githubusercontent.com/30579274/138508252-ded98da5-88b4-49db-adec-681a5af10003.jpg" width=25% height=25%/>

### Future features and enhancements
- [ ] Allow users to download images from the photo viewer
- [ ] Refact the user profile photo grid to scroll with the screen instead of being a scrollable container
- [ ] Add swipe refresh to photo list
- [ ] Refact lazy columns/vertical grids to launch onListEnd more effectively 

## 💻 Requisites

To run this application please be sure that the requirements below are met
* Install the most recent version of Android Studio Canary you can find the link download link [here](https://developer.android.com/studio/preview?hl=pt&gclid=CjwKCAjwwsmLBhACEiwANq-tXLkBkEHvrK_Tt4JdHaJHr435HTJJDc01GMtwKp_CRt_jeqLhq9cbLxoCnTcQAvD_BwE&gclsrc=aw.ds)
* Install a mobile emulator compatible with Android Studio or prepare a physical device to run the application, you can find more about this [here](https://developer.android.com/training/basics/firstapp/running-app)
* Create your own Unsplash Api Keys by creating an [Unsplash account and registering as a developer](https://unsplash.com/developers)
* In the "Redirect URI & Permissions" section replace the default url with ```https://imagefy.com.br/auth```
* In the "Redirect URI & Permissions" mark the following permissions: ```Public Access, Read User Access, Write likes access```
* Click Save.

## 💻 If you are using Android Api 31+ (Android 12.0 or newer)
Android 12 comes with new security features and one of this features is not allow unvalidated deep links work by default, because Imagefy's "domain" is not configured in the official Unsplash domain you will have to manually open the app settings in your device and manually add the following domain ```https://imagefy.com.br/auth``` (Yes the same used in Unsplash platform configuration) 

## ☕ How to use Imagefy

To run the application locally follow the steps below:
1. Clone this repository.
2. Open the project's directory using Android Studio.
3. Run the following command using your Unsplash api keys: ```gradlew assignKeys -PsecretKey=\"your-secret-key\" -PpublicKey=\"your-public-key\"```
4. Now your project is ready to be launched!

## Known Issues:
1. Deep links must be manually allowed in the device's app configuration.
2. Image's Vertical grid sometimes launches getMoreItems() improperly
3. Some images take too much time to load in the image viewer

[⬆ Voltar ao topo](#Imagefy)<br>
