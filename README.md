# apt-gui

This is a graphical user interface for [apt](https://github.com/CvO-Theory/apt).

## Screenshot

![Screenshot of APT-GUI](/screenshot.png?raw=true "Screenshot taken 2016/06/28")

## How to build

It is expected that the root folder of apt is next to the root folder of apt-gui in the file system.

```
home/
 |--- apt/
 |     |--- classes/
 |     |--- lib/
 |     |--- build.xml
 |     |--- apt.jar
 |--- apt-gui/
       |--- build.gradle
```

You need to build apt first since the apt/apt.jar is expected to be available for compilation of apt-gui. Then you can build apt-gui using the following commands:

```
cd apt-gui
./gradlew jar
```

Some other available build targets are: eclipse, javadoc
