#!/bin/bush

rm -rf ./bundle

mkdir bundle

# create bundle for Android
yarn bundle-android

yarn hermes-compile-android
mv -f ./bundle/miniapp.android.bundle ./bundle/android/res/miniapp.android.bundle

rm -rf ./bundle/miniapp.android.bundle.js


# create bundle for iOS
yarn bundle-ios

yarn hermes-compile-ios
mv -f ./bundle/miniapp.ios.bundle ./bundle/ios/miniapp.ios.bundle

rm -rf ./bundle/miniapp.ios.bundle.js