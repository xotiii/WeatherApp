# WeatherApp
Weather Forecast Application for Decenternet

# How to build
Open in Android Studio -> Run -> Choose device

# Build APK
Open in Android Studio -> Build -> Build Bundle(s)/APK(s) -> Build APK(s)
## Locate APK
Open project directory -> app -> build -> outputs -> apk -> debug

# MVVM Architecture
This app follows the Android App Architecture
With the use of Repository, ViewModel, upto the View

# Technologies used
## MVVM Architecture
- Used for Single-Source of data (Repository) and as follows;
## ViewModel
- Used for data manipulation with Life-cycle awareness
## LiveData
- Used for as an observer for data manipulation from Repository
## SharedPreferences
- Used for single-data saving
## AppExecutors
- Used for multi-threading
## Glide
- Image loader

This app uses AppCompat Components for different API level support
