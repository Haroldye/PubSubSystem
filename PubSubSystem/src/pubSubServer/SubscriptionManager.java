package pubSubServer;

import java.util.HashMap;
import java.util.Map;

import subscribers.AbstractSubscriber;
/**
 * @author kkontog, ktsiouni, mgrigori
 * MUST IMPLEMENT the Singleton Design Pattern and 
 * already implements the Proxy Design Pattern
 *  exposes the subscribe, and unsubscribe methods to the clients 
 */
/**
 * 
 * @author Howar Ye
 * Complete the singleton class
 * complete subscription and unsubscription
 */
public class SubscriptionManager {
	private static SubscriptionManager instance = null;
	private ChannelPoolManager cpManager;
	//private Map<AbstractSubscriber, String> subChannelMap = new HashMap<AbstractSubscriber, String>();
	//private List<AbstractSubscriber> subscriberlList = new ArrayList<AbstractSubscriber>();
	
	private SubscriptionManager() {
		cpManager = ChannelPoolManager.getInstance();
		/*
		String defaultChannel = "main";
		AbstractSubscriber defaultSub;
		defaultSub = SubscriberFactory.createSubscriber(
				SubscriberType.values()[0], 
				StateName.values()[0]);
		subChannelMap.put(defaultSub, defaultChannel);*/
	}
	
	public static SubscriptionManager getInstance() {
		if (instance == null)
			instance = new SubscriptionManager();
		return instance;
	}
	

	/**
	 * Completes the subscription of the provided ISubscriber to the appropriate AbstractChannel specified by the channelName
	 * @param channelName the name of the AbstractChannel to which the ISubscriber wants to subscribe
	 * @param subscriber the reference to an ISubscriber object
	 */
	public void subscribe(String channelName, AbstractSubscriber subscriber) {
			
		AbstractChannel channel = cpManager.findChannel(channelName);
		channel.subscribe(subscriber);
		//if (channel != null)
			//subChannelMap.put(subscriber, channelName);
		//System.out.println("Subscriber has subscribed channel " + channelName);
	}
	
	/**
	 * Completes the unSubscription of the provided ISubscriber from the specified, by the channelName, AbstractChannel
	 * @param channelName the name of the AbstractChannel from which the ISubscriber wants to unsubscribe
	 * @param subscriber the reference to an ISubscriber object
	 */
	public void unSubscribe(String channelName, AbstractSubscriber subscriber) {
		
		AbstractChannel channel = cpManager.findChannel(channelName);
		channel.unsubscribe(subscriber);
		//if (channel != null)
			//subChannelMap.remove(subscriber, channelName);
		//System.out.println("Subscriber has unSubscribed channel " + channelName);
		
	}
	
	
}
