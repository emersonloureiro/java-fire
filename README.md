## java-fire

Java Fire is a event framework and platform, providing:

* Publish-subscribe: publish (fire) events and have them delivered and processed by registered listeners;
* Transactionality: events are fired only within a transaction, and aren't delivered by listeners until the transaction is committed.

## Generating Binaries

The minimum requirements are:

* Java 6
* Maven 3.0.4

Once you've checked out the source code, open a command line prompt and run a "mvn clean install". This should get you the binaries and source on you local repository. A "mvn deploy" should let you deploy to your remote repository.
