package orchestration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import events.AbstractEvent;
import events.EventFactory;
import events.EventMessage;
import events.EventType;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelDiscovery;
import pubSubServer.SubscriptionManager;
import publishers.AbstractPublisher;
import publishers.PublisherFactory;
import publishers.PublisherType;
import states.subscriber.StateName;
import strategies.publisher.StrategyName;
import subscribers.AbstractSubscriber;
import subscribers.SubscriberFactory;
import subscribers.SubscriberType;

public class Orchestration {
	protected static Map<Integer, AbstractPublisher> publisherMap = new HashMap<Integer, AbstractPublisher>();
	protected static Map<Integer, AbstractSubscriber> subscriberMap = new HashMap<Integer, AbstractSubscriber>();
	protected static Integer subID = 0, pubID = 0;

	public static void main(String[] args) {

		List<AbstractPublisher> listOfPublishers = new ArrayList<>();
		List<AbstractSubscriber> listOfSubscribers = new ArrayList<>();
		Orchestration testHarness = new Orchestration();
		try {
			listOfPublishers = testHarness.createPublishers();
			listOfSubscribers = testHarness.createSubscribers();
			//System.out.println("list of publisher: " + listOfPublishers);
			//System.out.println("list of sub: " + listOfSubscribers);
			List<AbstractChannel> channels = ChannelDiscovery.getInstance().listChannels();
			//For demonstration purposes only
			try {
				BufferedReader initialChannels = new BufferedReader(new FileReader(new File("Channels.chl")));
				List<String> channelList = new ArrayList<String>();
				String line = "";
				while((line = initialChannels.readLine()) != null )
				{			
					channelList.add(line);
				}
				int subscriberIndex = 0;
				for(AbstractSubscriber subscriber : listOfSubscribers) {
					subscriber.subscribe(channelList.get(subscriberIndex%channelList.size()));
					//String channelName = channelList.get(subscriberIndex%channelList.size());
					// test on subscribing
					//System.out.println("Channel name: "+channelName + " Subs name: " + subscriber);
					//subscriber.subscribe(channelName);
					subscriberIndex++;
				}
				initialChannels.close();
			}catch(IOException ioe) {
				System.out.println("Loading Channels from file failed proceeding with random selection");
				for(AbstractSubscriber subscriber : listOfSubscribers) {
					int index = (int) Math.round((Math.random()*10))/3;
					SubscriptionManager.getInstance().subscribe(channels.get(index).getChannelTopic(), subscriber);
				}
			}
			for(AbstractPublisher publisher : listOfPublishers) {
				publisher.publish();
			}

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			System.out.println("Will now terminate");
			return;
		}
		for(AbstractPublisher publisher : listOfPublishers) {
			publisher.publish();
		}

	/**
	 * Driving the program test	
	 * @author H. Ye
	 */
		System.out.println("\n\n\n\n\n\n\nTest for driving the program: ");
		try {
			BufferedReader PathReader = new BufferedReader(new FileReader(new File("path.txt")));
			while(PathReader.ready()) {
				System.out.println("\n*****************************************************");
				String PathConfigLine = PathReader.readLine();
				String[] PathConfigArray = PathConfigLine.split(" ");
				
				if (PathConfigArray[0].equals("SUB")) {
					subscriberMap.get(Integer.parseInt(PathConfigArray[1])).subscribe(PathConfigArray[2]);
					// can also do printing in SubscriptionManager where declare subscribe() to make sure done subscribing
					System.out.println("main(), SubscriberID: " + PathConfigArray[1] + " has subscribed channel: " + PathConfigArray[2]);
				}
				
				else if (PathConfigArray[0].equals("PUB")) {
					if (PathConfigArray.length == 2) {
						publisherMap.get(Integer.parseInt(PathConfigArray[1])).publish();
						System.out.println("main(), default pub"); 
					}					
					else if (PathConfigArray.length == 5) {						
						//String eType;
						EventMessage eMsg = new EventMessage(PathConfigArray[3],PathConfigArray[4]);
						EventType e = null;
						switch(PathConfigArray[2]) {
						case "TypeA": 
							e = EventType.TypeA;
							break;//PathConfigArray[2].replaceAll("","TypeA");
						case "TypeB": 
							e = EventType.TypeB;
							break;//PathConfigArray[2].replaceAll("","TypeB");
						case "TypeC": 
							e = EventType.TypeC;
							break;
						default: e = EventType.TypeC; break;
						}								
						
						AbstractEvent newEvent = EventFactory.createEvent(e, Integer.parseInt(PathConfigArray[1]), eMsg);
												
						publisherMap.get(Integer.parseInt(PathConfigArray[1])).publish(newEvent);
						System.out.println("main(), pub with type " + PathConfigArray[2]);
					}
					else						
						System.out.println("Wrong input in path.txt file");
				}
				
				else if (PathConfigArray[0].equals("BLOCK")) {
					SubscriptionManager m = SubscriptionManager.getInstance();
					m.unSubscribe(PathConfigArray[2], subscriberMap.get(Integer.parseInt(PathConfigArray[1])));
					System.out.println("Subscriber with SubID: " + PathConfigArray[1] + " blocked the channel " + PathConfigArray[2]);
				}
				
				else if (PathConfigArray[0].equals("UNBLOCK")) {
					SubscriptionManager m = SubscriptionManager.getInstance();
					m.subscribe(PathConfigArray[2], subscriberMap.get(Integer.parseInt(PathConfigArray[1])));
					System.out.println("Subscriber with SubID: " + PathConfigArray[1] + " unblocked the channel " + PathConfigArray[2]);
				}
				
				else
					System.out.println("unexpected input in path.txt.");
			}
			PathReader.close();
		}catch(IOException ioe) {
			System.out.println("Error when driving the program.");
		}
		
	}


