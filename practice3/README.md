# モジュールのパッケージ

今回のファイルパス構成は最終的に以下のような構成になります。
省略しているところが存在します。

```
.
├── mlib
│   ├── com.github.wreulicke.another@1.0.jar
│   └── com.github.wreulicke.jar
├── mod
│   ├── com.github.wreulicke
│   └── com.github.wreulicke.another
└── src
    ├── com.github.wreulicke
    │   ├── com
    │   │   └── github
    │   │       └── wreulicke
    │   │           └── HelloWorld.java
    │   └── module-info.java
    └── com.github.wreulicke.another
        ├── com
        │   └── github
        │       └── wreulicke
        │           └── another
        │               └── AnotherWorld.java
        └── module-info.java
```

## コンパイルとパッケージ

anotherモジュールをコンパイルしてパッケージします。

```
javac -d mod/com.github.wreulicke.another $(find src/com.github.wreulicke.another/ -name *.java)
jar --create --file=mlib/com.github.wreulicke.another@1.0.jar --module-version=1.0 -C mod/com.github.wreulicke.another .
```

同じようにmainメソッドのあるモジュールをコンパイルしてパッケージします。

```
javac -p mod -d mod/com.github.wreulicke $(find src/com.github.wreulicke -name *.java)
jar --create --file=mlib/com.github.wreulicke.jar --main-class=com.github.wreulicke.HelloWorld -C mod/com.github.wreulicke .
```

## 実行

実行してみます。

```bash
java -p mlib -m com.github.wreulicke
# >>> Hello Another World!!
```