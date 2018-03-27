package club.crazyai.utils.kafka;

public interface KafkaConsumeCallback {
	
	public boolean process(String msg);

}
