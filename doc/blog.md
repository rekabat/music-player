Tyger Audio

# Day 1 - 2017/3/13

`https://developer.android.com/training/basics/firstapp/creating-project.html`

Steps for initialization of project:

- Create a new Github repository.
- Download and install JDK 8 from oracle website.
- Download and install Android Studio.
- Needed to rm and mkdir `/Users/me/.android` to give appropriate permissions.

Initially, I did the following, but instead decided I'd rather learn the official IDE:

- Download and install Android SDK from Macports (`sudo port install android`).
    - This installs the SDK into `/opt/local/share/java/android-sdk-macos` (henceforth `$ASDK`).
    - Check out `$ASDK/SDK Readme.txt`
- Run `./$ASDK/tools/android update sdk --no-ui`, agree to all prompts.
- Run `./$ASDK/tools/android create project`.






Considered features based on previous apps I've used and the pain points I experienced. These include:

- Playback
    - Play mp3s
    - Play other popular formats (e.g. FLAC, mp4, wma, ogg, wav)
    - Display album art
    - Display metadata
- Directory browser (only, no metadata indexing)
    - Adding a "home" directory (or several)
    - Visually distinguish audio from directory
- Tap and hold to create a "Play now" playlist
- Lock screen menu (controllable with headset buttons)
- Home screen widget
- Song snipping (for ringtones)
- Multiple colors schemes (at least day/night)
- Search
- Shuffle
- Repeat
- Navigation
    - Return to home directory
    - Go to directory of playing song


# Day 2 - 2017/3/16
I wanted to remove the title bar from the app activity before I bothered laying anything out. This was done by changing the `AppTheme` styling, found in `app/res/values/styles.xml`, to have `NoActionBar` in the logical place.

Spent a lot of time learning how to position elements. Different containers and elements.
