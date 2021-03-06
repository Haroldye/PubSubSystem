package strategies.publisher;

import java.util.ArrayList;
import java.util.List;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
import events.EventType;
import pubSubServer.ChannelEventDispatcher;

public class AStrategy implements IStrategy {

	public void doPublish(int publisherId) {
		List<String> listOfChannels = new ArrayList<String>();
		listOfChannels.add("cars");
		listOfChannels.add("planes");
		AbstractEvent event = EventFactory.createEvent(EventType.TypeA, publisherId, new EventMessage("h1", "b1")); 
		ChannelEventDispatcher.getInstance().postEvent(event, listOfChannels);
		System.out.println("Publisher with PubId: " + publisherId + " has published a default event," + event + " in As");
		
	}

	public void doPublish(AbstractEvent event, int publisherId) {
		List<String> listOfChannels = new ArrayList<String>();
		listOfChannels.add("cars");
		listOfChannels.add("planes");
		ChannelEventDispatcher.getInstance().postEvent(event, listOfChannels);
		System.out.println("Publisher with PubId: " + publisherId + " has published an event, " + event);
		
	}

}