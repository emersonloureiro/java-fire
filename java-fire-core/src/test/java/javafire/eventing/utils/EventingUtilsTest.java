package javafire.eventing.utils;

import javafire.annotations.Event;
import javafire.annotations.Event.DuplicateResolution;
import javafire.annotations.Event.EventType;
import javafire.annotations.EventKey;
import javafire.annotations.Handle;
import junit.framework.TestCase;

@SuppressWarnings("unused")
public class EventingUtilsTest extends TestCase {

	public void testGetEventAnnotation() {
		// Checking the default case
		Event eventAnnotation = EventingUtils.getEventAnnotation(SampleEventWithDefaultValues.class);
		assertEquals(eventAnnotation.type(), EventType.SYNCHRONOUS);
		assertEquals(eventAnnotation.duplicateResolution(), DuplicateResolution.FIRST_WINS);
		// Checking when parameters are defined in the
		// annotations
		eventAnnotation = EventingUtils.getEventAnnotation(SampleSynchronousEvent.class);
		assertEquals(eventAnnotation.type(), EventType.SYNCHRONOUS);
		assertEquals(eventAnnotation.duplicateResolution(), DuplicateResolution.LAST_WINS);
		// Checking when a class inherits from an annotated one,
		// we shouldn't inherit the annotations
		assertNull(EventingUtils.getEventAnnotation(SampleInheritedEventWithoutEventAnnotation.class));
		// Checking when inheriting from an annotated class, and also
		// defining the annotation on the subclass itself, which is fine
		assertNotNull(EventingUtils.getEventAnnotation(SampleInheritedEventWithEventAnnotation.class));
		// Checking for null input and class which isn't annotated, nor
		// does it have any superclass which is annotated
		assertNull(EventingUtils.getEventAnnotation(Object.class));
		assertNull(EventingUtils.getEventAnnotation(null));
	}

	public void testGetEventKeyMethod() {
		// Basic case, only one public method with a return type of
		// string and no parameters annotated with EventKey
		assertEquals(1, EventingUtils.getEventKeyMethods(SampleEventWithEventKeyMethod.class).size());
		// Checking when a class inherits from a class having an abstract
		// method which is annotated with EventKey. We don't inherit
		// the annotation in any case.
		Object event = new AbstractSampleEventWithEventKeyMethod() {
			@Override
			public String getAbstractKey() {
				return null;
			}
		};
		assertEquals(0, EventingUtils.getEventKeyMethods(event.getClass()).size());
		// This time the class that inherits from the class with
		// the abstract method annotated with EventKey, does annotate
		// the implementation of the method with, which is fine.
		event = new AbstractSampleEventWithEventKeyMethod() {
			@Override
			@EventKey
			public String getAbstractKey() {
				return null;
			}
		};
		assertEquals(1, EventingUtils.getEventKeyMethods(event.getClass()).size());
		// Checking that when annotated multiple times, we get all
		// methods (doesn't mean it's valid doing so!)
		assertEquals(2, EventingUtils.getEventKeyMethods(SampleEventWithMultipleEventKeyMethods.class).size());
		// Again checking that we don't inherit the EventKey annotation
		assertEquals(0, EventingUtils.getEventKeyMethods(SampleInheritedEventWithEventAnnotation.class).size());
		// Checking for when a sub-class overrides a non-abstract method
		// and then annotates that method with EventKey, which is fine.
		assertEquals(1, EventingUtils.getEventKeyMethods(SampleInheritedEventWithEventAnnotationAndEventKey.class).size());
		// Class with no EventKey annotation in any method
		assertEquals(0, EventingUtils.getEventKeyMethods(SampleEventWithDefaultValues.class).size());
		// Null-safe check
		assertEquals(0, EventingUtils.getEventKeyMethods(null).size());
	}

	public void testIsValidEventType() {
		// Invalid event type as the event key method
		// isn't defined
		assertFalse(EventingUtils.isValidEventType(SampleEventWithDefaultValues.class));
		// Valid event type, as both the Event and EventKey
		// are present
		assertTrue(EventingUtils.isValidEventType(SampleEventWithEventKeyMethod.class));
		// Class with two methods annotated with EventKey
		assertFalse(EventingUtils.isValidEventType(SampleEventWithMultipleEventKeyMethods.class));
		// Class annotated only with EventKey
		assertFalse(EventingUtils.isValidEventType(SampleClassWithOnlyEventKeyMethod.class));
		// Null-safe
		assertFalse(EventingUtils.isValidEventType(null));
	}

