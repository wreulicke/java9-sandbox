# Lombokを使った簡単なモジュールのコンパイル(動かなかった)

Java9 を使ってlombokを使った簡単なモジュールをコンパイルして実行してみます。
今回試すのはvalを使ったコードのみです。
今回利用していない、他のアノテーションを使ったAST変換は動作しない可能性があります。
このドキュメントは lombokが Java9 で完全に動作する保証はしておりません。

## Java 8では

### コンパイル

```
mkdir mod
javac -cp lib/*.jar -d mod $(find src -name *.java|grep -v module-info.java)
```

### 実行

```bash
java -cp mod com.github.wreulicke.HelloWorld
# >>> Hello World!!
```

## Java 9では

## コンパイル

`--processor-module-path` といった形で指定します。 `--module-path` では読み込まれないしコンパイルもできません。

```
javac --processor-module-path lib -d mod $(find src -name *.java)
```

## 実行

```bash
java -p mod -m com.github.wreulicke/com.github.wreulicke.HelloWorld
# >>> Hello World!!
```