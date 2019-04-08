package orchestration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import events.EventFactory;
import events.EventMessage;
import events.EventType;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelAccessControl;
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
	protected static Integer subCounter = 0, pubCounter = 0;

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

/*
		try {

			BufferedReader PathBufferedReader = new BufferedReader(new FileReader(new File("path.txt")));
			while(PathBufferedReader.ready()) {
				String PathConfigLine = PathBufferedReader.readLine();
				String[] PathConfigArray = PathConfigLine.split("\t");

				if (PathConfigArray[0].equals("SUB")) 
				{
					subscriberMap.get(PathConfigArray[1]).subscribe(PathConfigArray[2]);;
				}
				else if  (PathConfigArray[0].equals("PUB")) {
					if (PathConfigArray.length < 2) publisherMap.get(PathConfigArray[1]).publish();
					else if (PathConfigArray.length >= 2) {
						if (PathConfigArray[2] == "TypeA") publisherMap.get(PathConfigArray[1]).publish(EventFactory.createEvent(EventType.TypeA,  Integer.parseInt(PathConfigArray[1]), new EventMessage (PathConfigArray[3], PathConfigArray[4])));
						else if (PathConfigArray[2] == "TypeB") publisherMap.get(PathConfigArray[1]).publish(EventFactory.createEvent(EventType.TypeB,  Integer.parseInt(PathConfigArray[1]), new EventMessage (PathConfigArray[3], PathConfigArray[4])));
						else if (PathConfigArray[2] == "TypeC") publisherMap.get(PathConfigArray[1]).publish(EventFactory.createEvent(EventType.TypeC,  Integer.parseInt(PathConfigArray[1]), new EventMessage (PathConfigArray[3], PathConfigArray[4])));	
					}
				}
				//				else if (PathConfigArray[0].equals("BLOCK")) {
				//					if (PathConfigArray[1].equals('0')) ChannelAccessControl.blockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.alpha, StateName.astate)  , PathConfigArray[2]);
				//					else if (PathConfigArray[1].equals('1')) ChannelAccessControl.blockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.beta, StateName.bstate)  , PathConfigArray[2]);
				//					else if (PathConfigArray[1].equals('2')) ChannelAccessControl.blockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.gamma, StateName.cstate)  , PathConfigArray[2]);
				//					else ChannelAccessControl.blockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.alpha, StateName.defaultState), PathConfigArray[2]);
				//				}
				//				else if (PathConfigArray[0].equals("UNBLOCK")) {
				//					
				//					if (PathConfigArray[1].equals('0')) ChannelAccessControl.unBlockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.alpha, StateName.astate)  , PathConfigArray[2]);
				//					else if (PathConfigArray[1].equals('1')) ChannelAccessControl.unBlockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.beta, StateName.bstate)  , PathConfigArray[2]);
				//					else if (PathConfigArray[1].equals('2')) ChannelAccessControl.unBlockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.gamma, StateName.cstate)  , PathConfigArray[2]);
				//					else ChannelAccessControl.unBlockSubcriber(SubscriberFactory.createSubscriber(SubscriberType.alpha, StateName.defaultState), PathConfigArray[2]);				
				//					
				//				}
			}

			PathBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

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
			publisherMap.put(pubCounter, newPub);
			pubCounter++;
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
			subscriberMap.put(subCounter, newSub);
			subCounter++;
		}
		StateBufferedReader.close();
		return listOfSubscribers;
	}



}
