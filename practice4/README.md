# Automatic Module Name

今回はAutomatic Module Nameを使ってみます。
この機能はJarファイル名からmodule名が自動的に決定します。
これはJava8で作られたモジュールをいい感じに利用するための機能だと思われます。

## コンパイル

各モジュールをコンパイルしてパッケージします。
まずはmodule-info.javaを持っていないjarを作ります。

```
javac -d mod/com.github.wreulicke.another $(find src/com.github.wreulicke.another/ -name *.java)
jar --create --file=mlib/com.github.wreulicke.another.jar -C mod/com.github.wreulicke.another .
```

先ほど作ったモジュールを-pで指定して
ビルドとパッケージをしてみます。

```
javac -p mlib -d mod/com.github.wreulicke $(find src/com.github.wreulicke -name *.java)
jar --create --file=mlib/com.github.wreulicke.jar --main-class=com.github.wreulicke.HelloWorld -C mod/com.github.wreulicke .
```

## 実行

実行してみます。

```bash
java -p mlib -m com.github.wreulicke
# >>> Hello Another World!!
```

今回はanotherモジュールに外部の依存がなかったので良かったのですが
他のモジュールに依存がある場合はどうするのでしょうね・・・。

## バージョン付きのJar名

Automatic Module Nameでは一定の規則に則っていればjarのファイル名から
自動的にバージョンを取り外した名前で
モジュールが認識されます。
そのため、`com.github.wreulicke.another-1.0.0-SNAPSHOT.jar` の場合
`com.github.wreulicke.another` としてモジュールを認識してくれます。

module-info.javaを持っていないモジュールをビルドして
バージョン付きJar名にしてパッケージしてみます。

```
javac -d mod/com.github.wreulicke.another $(find src/com.github.wreulicke.another/ -name *.java)
jar --create --file=mlib/com.github.wreulicke.another-1.0.0-SNAPSHOT.jar -C mod/com.github.wreulicke.another .
```

先ほど作ったモジュールを使って
もう一つのモジュールをビルドしてパッケージしてみます。

```
javac -p mlib -d mod/com.github.wreulicke $(find src/com.github.wreulicke -name *.java)
jar --create --file=mlib/com.github.wreulicke.jar --main-class=com.github.wreulicke.HelloWorld -C mod/com.github.wreulicke .
```

ちゃんと実行可能です。

```bash
java -p mlib -m com.github.wreulicke
# >>> Hello Another World!!
```