# Mantiene tutte le classi, i metodi e i campi nel package principale della tua app.
-keep class it.stefanobusceti.krono.** { *; }
#KOIN
# Keep annotation definitions
-keep class org.koin.core.annotation.** { *; }
# Keep classes annotated with Koin annotations
-keep @org.koin.core.annotation.* class * { *; }
#ROOM
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-dontwarn androidx.sqlite.**
-keep class androidx.sqlite.** { *;}