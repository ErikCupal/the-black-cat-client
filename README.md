# The Black Cat - Client 🎮🃏

[![Travis branch](https://img.shields.io/travis/ErikCupal/the-black-cat-client/master.svg?style=flat-square)](https://travis-ci.org/ErikCupal/the-black-cat-client)

The Black Cat is an online multiplayer card game. It consists of two parts: Node.js [server](https://github.com/ErikCupal/the-black-cat-server) and [client](https://github.com/ErikCupal/the-black-cat-client) app written in Kotlin and based on LibGDX game framework.

### [Website](https://theblackcat.club)

### [Server repo](https://github.com/ErikCupal/the-black-cat-server)

## Download

[Latest release](https://github.com/ErikCupal/the-black-cat-client/releases) - crossplatform JAR (The game needs instaled Java version 6 and up)

## Official active servers (🔥 still under development 🔥)

* [https://sr1.theblackcat.club](https://sr1.theblackcat.club)
* [https://sr2.theblackcat.club](https://sr2.theblackcat.club)
* [https://sr3.theblackcat.club](https://sr3.theblackcat.club)
* [https://sr4.theblackcat.club](https://sr4.theblackcat.club)

## How to play? 🤔

Read the [game rules](https://github.com/ErikCupal/the-black-cat-client/wiki/Game-rules)

## Credits

### Textures

In the game are used textures **designed by [Freepik](http://www.freepik.com/)**.
In the game are used button and input textures **designed by [Lamoot](https://opengameart.org/users/lamoot)**.
Card textures are under CC License. Source [freedesignfile.com](http://freedesignfile.com).

### Fonts

In the game is used **Merriweather** font created by **[Sorkin Type](http://sorkintype.com/)**.

## Libraries used

* [LibGDX](https://libgdx.badlogicgames.com/)
* [Socket.IO](https://github.com/socketio/socket.io)
* [jackson-module-kotlin](https://github.com/FasterXML/jackson-module-kotlin)
* [RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [KTX](https://github.com/czyzby/ktx)

## How to experiment

Clone the repo

To launch the game run command `./gradlew desktop:run`

To pack new game textures run command `./gradlew texturePacker`

To generate code documentation run command `./gradlew dokka && ./gradlew copyDocCSS`

## Future (possible features)

* Add application wrappers for specific platforms (`.exe` for Windows, `App` for macOS)
* Extend rules (10 more points for last taken trick etc.)
* Add support for Android tablets

## License

MIT
