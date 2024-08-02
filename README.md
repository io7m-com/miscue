miscue
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.miscue/com.io7m.miscue.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.miscue%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.miscue/com.io7m.miscue?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/miscue/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/miscue.svg?style=flat-square)](https://codecov.io/gh/io7m-com/miscue)
![Java Version](https://img.shields.io/badge/21-java?label=java&color=e6c35c)

![com.io7m.miscue](./src/site/resources/miscue.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/miscue/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/miscue/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/miscue/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/miscue/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/miscue/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/miscue/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/miscue/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/miscue/actions?query=workflow%3Amain.windows.temurin.lts)|

## miscue

JavaFX component for displaying error dialogs.

## Features

* Display error dialogs containing [structured errors](https://www.io7m.com/software/seltzer).
* Written in pure Java 21.
* [OSGi-ready](https://www.osgi.org/)
* [JPMS-ready](https://en.wikipedia.org/wiki/Java_Platform_Module_System)
* ISC license.

## Usage

Given a [structured error](https://www.io7m.com/software/seltzer) value:

```
SStructuredErrorType<String> error;
```

Create a new dialog and display it:

```
MSErrorDialogs.builder(error)
  .build()
  .showAndWait();
```

The contents of the dialog depends on the data present in the error value.

If only a _message_ is present, the message will be displayed:

![Message Only](./src/site/resources/stage0.png?raw=true)

If _attributes_ are present, they will be displayed in a table below the
message:

![Attributes](./src/site/resources/stage1.png?raw=true)

If an _exception_ is present, it will be displayed:

![Exception](./src/site/resources/stage2.png?raw=true)

If a _remediating action_ is present, it will be displayed:

![Remediation](./src/site/resources/stage3.png?raw=true)

Any combination of the above is possible:

![All](./src/site/resources/stage4.png?raw=true)

### Reporting

If a _reporting callback_ is provided to the builder, it will be evaluated
when the user clicks the provided _Report_ button. If no callback is provided,
the _Report_ button is disabled. The callback should be tied to your
application's error reporting functionality.

```
MSErrorDialogs.builder(error)
  .setErrorReportCallback(() -> {
    LOG.debug("Pretending to send an error report...");
  })
  .build()
  .showAndWait();
```

### CSS

All elements within an error dialog are assigned CSS classes:

![CSS](./src/site/resources/css.png?raw=true)

A custom stylesheet can be specified to the dialog builder:

```
MSErrorDialogs.builder(error)
  .setCSS(URI.create("/path/to/custom.css"))
  .build()
  .showAndWait();
```

With great power comes great responsibility.

![CSS Power](./src/site/resources/stage5.png?raw=true)

## Demo

A [demo application](com.io7m.miscue.fx.demo) is included.


