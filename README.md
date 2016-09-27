# apt-gui

This is a graphical user interface for [apt](https://github.com/CvO-Theory/apt).

A pre-built "apt-gui.jar" file is available
[here](https://cvo-theory.github.io/apt-gui-builds/apt-gui.jar).

## Screenshot

![Screenshot of APT-GUI](/screenshot.png?raw=true "Screenshot taken 2016/06/28")

## How to build

apt-gui depends on apt which is included as a git submodule. Therefore either clone with

```
git clone --recursive https://github.com/jprellberg/apt-gui.git
```

or run

```
git submodule init
git submodule update
```

after cloning.

After cloning you can build apt-gui with

```
cd apt-gui
./gradlew jar
```

Some other available build targets are: clean, eclipse, javadoc
