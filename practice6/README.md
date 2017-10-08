## 公開パッケージの動的追加

今回はモジュールレベルのリフレクションを使って
ランタイムにパッケージを公開してみます。

あくまで、ここでできるのはメインモジュールに対してopenであるモジュールが存在する場合に
別のモジュールに対してopenできる、というものです。

## コンパイル

各モジュールをコンパイルします。

```
javac -d mod --module-source-path src $(find src -name "*.java")
```

## 実行

まず何も考えずに実行してみます。

```bash
java -p mod -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
## >>> error occured
## >>> java.lang.ClassNotFoundException: com.github.wreulicke.another.AnotherWorld
## >>>	 at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:582)
## >>>	 at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:185)
## >>>	 at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:496)
## >>>	 at java.base/java.lang.Class.forName0(Native Method)
## >>>	 at java.base/java.lang.Class.forName(Class.java:292)
## >>>	 at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:10)
## >>>	 at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:10)
## >>> Exception in thread "main" java.util.NoSuchElementException: No value present
## >>>	 at java.base/java.util.Optional.get(Optional.java:151)
## >>>	 at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:12)
```

ClassNotFoundExceptionが出ています。いやないわけないやろ！！って気持ちになりますが、こういうものなんでしょう。
2個目の例外は `com.github.wreulicke.main` モジュールから`com.github.wreulicke.another` モジュールが見えていないようです。

今度は `--add-modules` に追加して実行してみます。

```bash
java --add-modules com.github.wreulicke.another -p mod -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> error occured
# >>> java.lang.IllegalAccessException: class com.github.wreulicke.HelloWorld (in module com.github.wreulicke) cannot access class com.github.wreulicke.another.AnotherWorld (in module com.github.wreulicke.another) because module com.github.wreulicke.another does not export com.github.wreulicke.another to module com.github.wreulicke
# >>>	 at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
# >>>	 at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:589)
# >>>	 at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:479)
# >>>	 at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:11)
# >>>	 at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:10)
# >>> test
# >>> Exception in thread "main" java.lang.IllegalCallerException: com.github.wreulicke.another is not open to module com.github.wreulicke.main
# >>>	 at java.base/java.lang.Module.addOpens(Module.java:751)
# >>>  at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:14)
```

ClassNotFoundExceptionからIllegalAccessExceptionに変わりました。不思議ですね。
また、後のエラーを見るとIllegalCallerExceptionが発生しているようです。
mainモジュールからanotherモジュールは見えないよ、と怒られているようです。

今度は `--add-opens` を使ってpackageをopenしてみましょう。(moduleをopenにする方法ってないのかな)

```bash
java --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke.main --add-modules com.github.wreulicke.another -p mod -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> error occured
# >>> java.lang.IllegalAccessException: class com.github.wreulicke.HelloWorld (in module com.github.wreulicke) cannot access class com.github.wreulicke.another.AnotherWorld (in module com.github.wreulicke.another) because module com.github.wreulicke.another does not export com.github.wreulicke.another to module com.github.wreulicke
# >>>  at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
# >>>	 at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:589)
# >>>	 at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:479)
# >>>	 at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:11)
# >>>	 at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:10)
# >>> test
# >>> com.github.wreulicke.another.AnotherWorld@96532d6
```

動きました。

また、似たようなotherパッケージを用意して
実行してみます。
今回変更したのは module-info.java がメインです。
module-info に依存関係を書いておくとロードされてオプションなしで起動できるようになります。
Java9からはリフレクションはできるだけ避けたいところですね。

```bash
java -p mod -m com.github.wreulicke.other/com.github.wreulicke.other.MainClass
# >>> error occured
# >>> java.lang.IllegalAccessException: class com.github.wreulicke.HelloWorld (in module com.github.wreulicke) cannot access class com.github.wreulicke.other.MainClass (in module com.github.wreulicke.other) because module com.github.wreulicke.other does not export com.github.wreulicke.other to module com.github.wreulicke
# >>> 	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
# >>> 	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:589)
# >>> 	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:479)
# >>> 	at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:11)
# >>> 	at com.github.wreulicke.other/com.github.wreulicke.other.MainClass.main(MainClass.java:10)
# >>> test
# >>> com.github.wreulicke.other.MainClass@71be98f5
```

