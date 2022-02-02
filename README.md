# NoteChaser

NoteChaser is an Android application for practical and highly configurable ear training. This app allows you to practice ear training exercises by using only your instrument and your phone so you can improve your muscle memory in the process!

Get it on the [Google Play Store](https://play.google.com/store/apps/details?id=com.cannonballapps.notechaser).

## Supported Modes

Currently NoteChaser only supports single note ear training. You can create configurations based on diatonic scales (e.g. major, melodic minor) or create your own based on chromatics. You can also specify the range of pitches, which allows you to do things like only practice notes that fall on the range of your guitar.

Upcoming features will introduce practice for more than one note, including intervals, chords, melodic dictations, and more!

## Technical Details

- Kotlin.
- Coroutines to process audio on background threads.
- MVVM architecture.
- [TarsosDSP](https://github.com/JorenSix/TarsosDSP) for digital signal processing.

