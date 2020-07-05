package dataconsumerjava;

import dataconsumerjava.reader.processor.NewsRecordProcessorFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import software.amazon.awssdk.core.SdkSystemSetting;
import software.amazon.awssdk.http.Protocol;
import software.amazon.awssdk.http.SdkHttpConfigurationOption;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClientBuilder;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClientBuilder;
import software.amazon.awssdk.utils.AttributeMap;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.metrics.MetricsLevel;
import software.amazon.kinesis.retrieval.polling.PollingConfig;

public class DataConsumerJavaApplication {
  private static final Log LOG = LogFactory.getLog(DataConsumerJavaApplication.class);

  private static final Logger ROOT_LOGGER = Logger.getLogger("");
  private static final Logger PROCESSOR_LOGGER =
      Logger.getLogger("dataconsumerjava.DataConsumerJavaApplication");

  private static void setLogLevels() {
    ROOT_LOGGER.setLevel(Level.INFO);
    PROCESSOR_LOGGER.setLevel(Level.INFO);
  }

  public static void main(String[] args) throws URISyntaxException {
    Map<String, String> env = System.getenv();

    BasicConfigurator.configure();

    setLogLevels();

    String applicationName = env.get("KINESIS_APPLICATION_NAME");
    String streamName = env.get("KINESIS_STREAM_NAME");
    String awsUrl = env.get("AWS_ENDPOINT_URL");
    Region region = Region.of(env.get("AWS_REGION"));

    if (region == null || streamName == null || streamName.isEmpty()) {
      LOG.error("Missing region or kinesis stream name");
      System.exit(1);
    }

    LOG.info(
        "----------------------------------------"
            + applicationName
            + "\t"
            + streamName
            + "\t"
            + region
            + "\t"
            + awsUrl
            + "\t"
            + "---------------------------------------------");
    // KinesisClient kinesisClient1 = KinesisClient.builder().region(region).build();
    // getNews(kinesisClient1, streamName);

    KinesisAsyncClientBuilder kinesisBuilder = KinesisAsyncClient.builder().region(region);
    DynamoDbAsyncClientBuilder dynamodbBuilder = DynamoDbAsyncClient.builder().region(region);
    CloudWatchAsyncClientBuilder cloudwatchBuilder = CloudWatchAsyncClient.builder().region(region);
    MetricsLevel metrics = MetricsLevel.DETAILED;

    if (awsUrl != null && !awsUrl.isEmpty()) {
      URI awsUri = new URI(awsUrl);
      SdkAsyncHttpClient nettyClient =
          NettyNioAsyncHttpClient.builder()
              .protocol(Protocol.HTTP1_1)
              .buildWithDefaults(
                  AttributeMap.builder()
                      .put(
                          SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES, java.lang.Boolean.TRUE)
                      .build());
      System.setProperty(SdkSystemSetting.CBOR_ENABLED.property(), "false");
      metrics = MetricsLevel.NONE;
      kinesisBuilder = kinesisBuilder.endpointOverride(awsUri).httpClient(nettyClient);
      dynamodbBuilder = dynamodbBuilder.endpointOverride(awsUri).httpClient(nettyClient);
      cloudwatchBuilder = cloudwatchBuilder.endpointOverride(awsUri).httpClient(nettyClient);
    }

    KinesisAsyncClient kinesisClient = kinesisBuilder.build();
    DynamoDbAsyncClient dynamoClient = dynamodbBuilder.build();
    CloudWatchAsyncClient cloudWatchClient = cloudwatchBuilder.build();
    NewsRecordProcessorFactory shardRecordProcessor = new NewsRecordProcessorFactory();
    ConfigsBuilder configsBuilder =
        new ConfigsBuilder(
            streamName,
            applicationName,
            kinesisClient,
            dynamoClient,
            cloudWatchClient,
            UUID.randomUUID().toString(),
            shardRecordProcessor);
    Scheduler scheduler =
        new Scheduler(
            configsBuilder.checkpointConfig(),
            configsBuilder.coordinatorConfig(),
            configsBuilder.leaseManagementConfig(),
            configsBuilder.lifecycleConfig(),
            configsBuilder.metricsConfig().metricsLevel(metrics),
            configsBuilder.processorConfig(),
            configsBuilder
                .retrievalConfig()
                .retrievalSpecificConfig(new PollingConfig(streamName, kinesisClient)));
    //    .initialPositionInStreamExtended(
    //            InitialPositionInStreamExtended.newInitialPosition(
    //                InitialPositionInStream.TRIM_HORIZON))
    int exitCode = 0;
    try {
      scheduler.run();
    } catch (Throwable t) {
      LOG.error("Caught throwable while processing data.", t);
      exitCode = 1;
    }
    System.exit(exitCode);
  }
}
