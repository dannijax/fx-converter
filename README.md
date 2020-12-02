# fx-converter
=======================

This  project for currency conversion and checking exchange rates

How to use
----------
+Clone the Project ```git clone https://github.com/dannijax/fx-converter.git```
+Import the project in Android Studio
+Get API key from [currencylayer](https://currencylayer.com/dashboard)
+Replace the Access_Key in the gradle.properties file
+Sync the project, Build and run the application



Static Analyzers
-----------------------

By default, build script will perform static code analysys and aggregate results. **Build task will fail if any analyzers report errors or warnings.** However, you can always choose not to use one or all of them.
To disable all analyzers, except Lint, remove following line from your build.gradle script:
```groovy
apply from: rootProject.file('gradle/check.gradle')
```

### Lint
http://tools.android.com/tips/lint

Lint configuration can be found in build.gradle file:
```groovy
lintOptions {
    abortOnError true
    checkAllWarnings true
    warningsAsErrors true
    disable 'AllowBackup', 'ContentDescription', 'InvalidPackage', 'SelectableText', 'SpUsage'
}
```
You can always suppress Lint warnings using @SuppressLint() annotation.
See http://tools.android.com/tips/lint/suppressing-lint-warnings for more details.

Results can be found in ./build/lint-results.html

### FindBugs
http://findbugs.sourceforge.net

FindBug configuration files can be found in ./gradle/config/findbugs folder.

To suppress FindBug warnings add following dependency to your module:
```groovy
compile 'com.google.code.findbugs:annotations:2.0.+'
```
You can then use @SuppressFBWarnings() annotation.

See http://findbugs.sourceforge.net/manual/index.html for more details.

Results can be found in ./build/reports/findbugs

### PMD
http://pmd.sourceforge.net

PMD configuration file can be found in ./gradle/config/pmd folder.

To suppress PMD warning you can use @SuppressWarnings("PMD[.%RULE%]") annotation.

See http://pmd.sourceforge.net/pmd-5.1.0/suppressing.html for more details.

Results can be found in ./build/reports/pmd

### CheckStyle
http://checkstyle.sourceforge.net

CheckStyle configuration files can be found in ./gradle/config/checkstyle folder.

You can disable check for some code fragment using following comments:
```java
//CHECKSTYLE:OFF
String s= "Some"+
"poorly "     +     "formatted"+" code";
//CHECKSTYLE:ON
```

Results can be found in ./build/reports/checkstyle

Details
----------------------

More in detail you can read about types of projects in readme files:

 + [android-simple-example](android-simple-example/README.md)
 + [android-extend-example](android-extended-example/README.md)
 + [android-sdk-manager-example](android-sdk-manager-example/README.md)

