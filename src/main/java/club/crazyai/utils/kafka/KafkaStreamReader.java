package club.crazyai.utils.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaStreamReader {
	private static Logger log = LoggerFactory.getLogger(KafkaStreamReader.class);
	private String zkServers;	//形如ip:port,ip:port
	private String groupId;
	private Collection<String> topics;
	
	ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public KafkaStreamReader(Collection<String> topics, String groupId, String zkServers) {
		this.topics = topics;
		this.groupId = groupId;
		this.zkServers = zkServers;
	}
	
	public KafkaStreamReader(String topic, String groupId, String zkServers) {
		List<String> topics = new ArrayList<>();
		topics.add(topic);
		this.topics = topics;
		this.groupId = groupId;
		this.zkServers = zkServers;
	}
	
	public void start(KafkaConsumeCallback cb) {
		Properties properties = new Properties();
        properties.put("zookeeper.connect", zkServers);
        properties.put("group.id", groupId);
        properties.put("auto.offset.reset", "largest"); 
        ConsumerConnector consumer = Consumer
                .createJavaConsumerConnector(new ConsumerConfig(properties));
        
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        for (String topic : topics) {
        	topicCountMap.put(topic, 1);
        }
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        
        for (String topic : topics) {
        	threadPool.submit(new KafkaReader(consumer, messageStreams.get(topic).get(0), cb));
        }
	}
	
	/**
	 * 读某个topic的数据流
	 * @param stream 某个topic对应的流
	 * @param cb
	 * @return
	 */
	static class KafkaReader implements Runnable {
		ConsumerConnector consumer;
		KafkaStream<byte[], byte[]> stream;
		KafkaConsumeCallback cb;
		public KafkaReader(ConsumerConnector consumer, KafkaStream<byte[], byte[]> stream, KafkaConsumeCallback cb) {
			this.consumer = consumer;
			this.stream = stream;
			this.cb = cb;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
			        while (iterator.hasNext()) {
			            String message = new String(iterator.next().message());
			            while(!cb.process(message)) {
			            	log.error("kafka message read but process error, probably write redis error...");
			            	Thread.sleep(1000);
			            }
			            consumer.commitOffsets();
			        }
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		List<String> topics = Arrays.asList(new String[]{"topic_1", "topic_2"});
		String groupId = "test";
		String zkServers = "localhost:2181,localhost:2182,localhost:2183";
		KafkaStreamReader reader = new KafkaStreamReader(topics, groupId, zkServers);
		reader.start(new KafkaConsumeCallback() {
			
			@Override
			public boolean process(String msg) {
				System.out.println(msg);
				return true;
			}
		});
		
		System.out.println("started");
		while(true) {
			Thread.sleep(10000);
		}
	}
	
	
}
