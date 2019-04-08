package events;

import publishers.AbstractPublisher;


/**
 * @author kkontog, ktsiouni, mgrigori, 
 * @editedBy Syeda Nehal Hussain; 
 * @editedBy Howar
 *
 */
public class EventFactory {

	
	/**
	 * This is an implementation of the Factory Method design pattern
	 * Creates an instance of any of the subclasses derived from the top level class AbstractEvent
	 * 
	 * @param eventType a member of the {@link EventType} enumeration
	 * @param eventPublisherId a number generated by invoking the  {@link AbstractPublisher#hashCode()} on the {@link AbstractPublisher} instance issuing the event
	 * @param payload an object of type {@link EventMessage}
	 * @return
	 */
	public static AbstractEvent createEvent(EventType eventType, int eventPublisherId, EventMessage payload) {

		switch(eventType) {
		case TypeA:
			return new EventTypeA(EventIDMaker.getNewEventID(), eventPublisherId, payload);
		case TypeB:
			return new EventTypeB(EventIDMaker.getNewEventID(), eventPublisherId, payload);
		case TypeC:
			return new EventTypeC(EventIDMaker.getNewEventID(), eventPublisherId, payload);
		default :
			return new EventTypeC(EventIDMaker.getNewEventID(), eventPublisherId, payload);
		}
	}
	
}
