-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

#-libraryjars   libs/android-support-v4.jar
#-libraryjars   libs/baidumapapi_v2_2_0.jar
#-libraryjars   libs/locSDK_3.1.jar

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-keep public class * extends com.umeng.**

-dontwarn u.aly.**
-keep class u.aly.** { *; }
-keep public class * extends u.aly.**

-dontwarn com.hp.**
-keep class com.hp.** { *; }
-keep public class * extends com.hp.**

-dontwarn demo.**
-keep class demo.** { *; }

-dontwarn net.sourceforge.**
-keep class net.sourceforge.** { *; }
-keep public class * extends net.sourceforge.**

-dontwarn pinyindb.**
-keep class pinyindb.** { *; }
-keep public class * extends pinyindb.**

-keepclasseswithmembernames class * {

native <methods>;
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
public void *(android.view.View);
}

-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

