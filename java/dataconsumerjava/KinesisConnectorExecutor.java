package dataconsumerjava;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorExecutorBase;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorRecordProcessorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class KinesisConnectorExecutor<T, U> extends KinesisConnectorExecutorBase<T, U> {
  private static final Log LOG = LogFactory.getLog(KinesisConnectorExecutor.class);

  // Elasticsearch Cluster Resource constants
  private static final String EC2_ELASTICSEARCH_FILTER_NAME = "tag:type";
  private static final String EC2_ELASTICSEARCH_FILTER_VALUE = "datascience-local-news";

  // Create Stream Source constants
  private static final String CREATE_STREAM_SOURCE = "createStreamSource";
  private static final String LOOP_OVER_STREAM_SOURCE = "loopOverStreamSource";
  private static final boolean DEFAULT_CREATE_STREAM_SOURCE = false;
  private static final boolean DEFAULT_LOOP_OVER_STREAM_SOURCE = false;

  protected final KinesisConnectorConfiguration config;
  private final Properties properties;

  public KinesisConnectorExecutor(String configFile) {
    InputStream configStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
    if (configStream == null) {
      String msg = "Could not find resource " + configFile + "in the classpath";
      throw new IllegalStateException(msg);
    }
    properties = new Properties();
    try {
      properties.load(configStream);
      configStream.close();
    } catch (IOException e) {
      String msg = "Could not find resource " + configFile + "in the classpath";
      throw new IllegalStateException(msg);
    }

    this.config = new KinesisConnectorConfiguration(properties, getAWSCredentialsProvider());
    super.initialize(config);
  }

  public AWSCredentialsProvider getAWSCredentialsProvider() {
    return new DefaultAWSCredentialsProviderChain();
  }

  @Override
  public KinesisConnectorRecordProcessorFactory<T, U> getKinesisConnectorRecordProcessorFactory() {
    return null;
  }
}