## コラム jlinkを使ってカスタムの起動イメージを作る

次の例はoutputフォルダにカスタムの実行イメージが作成されます。
`--add-modules`でカスタムのイメージで必要なmainモジュールを追加すると
mainモジュールに必要なカスタムモジュールが作成可能です。

```
jlink --compress=1 -p $JAVA_HOME/jmods:mod --add-modules com.github.wreulicke.main --output output
```

さて、実行して見ましょう。

outputモジュールにカスタムイメージが配置されているので、普通にjavaを使う感覚で実行可能です。
bin/javaにファイルが配置されています。

上で使ったコマンドのオプションを追加して実行してみます。

```bash
output/bin/java --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke.main --add-modules com.github.wreulicke.another -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> output/bin/java --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke.main --add-modules com.github.wreulicke.another -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> Error occurred during initialization of boot layer
# >>> java.lang.module.FindException: Module com.github.wreulicke.another not found
```

もちろん、動きません。
今回のモジュールでは、anotherモジュールをランタイムに参照しているわけですが
mainモジュールのmodule-infoには依存関係が記述されていないので、カスタムイメージには含まれていません。

`--list-modules` オプションを使ってイメージに含まれているモジュールを表示してみましょう。
anotherモジュールが含まれていないことがわかります。

```bash
output/bin/java --list-modules
# >>> com.github.wreulicke
# >>> com.github.wreulicke.main
# >>> java.base@9-Debian
```

カスタムイメージであっても、モジュールパスを追加すると起動することが可能です。
動きました。

```bash
output/bin/java -p mod --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke.main --add-modules com.github.wreulicke.another -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> 	error occured
# >>> 	java.lang.IllegalAccessException: class com.github.wreulicke.HelloWorld (in module com.github.wreulicke) cannot access class com.github.wreulicke.another.AnotherWorld (in module com.github.wreulicke.another) because module com.github.wreulicke.another does not export com.github.wreulicke.another to module com.github.wreulicke
# >>> 		at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
# >>> 		at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:589)
# >>> 		at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:479)
# >>> 		at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:11)
# >>> 		at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:10)
# >>> 	test
# >>> 	com.github.wreulicke.another.AnotherWorld@6108b2d7
```

さて、では、ランタイムにanotherモジュールを含ませるには、どうすれば良いでしょうか。
`--add-modules` オプションにカンマ区切りで記述することが出来ます。
outputフォルダを `rm -rf output` で削除して
再度カスタムイメージを作って実行して見ましょう。

結果は先ほどと同じ出力がされており、期待通りです。

```bash
jlink --compress=1 -p $JAVA_HOME/jmods:mod --add-modules com.github.wreulicke.another,com.github.wreulicke.main --output output
output/bin/java --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke.main --add-modules com.github.wreulicke.another -m com.github.wreulicke.main/com.github.wreulicke.main.MainClass
# >>> error occured
# >>> java.lang.IllegalAccessException: class com.github.wreulicke.HelloWorld (in module com.github.wreulicke) cannot access class com.github.wreulicke.another.AnotherWorld (in module com.github.wreulicke.another) because module com.github.wreulicke.another does not export com.github.wreulicke.another to module com.github.wreulicke
# >>> 	at java.base/jdk.internal.reflect.Reflection.newIllegalAccessException(Reflection.java:361)
# >>> 	at java.base/java.lang.reflect.AccessibleObject.checkAccess(AccessibleObject.java:589)
# >>> 	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:479)
# >>> 	at com.github.wreulicke/com.github.wreulicke.HelloWorld.hello(HelloWorld.java:11)
# >>> 	at com.github.wreulicke.main/com.github.wreulicke.main.MainClass.main(MainClass.java:10)
# >>> test
# >>> com.github.wreulicke.another.AnotherWorld@29444d75
```