	public void testIsValidHandler() {
		// Basic cases, class with one or more methods annotated
		// with Handle
		assertTrue(EventingUtils.isValidHandler(SampleHandlerWithOneHandlerMethod.class));
		assertTrue(EventingUtils.isValidHandler(SampleHandlerWithMultipleHandlerMethods.class));
		// Method argument isn't a valid event type
		assertFalse(EventingUtils.isValidHandler(SampleHandlerWithInvalidEvent.class));
		// Class doesn't have any method annotated with Handler
		assertFalse(EventingUtils.isValidHandler(SampleHandlerWithNoHandleAnnotation.class));
		// A class that extends a class with methods annotated with Handler,
		// but the sub-class itself doesn't have any method annotated with it
		assertFalse(EventingUtils.isValidHandler(SampleInheritedHandler.class));
		// A class that extends a class with methods annotated with Handler,
		// and the sub-class does have methods annotated with it (which could
		// be a method that's being overridden
		assertTrue(EventingUtils.isValidHandler(SampleInheritedOverridesHandler.class));
		// Class with a method annotated with Handler, but
		// method has more than one parameter.
		assertFalse(EventingUtils.isValidHandler(SampleHandlerWithMethodHavingMoreThanOneParameter.class));
		// Class with a method annotated with Handler, but
		// method doesn't have a void return type
		assertFalse(EventingUtils.isValidHandler(SampleHandlerWithMethodHavingNonVoidReturnType.class));
	}

	// --------------------------------------------
	// Sample classes for testing handle-related annotations
	// --------------------------------------------

	private class SampleHandlerWithOneHandlerMethod {
		@Handle
		public void handleEvent(SampleEventWithEventKeyMethod event) {
		}
	}

	private class SampleHandlerWithMultipleHandlerMethods {
		@Handle
		public void handleEvent(SampleEventWithEventKeyMethod event) {
		}

		@Handle
		public void handleAnotherEvent(SampleEventWithEventKeyMethod event) {
		}
	}

	private class SampleHandlerWithInvalidEvent {
		@Handle
		public void handleEvent(SampleClassWithOnlyEventKeyMethod event) {
		}
	}

	private class SampleHandlerWithMethodHavingMoreThanOneParameter {
		@Handle
		public void handleAnotherEvent(SampleEventWithEventKeyMethod event, Object object) {
		}
	}

	private class SampleHandlerWithMethodHavingNonVoidReturnType {
		@Handle
		public String handleAnotherEvent(SampleEventWithEventKeyMethod event) {
			return null;
		}
	}

	private class SampleHandlerWithNoHandleAnnotation {
		public void handleEvent(SampleEventWithEventKeyMethod event) {
		}
	}

	private class SampleInheritedHandler extends SampleHandlerWithMultipleHandlerMethods {
	}

	private class SampleInheritedOverridesHandler extends SampleHandlerWithMultipleHandlerMethods {
		@Override
		@Handle
		public void handleEvent(SampleEventWithEventKeyMethod event) {
		}
	}

	// --------------------------------------------
	// Sample classes for testing event-related annotations
	// --------------------------------------------

	private class SampleClassWithOnlyEventKeyMethod {
		@EventKey
		public String getSomething() {
			return "";
		}
	}

	@Event
	private class SampleEventWithDefaultValues {
	}

	@Event(type = EventType.SYNCHRONOUS, duplicateResolution = DuplicateResolution.LAST_WINS)
	private class SampleSynchronousEvent {
	}

	@Event
	private static class SampleEventWithEventKeyMethod {
		@EventKey
		public String getKey() {
			return "";
		}

		// The methods below shouldn't be considered
		// as an "event key method"
		@EventKey
		public Long getKeyAsLong() {
			return 1l;
		}

		@EventKey
		public String getKeyAsLong(String someParameter) {
			return "";
		}

		public String getAnotherKey() {
			return "";
		}

		public static String getStaticKey() {
			return "";
		}

		private String getPrivateKey() {
			return "";
		}

		protected String getProtectedKey() {
			return "";
		}

		protected String getPackagePrivateKey() {
			return "";
		}

		public final String getFinalKey() {
			return "";
		}
	}

	@Event
	private abstract static class AbstractSampleEventWithEventKeyMethod {
		// This shouldn't be considered an
		// "event key method" as the annotation
		// has to be on the implementing class
		@EventKey
		public abstract String getAbstractKey();
	}

	@Event
	private class SampleEventWithMultipleEventKeyMethods {
		@EventKey
		public String getKey1() {
			return "";
		}

		@EventKey
		public String getKey2() {
			return "";
		}
	}

	private class SampleInheritedEventWithoutEventAnnotation extends SampleEventWithEventKeyMethod {
	}

	@Event
	private class SampleInheritedEventWithEventAnnotation extends SampleEventWithEventKeyMethod {
	}

	@Event
	private class SampleInheritedEventWithEventAnnotationAndEventKey extends SampleEventWithEventKeyMethod {
		@Override
		@EventKey
		public String getAnotherKey() {
			return "";
		}
	}
}
