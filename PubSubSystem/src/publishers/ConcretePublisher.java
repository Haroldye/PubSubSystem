package publishers;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
import events.EventType;
import strategies.publisher.IStrategy;
import strategies.publisher.StrategyFactory;


/**
 * @author kkontog, ktsiouni, mgrigori
 * 
 * the WeatherPublisher class is an example of a ConcretePublisher 
 * implementing the IPublisher interface. Of course the publish 
 * methods could have far more interesting logics
 */
public class ConcretePublisher extends AbstractPublisher {

	
	
	
	
	/**
	 * @param concreteStrategy attaches a concreteStrategy generated from the {@link StrategyFactory#createStrategy(strategies.publisher.StrategyName)}
	 * method
	 */
	protected ConcretePublisher(IStrategy concreteStrategy) {
		this.publishingStrategy = concreteStrategy;
	}

	/* (non-Javadoc)
	 * @see publishers.IPublisher#publish(events.AbstractEvent)
	 */
	@Override
	public void publish(AbstractEvent event) {
		publishingStrategy.doPublish(event, this.hashCode());
	}

	/* (non-Javadoc)
	 * @see publishers.IPublisher#publish()
	 */
	@Override
	public void publish() {
		publishingStrategy.doPublish(this.hashCode());
	}
	/*
	public void publish(int pubID) {
		publishingStrategy.doPublish(pubID);
	}
	
	public void publish(String type, int pubID, EventMessage msg) {
		switch (type) {
		case "TypeA" : EventFactory.createEvent(EventType.TypeA, pubID, msg);
		case "TypeB" : EventFactory.createEvent(EventType.TypeB, pubID, msg);
		case "TypeC" : EventFactory.createEvent(EventType.TypeC, pubID, msg);
		default : EventFactory.createEvent(EventType.TypeA, pubID, msg);
		}
		
		System.out.println("publisher with pubID: " + pubID + " has published Eventtype: "  + type + " event and event msg " + msg);
	}*/
}
