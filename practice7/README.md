## 公開パッケージの動的追加

今回は少し複雑なモジュール構成を取ってみます。


## コンパイル

各モジュールをコンパイルします。

```
javac -d mod --module-source-path src $(find src -name "*.java")
```

## 実行

```bash
java -p mod -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> hogehoge
```

普通に動きます。

これだけだと何も面白くありません。

さて、もう少し依存関係を見てみます。
以下のような形の依存関係になっています。

* dispatcher 
  * [required] handler
* handler 依存なし
* main
  * [required] handler
  * [required] dispatcher

handlerでinterface定義されたハンドラをmainモジュールでラムダ式によって実装し
dispatcherで定義されたディスパッチャに対して
引数で渡してハンドラを実行しています。

特に問題なく実行できています。

これはDispatcherには公開されたhandlerモジュールを要求しており
dispatcherモジュールはアクセス権を持った状態になっています。
そのため、mainモジュールで実装したオブジェクトであっても、handlerモジュールが公開しているメンバにアクセスできる、というわけです。

これだけを見ると、リフレクションによるメンバ呼び出しは避けて行くほうが良さそうですね。

コンパイル時に解決されない依存関係によって
ランタイム時に発生する実行時例外で死ぬことになるでしょう。
メッセージは優しいので、自分のモジュール内であれば対処出来るかもしれません。