#### Overview

Java Fire is a event framework and platform, providing:

* Publish-subscribe: publish (fire) events and have them delivered and processed by registered listeners;
* Transactionality: events are fired only within a transaction, and aren't delivered by listeners until the transaction is committed.

More information can be found at http://emersonloureiro.github.io/java-fire.

Latest stable release is 1.0.0-rc1. You can download it [here](https://github.com/emersonloureiro/java-fire/releases/download/v1.0.0-rc1/java-fire-core-1.0.0-rc1.jar).

#### Generating Binaries

The minimum requirements are:

* Java 6
* Maven 3.0.4

Once you've checked out the source code, open a command line prompt and run a "mvn clean install". This should get you the binaries and source on you local repository. A "mvn deploy" should let you deploy to your remote repository.
