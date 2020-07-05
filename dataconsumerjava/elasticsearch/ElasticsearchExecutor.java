package dataconsumerjava.elasticsearch;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorRecordProcessorFactory;
import com.amazonaws.services.kinesis.connectors.elasticsearch.ElasticsearchObject;
import dataconsumerjava.KinesisConnectorExecutor;
import dataconsumerjava.News;

public class ElasticsearchExecutor extends KinesisConnectorExecutor<News, ElasticsearchObject> {

  private static String configFile = "ElasticSearch.properties";

  public ElasticsearchExecutor(String configFile) {
    super(configFile);
  }

  @Override
  public KinesisConnectorRecordProcessorFactory<News, ElasticsearchObject>
      getKinesisConnectorRecordProcessorFactory() {
    return new KinesisConnectorRecordProcessorFactory<News, ElasticsearchObject>(
        new ElasticsearchPipeline(), config);
  }

  public static void main(String[] args) {
    try {
      Class.forName("org.elasticsearch.client.transport.TransportClient");
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Could not load elastic search jar", e);
    }
    try {

      Class.forName("org.apache.lucene.util.Version");
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Could not load Apache Lucene jar", e);
    }

    KinesisConnectorExecutor<News, ElasticsearchObject> elasticsearchExecutor =
        new ElasticsearchExecutor(configFile);
    elasticsearchExecutor.run();
  }
}
