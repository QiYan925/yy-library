#support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

#retrolambda
-dontwarn java.lang.invoke.*

#support-v4
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

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
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes *Annotation*
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


#rx
-dontwarn io.reactivex.**
-keep io.reactivex.**
-keepclassmembers class io.reactivex.** { *; }