	private List<AbstractPublisher> createPublishers() throws IOException{
		//Integer pubCounter = 0;
		List<AbstractPublisher> listOfPublishers = new ArrayList<>();
		AbstractPublisher newPub;
		BufferedReader StrategyBufferedReader = new BufferedReader(new FileReader(new File("Strategies.str")));
		
		while(StrategyBufferedReader.ready()) {
			String PublisherConfigLine = StrategyBufferedReader.readLine();
			String[] PublisherConfigArray = PublisherConfigLine.split("\t");
			int[] PublisherConfigIntArray = new int[2];
			for(int i = 0; i < PublisherConfigArray.length; i++)
				PublisherConfigIntArray[i] = Integer.parseInt(PublisherConfigArray[i]);

			newPub = PublisherFactory.createPublisher(
					PublisherType.values()[PublisherConfigIntArray[0]],
					StrategyName.values()[PublisherConfigIntArray[1]]);

			listOfPublishers.add(newPub);
			publisherMap.put(pubID, newPub);
			pubID++;
		}
		StrategyBufferedReader.close();
		return listOfPublishers;
	}

	private List<AbstractSubscriber> createSubscribers() throws IOException{

		List<AbstractSubscriber> listOfSubscribers = new ArrayList<>();
		AbstractSubscriber newSub;
		BufferedReader StateBufferedReader = new BufferedReader(new FileReader(new File("States.sts")));
		
		while(StateBufferedReader.ready()) {
			String StateConfigLine = StateBufferedReader.readLine();
			String[] StateConfigArray = StateConfigLine.split("\t");
			int[] StateConfigIntArray = new int[2];
			for(int i = 0; i < StateConfigArray.length; i++)
				StateConfigIntArray[i] = Integer.parseInt(StateConfigArray[i]);

			newSub = SubscriberFactory.createSubscriber(
					SubscriberType.values()[StateConfigIntArray[0]], 
					StateName.values()[StateConfigIntArray[1]]);
			// 
			// System.out.println("sub type: " + SubscriberType.values()[StateConfigIntArray[0]] + " state value: " + StateName.values()[StateConfigIntArray[1]]);
			//System.out.println("newsub = " + newSub);
			listOfSubscribers.add(newSub);
			subscriberMap.put(subID, newSub);
			subID++;
		}
		StateBufferedReader.close();
		return listOfSubscribers;
	}



}