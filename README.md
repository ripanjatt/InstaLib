> A fast library to get photos/videos/reels from a  public instagram post!

This library doesn't download the files on its own. It extracts all the file links from a post.
You can use those links to download using your custom download or you can use my own <a href="https://github.com/ripanjatt/jDownloader">jDowbloader</a> library.

Features
-
* Works for both java and kotlin.
* Works with android.
* Allows posts with multiple photos/videos.
* Allows photos/videos/reels.

Feel free to use it in your own project.
Don't forget to mention❤.

Gradle implementation(in app level build.gradle)
-
`allprojects {
  repositories {
      google()
      maven { url 'https://jitpack.io' }
  }
}

dependencies {
 implementation 'com.github.ripanjatt:InstaLib:1.0.0'
}`
