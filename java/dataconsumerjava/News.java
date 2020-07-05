package dataconsumerjava;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "title",
  "description",
  "summary",
  "date",
  "pubdate",
  "pubDate",
  "link",
  "guid",
  "author",
  "comments",
  "origlink",
  "image",
  "source",
  "categories",
  "enclosures"
})
public class News {

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  @JsonProperty("summary")
  private String summary;

  @JsonProperty("date")
  private String date;

  @JsonProperty("pubdate")
  private String pubdate;

  @JsonProperty("pubDate")
  private String pubDate;

  @JsonProperty("link")
  private String link;

  @JsonProperty("guid")
  private String guid;

  @JsonProperty("author")
  private Object author;

  @JsonProperty("comments")
  private Object comments;

  @JsonProperty("origlink")
  private Object origlink;

  @JsonProperty("image")
  private Image image;

  @JsonProperty("source")
  private Source source;

  @JsonProperty("categories")
  private List<Object> categories = null;

  @JsonProperty("enclosures")
  private List<Object> enclosures = null;

  @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  @JsonProperty("summary")
  public void setSummary(String summary) {
    this.summary = summary;
  }

  @JsonProperty("date")
  public String getDate() {
    return date;
  }

  @JsonProperty("date")
  public void setDate(String date) {
    this.date = date;
  }

  @JsonProperty("pubdate")
  public String getPubdate() {
    return pubdate;
  }

  @JsonProperty("pubdate")
  public void setPubdate(String pubdate) {
    this.pubdate = pubdate;
  }

  @JsonProperty("pubDate")
  public String getPubDate() {
    return pubDate;
  }

  @JsonProperty("pubDate")
  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }

  @JsonProperty("link")
  public String getLink() {
    return link;
  }

  @JsonProperty("link")
  public void setLink(String link) {
    this.link = link;
  }

  @JsonProperty("guid")
  public String getGuid() {
    return guid;
  }

  @JsonProperty("guid")
  public void setGuid(String guid) {
    this.guid = guid;
  }

  @JsonProperty("author")
  public Object getAuthor() {
    return author;
  }

  @JsonProperty("author")
  public void setAuthor(Object author) {
    this.author = author;
  }

  @JsonProperty("comments")
  public Object getComments() {
    return comments;
  }

  @JsonProperty("comments")
  public void setComments(Object comments) {
    this.comments = comments;
  }

  @JsonProperty("origlink")
  public Object getOriglink() {
    return origlink;
  }

  @JsonProperty("origlink")
  public void setOriglink(Object origlink) {
    this.origlink = origlink;
  }

  @JsonProperty("image")
  public Image getImage() {
    return image;
  }

  @JsonProperty("image")
  public void setImage(Image image) {
    this.image = image;
  }

  @JsonProperty("source")
  public Source getSource() {
    return source;
  }

  @JsonProperty("source")
  public void setSource(Source source) {
    this.source = source;
  }

  @JsonProperty("categories")
  public List<Object> getCategories() {
    return categories;
  }

  @JsonProperty("categories")
  public void setCategories(List<Object> categories) {
    this.categories = categories;
  }

  @JsonProperty("enclosures")
  public List<Object> getEnclosures() {
    return enclosures;
  }

  @JsonProperty("enclosures")
  public void setEnclosures(List<Object> enclosures) {
    this.enclosures = enclosures;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({})
class Image {

  @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({})
class Source {

  @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }
}
