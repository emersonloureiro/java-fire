# Java Fire

[![Build Status](https://travis-ci.org/jangasoft/java-fire.svg?branch=master)](https://travis-ci.org/jangasoft/java-fire) [![Coverage Status](https://coveralls.io/repos/jangasoft/java-fire/badge.png?branch=master)](https://coveralls.io/r/jangasoft/java-fire?branch=master) [![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/jangasoft/java-fire/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

Java Fire is a event framework and platform, providing:

* Publish-subscribe: publish (fire) events and have them delivered and processed by registered listeners;
* Transactionality: events are fired only within a transaction, and aren't delivered to listeners until the transaction is committed.

The latest stable release is 1.0.2. The easiest way to obtain it is through the [Central Repository](http://central.sonatype.org/). On Maven, simply add the dependency below to your project.

```xml
<dependency>
	<groupId>cf.janga</groupId>
	<artifactId>java-fire-core</artifactId>
	<version>1.0.2</version>
</dependency>
```

More information on the project can be found at http://jangasoft.github.io/java-fire.
