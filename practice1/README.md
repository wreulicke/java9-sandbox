# Practice1 簡単なモジュールのコンパイルと実行

ここでは Java9 を使って簡単なモジュールをコンパイルして実行してみます。
なんの依存もないモジュールです。

## Java 8では

### コンパイル

```
mkdir mod
javac -d mod $(find src -name *.java|grep -v module-info.java)
```

### 実行

```bash
java -cp mod com.github.wreulicke.HelloWorld
# >>> Hello World!!
```

## Java 9では

Dockerを使って試します。

```
$ docker run -it -v $(pwd):/app openjdk:9-jdk bash
Container $ cd /app 
```

## コンパイル

```
javac -d mod $(find src -name *.java)
```

## 実行

Java9でも普通に-cpで実行できます。

```bash
java -cp mod com.github.wreulicke.HelloWorld
# >>> Hello World!!
```

でもこれではつまらないですね。

moduleっぽさを出して実行してみます。

```bash
java --module-path mod -m com.github.wreulicke/com.github.wreulicke.HelloWorld
# >>> Hello World!!
```

モジュールっぽさを出して、まず第一段階は終わりです。