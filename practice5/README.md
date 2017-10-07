# 他のモジュールをリフレクションする

今回はReflectionを使って遊んでいきます。
今回はanotherモジュールにいくつかのメンバーを用意してみます。

## コンパイル

各モジュールをコンパイルしてパッケージします。

```
javac -d mod --module-source-path src $(find src -name "*.java")
jar --create --file=mlib/com.github.wreulicke.another.jar -C mod/com.github.wreulicke.another .
jar --create --file=mlib/com.github.wreulicke.jar --main-class=com.github.wreulicke.HelloWorld -C mod/com.github.wreulicke .
```

## 実行

実行してみます。

```bash
java -p mlib -m com.github.wreulicke
# >>>	Hello Another World!!
# >>>	publicStaticField
# >>>	publicField
# >>>	publicStaticMethod
# >>>	publicMethod
# >>>	publicStaticMethod
# >>>	publicMethod
# >>>	Exception in thread "main" java.lang.reflect.InaccessibleObjectException: Unable to make private static java.lang.String com.github.wreulicke.another.AnotherWorld.privateStaticMethod() accessible: # >>>	module com.github.wreulicke.another does not "opens com.github.wreulicke.another" to module com.github.wreulicke
# >>>		at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:337)
# >>>   at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:281)
# >>>   at java.base/java.lang.reflect.Method.checkCanSetAccessible(Method.java:198)
# >>>   at java.base/java.lang.reflect.Method.setAccessible(Method.java:192)
# >>>   at com.github.wreulicke/com.github.wreulicke.HelloWorld.main(HelloWorld.
```

InaccessibleObjectExceptionで怒られてしまいました。まぁそりゃそうですね。

さて、どうしましょう。
こういう時は `--add-opens` オプションを使います。

指定
`<公開したいmodule>/<公開したいパッケージ>=<アクセスしているパッケージ>`
com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke

```bash
java --add-opens com.github.wreulicke.another/com.github.wreulicke.another=com.github.wreulicke -p mod -m com.github.wreulicke/com.github.wreulicke.HelloWorld
# >>> Hello Another World!!
# >>> publicStaticField
# >>> publicField
# >>> publicStaticMethod
# >>> publicMethod
# >>> publicStaticMethod
# >>> publicMethod
# >>> publicStaticMethod
# >>> privateMethod
```

今回の例では自分がモジュールを作成者なので
本来はopensディレクティブを使うかopenモジュールを使うことで
リフレクションできない制限を解除することができます（が、使わないと思います。）

以下のコメントのどちらかを使う形になるかと思います。

```java
// open module com.github.wreulicke.another {
module com.github.wreulicke.another {
  exports com.github.wreulicke.another;
  // opens com.github.wreulicke.another
}
```