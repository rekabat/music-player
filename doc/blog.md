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

Okay. That was too technical. Going forward things will be more holistic.

Spent a lot of time learning how to position elements. Different containers and elements. Not much to say, it's just the basics right now. My main activity has three main ConstraintLayouts: a header, the main center one which displays the directory browser, and the footer which gives playback information and controls.

# Day 3 - 2017/3/17
Figure out the ListView container and browsing directories. This is for the center layout. Just learning basics.

Made a custom adapter for the ListView. This was complicated and took some researching. But it seems necessary due to the complicated nature of my ListView, which I intend to separately alphabetize directories and files, and displays dirs first then files. Keep things modular! The logic for this does not belong in the main activity. Currently, I only display directories. I want to nail down navigation first.

Display the current directory in the title bar. This was done by adding a TextView to the header ConstraintLayout, and modifying the displayed text anytime the directory is changed.

Handle back button. I could just use activities, which completely save the state of the app and automatically get handled by the back button. However, the transition animation it uses is jarring and doesn't make sense given that you're navigating directories. This should just be an instantaneous change. This was simple to do, just create an onBackPressed function that informs the adapter to go up a directory. This lead to a slight issue where the view is scrolled down to where you were in the deeper directory! I fixed this by just bumping it to the top. I also needed to close the app if you're in the root directory and press back.

Show directories AND files. This took a little bit of time where I had to finagle the adapter.

# Day 4 - 2017/3/19
Today is all about learning styling! I'm tired of staring at this ugly app.

Bold font directories, normal font files. Good start. Just a matter of modifying the TextView properties when the adapter returns a view. Still ugly.

Change theme/styling of app. Everything is dark now. I learned about the Android material theme, which makes it easy to specify just a few colors but change the look of all the widgets, and app bars, etc. Much better. Now I have two different font colors for dirs/files.

Add a bottom border to the header, and a top border to the footer. This was kind of complicated, and taught me how to make my own drawables.

Change pause/play toggle to images. There is no widget for this, only a toggle button with text. I searched around online for some help, made a new drawable. It's close, but the image is scaled for some reason. Not a permanent solution.

Weird problem. When I press the toggle button, the size of the list view shrinks. Never goes back to the original size. I have no idea what the problem is.

Fixing back handling. Oops. Last time I just made back go up a directory and *scroll to the top*. That doesn't make sense. The user should go back to where they left off. Of course launching new activities would do this, but I don't want to do it that way! It appears, with Parcelables, there is a way to save the state of an element. So I just create a stack of these and pop one off when going back. Seems sound, but for some reason it doesn't always work. Don't know why yet.

# Day 5 - 2017/3/20
Getting permission to read from an SD card. The user will likely store media on an externally. Need to add permissions to the manifest, and then request from the user. Ultimately, it should be okay if they say "no", just don't read from external files. If they say "no" and change their mind in the future, there should be a setting that lets them adjust this. Right now I just expect a "yes".

Only show music files. No need to show all files. Have a list of extensions, if the file matches something in the list, display it.

Refactor directory adapter. Pull out all path parsing and and file finding into a new static class.


# Things to look into
fastScrollEnabled?

File names too long? In list, or on title bar

Display name of file above scrubber?
