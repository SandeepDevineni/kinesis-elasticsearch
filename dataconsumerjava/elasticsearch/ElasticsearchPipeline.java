package dataconsumerjava.elasticsearch;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.elasticsearch.ElasticsearchEmitter;
import com.amazonaws.services.kinesis.connectors.elasticsearch.ElasticsearchObject;
import com.amazonaws.services.kinesis.connectors.impl.AllPassFilter;
import com.amazonaws.services.kinesis.connectors.impl.BasicMemoryBuffer;
import com.amazonaws.services.kinesis.connectors.interfaces.*;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataconsumerjava.News;
import java.io.IOException;

public class ElasticsearchPipeline implements IKinesisConnectorPipeline<News, ElasticsearchObject> {

  @Override
  public IEmitter<ElasticsearchObject> getEmitter(
      KinesisConnectorConfiguration kinesisConnectorConfiguration) {
    return new ElasticsearchEmitter(kinesisConnectorConfiguration);
  }

  @Override
  public IBuffer<News> getBuffer(KinesisConnectorConfiguration kinesisConnectorConfiguration) {
    return new BasicMemoryBuffer<>(kinesisConnectorConfiguration);
  }

  @Override
  public ITransformerBase<News, ElasticsearchObject> getTransformer(
      KinesisConnectorConfiguration kinesisConnectorConfiguration) {
    return new ITransformer<News, ElasticsearchObject>() {
      @Override
      public News toClass(Record record) throws IOException {
        try {
          return new ObjectMapper().readValue(record.getData().array(), News.class);
        } catch (IOException e) {
          String message =
              "Error parsing record from JSON: " + new String(record.getData().array());
          throw new IOException(message, e);
        }
      }

      @Override
      public ElasticsearchObject fromClass(News news) throws IOException {
        String index = "reuters-index";
        String type = news.getClass().getSimpleName();
        String id = "123";
        String source = null;
        try {

          source = new ObjectMapper().writeValueAsString(news);
        } catch (JsonProcessingException e) {
          String message = "Error parsing news to JSON";
          throw new IOException(message, e);
        }
        ElasticsearchObject elasticsearchObject = new ElasticsearchObject(index, type, id, source);
        elasticsearchObject.setCreate(true);
        return elasticsearchObject;
      }
    };
  }

  @Override
  public IFilter<News> getFilter(KinesisConnectorConfiguration kinesisConnectorConfiguration) {
    return new AllPassFilter<News>();
  }
}
