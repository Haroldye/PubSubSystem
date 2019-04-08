package publishers;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
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
	
	public void publish(String type, EventMessage msg) {
		System.out.println("Eventtype: " + type + " and event msg " + msg + " has been published");
	}
}
