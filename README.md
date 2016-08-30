# Java Fire

[![Coverage Status](https://coveralls.io/repos/jangasoft/java-fire/badge.png?branch=master)](https://coveralls.io/r/jangasoft/java-fire?branch=master)

Java Fire is a event framework and platform, providing:

* Publish-subscribe: publish (fire) events and have them delivered and processed by registered listeners;
* Transactionality: events are fired only within a transaction, and aren't delivered to listeners until the transaction is committed.

The latest stable release is 1.0.2. The easiest way to obtain it is through the [Central Repository](http://central.sonatype.org/). On Maven, simply add the dependency below to your project. Java Fire requires Java 6 or higher.

```xml
<dependency>
	<groupId>cf.janga</groupId>
	<artifactId>java-fire-core</artifactId>
	<version>1.0.2</version>
</dependency>
```

If you wish to download it manually, you can do so at [here](https://oss.sonatype.org/content/groups/public/cf/janga/java-fire-core/1.0.2/java-fire-core-1.0.2.jar).

## Using Java Fire

### First Things First
Before moving on, some basic concepts:

* Event: Anything that can be fired, e.g., a particular occurrence in the system;
* Handler: Handles fired events. For that, it must be registered against the event types it is interested in. From this point on, we'll use the term process an event to indicate an event being passed on to a handler;
* Event Bus: Where events are fired, and eventually delivered to registered handlers;
* Transaction: Events can only be fired and delivered to handlers within the scope of a transaction. You can start/commit/rollback a transaction, just like the database ones. Events won't be processed until a transaction is committed.

Most of the programming involved in Java Fire is around annotations. There's simply no interface to implement or abstract class to extend. 

### Defining Events
There are two main annotations for defining events `@Event` and `@EventKey`. `@Event` is a class-level annotation, and - as the name indicates! - it says that the annotated class is an event type (i.e., things that can be fired through the Event Bus). `@EventKey`, on the other hand, is a method-level annotation, and it's used to indicate the method from an event type that returns the event key, which is a what differentiates an event from others of the same type. These two annotations are the bare minimum required when defining an event. When a class is annotated with them, we say that it then refers to a valid event type. 

```java
@Event
public class SomeEvent {
    @EventKey
    public String getSomeProperty() {
      // ...
    }
}
```

Event processing in Java Fire - so far - happens only synchronously. This means that, once a transaction is committed, all fired events are processed within the same thread they were fired. Because events aren't processed by registered handlers until the end of a transaction, events representing the same change can be fired. It's because of this that the `@EventKey` annotation and the duplicateResolution value of the `@Event` annotation exist. Essentially, when two events having the same event key are fired within the same transaction, the value of duplicateResolution will determine which event gets to be processed at the end of the transaction. `DuplicateResolution.FIRST_WINS` means the first event fired gets to be processed, and all others with the same key will be ignored. `DuplicateResolution.LAST_WINS` is the opposite, so the last event fired always get to be processed, "overriding" previously fired ones with the same key. 

```java
@Event(duplicateResolution = DuplicateResolution.LAST_WINS)
public class LastWinsEvent {
}
...
@Event(duplicateResolution = DuplicateResolution.FIRST_WINS)
public class FirstWinsEvent {
}
```

### Defining Handlers

Unlike events, handlers have no need for class-level annotations. All it's needed is to annotate a method with the `@Handle` annotation. The only constraints in this sense are that: 1) the method has to be public - duh, 2) the method must return void, and 3) it must take a single argument, whose type is the class of the event that the method is supposed to handle. The class of the parameter has to, obviously, be properly annotated as an event type, or else the framework will throw an error when registering the handler. 

```java
public class SomeEventListener {
	@Handle
	public void handleSomeEvent(SomeEvent event) {
		// Do your stuff here!
		// ...
	}
	// These won't be valid handlers
	@Handle
	public void handleObjectEvent(Object object) {
		// ...
	}
	@Handle
	public String handleSomeOtherEvent(SomeOtherEvent event) {
		// ...
		return aString;
	}
	@Handle
	public void handleYetAnotherEvent(YetAnotherEvent event, Object object) {
		// Do your stuff here!
		// ...
	}
}
```

### Registering Handlers & Firing Events

The registration of handlers and firing of events is done through the Event Bus. To fire an event, one simply needs to pass down an instance of the event to be fired. The bus takes care of communicating with the current transaction to get that event processed when the time comes. The event instance provided to the bus must be of a class which is properly annotated as an event type - as discussed above - or else an exception is thrown. 

```java
EventBus.fire(new SampleEvent("eventkey"));
// This will throw an EventException
EventBus.fire(new Object());
```

For registering handlers, there are two ways. One can either provide an instance of a class annotated with handler methods, or the class itself. The difference there is that, in the first case, the instance is always used to process the events. This means that, thread-safety is left entirely to implementors. In the second case, on the other hand, a new instance of the class is automatically created by the framework, and then used to process the event. As with the firing of events, attempting to register an object/class not properly annotated as a handler will throw an exception. 

```java
EventBus.register(new SomeEventListener());
EventBus.register(SomeEventListener.class);

// Any of these will throw an EventException
EventBus.register(new Object());
EventBus.register(Object.class);
```

### Transactions

As said before, events can only be fired within the scope of a transaction, which means one needs to be started before anything else. There are two ways of doing so. One is manually, via the EventBus. Doing it that way means you receive a reference to the transaction, which you can then use to perform a commit or rollback.

```java
EventTransaction transaction = EventBus.startTransaction();
try {
	// Do stuff...
	...
	// Events are now processed and delivered
	// to registered handlers
	transaction.commit();
} catch (Exception e) {
	// All events fired within the transaction
	// are cleared and nothing gets processed
	transaction.rollback();
}
```

Another way to get a transaction started is automatically, when the "automatic transaction start" property is set to true - which is the default. What this means is that, whenever an event is fired, if a transaction doesn't exist for the current thread, one will be created and the event then fired. A reference to the transaction, in this case, can be obtained via the EventBus too. 

```java
try {
	// New transaction started automatically
	// here if one doesn't exist for the current
	// thread
	EventBus.fire(new SomeEvent());
	// A reference to the current transaction
	// can always be obtained for commits/rollbacks
	EventBus.getCurrentTransaction().commit();
} catch (Exception e) {
	EventBus.getCurrentTransaction().rollback();
}
```

Given the above then, setting the "automatic transaction start" property to false and attempting to fire an event, without having manually started a transaction, will cause an exception to be thrown by the bus.

```java
EventBus.setAutomaticTransactionStart(false);
try {
	EventBus.fire(new SomeEvent());
	EventBus.getCurrentTransaction().commit();
} catch (EventException e) {
	// No transaction started automatically
	// here, so exception will be raised
} catch (Exception e) {
	EventBus.getCurrentTransaction().rollback();
}
```

## Contributing
The minimum requirements are:

* Java 6
* Maven 3.0.4

Once you've cloned the repository, open a command prompt and run a `mvn clean install` to make sure you're all sorted. Do your code changes and then submit a patch to any relevant project owner. Once approved, you're free to push.
