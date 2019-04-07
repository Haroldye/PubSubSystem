package subscribers;

import states.subscriber.StateFactory;
import states.subscriber.StateName;


/**
 * @author kkontog, ktsiouni, mgrigori; Howar Ye
 *  
 */
/**
 * @author kkontog, ktsiouni, mgrigori
 * creates new {@link AbstractSubscriber} objects
 * contributes to the State design pattern
 * implements the FactoryMethod design pattern   
 */
/**
 * @author H.Ye
 * implement the full code of createSubscriber
 */
public class SubscriberFactory {

	
	/**
	 * creates a new {@link AbstractSubscriber} using an entry from the {@link SubscriberType} enumeration
	 * @param subscriberType a value from the {@link SubscriberType} enumeration specifying the type of Subscriber to be created 
	 * @return the newly created {@link AbstractSubscriber} instance 
	 */
	public static AbstractSubscriber createSubscriber(SubscriberType subscriberType, StateName stateName) {
		AbstractSubscriber CSA = null;
		// type alpha, beta, gamma
		switch (subscriberType) {
			case alpha : 
				CSA = new ConcreteSubscriberA();
				CSA.setState(stateName);
				return CSA;
			case beta :
				CSA = new ConcreteSubscriberA();
				CSA.setState(stateName);
				return CSA;
			case gamma :
				CSA = new ConcreteSubscriberA();
				CSA.setState(stateName);
				return CSA;
			default:
				CSA = new ConcreteSubscriberA();
				CSA.setState(stateName);
				return CSA;
		}
	}
	
}
