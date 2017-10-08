# 複数モジュールのコンパイルと実行

## コンパイル

anotherモジュールをコンパイルします。

```
javac -d mod/com.github.wreulicke.another $(find src/com.github.wreulicke.another/ -name *.java)
```

同じようにmainメソッドのあるモジュールをコンパイルします。
この時、-pでmodule-pathを指定します。

```
javac -p mod -d mod/com.github.wreulicke $(find src/com.github.wreulicke -name *.java)
```

## 実行

実行してみます。

```bash
java --module-path mod -m com.github.wreulicke/com.github.wreulicke.HelloWorld
# >>> Hello Another World!!
```

## マルチモジュールコンパイル

分離してコンパイルしなくてもコンパイル可能だったりします。
-pは--module-pathのショートハンドです。

```bash 
javac -d mod --module-source-path src $(find src -name "*.java")
java -p mod -m com.github.wreulicke/com.github.wreulicke.HelloWorld
# >>> Hello Another World!!
```