package org.apache.streamline.streams.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.streamline.common.Schema;
import org.apache.streamline.storage.PrimaryKey;
import org.apache.streamline.storage.catalog.AbstractStorable;

import java.util.HashMap;
import java.util.Map;

/**
 * Virtual group of services which are from multiple clusters
 */
public class Namespace extends AbstractStorable {
  private static final String NAMESPACE = "namespaces";

  private Long id;
  private String name;
  private String streamingEngine;
  private String timeSeriesDB;
  private String description = "";
  private Long timestamp;

  @JsonIgnore
  @Override
  public String getNameSpace() {
    return NAMESPACE;
  }

  @JsonIgnore
  @Override
  public PrimaryKey getPrimaryKey() {
    Map<Schema.Field, Object> fieldToObjectMap = new HashMap<>();
    fieldToObjectMap.put(new Schema.Field("id", Schema.Type.LONG), this.id);
    return new PrimaryKey(fieldToObjectMap);
  }

  /**
   * The primary key
   */
  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * The name of the namespace
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The selected streaming engine of the namespace
   */
  public String getStreamingEngine() {
    return streamingEngine;
  }

  public void setStreamingEngine(String streamingEngine) {
    this.streamingEngine = streamingEngine;
  }

  /**
   * The selected Time-series DB of the namespace
   */
  public String getTimeSeriesDB() {
    return timeSeriesDB;
  }

  public void setTimeSeriesDB(String timeSeriesDB) {
    this.timeSeriesDB = timeSeriesDB;
  }

  /**
   * The namespace description (optional)
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Namespace)) return false;

    Namespace namespace = (Namespace) o;

    if (getId() != null ? !getId().equals(namespace.getId()) : namespace.getId() != null) return false;
    if (getName() != null ? !getName().equals(namespace.getName()) : namespace.getName() != null) return false;
    if (getStreamingEngine() != null ? !getStreamingEngine().equals(namespace.getStreamingEngine()) : namespace.getStreamingEngine() != null)
      return false;
    if (getTimeSeriesDB() != null ? !getTimeSeriesDB().equals(namespace.getTimeSeriesDB()) : namespace.getTimeSeriesDB() != null)
      return false;
    if (getDescription() != null ? !getDescription().equals(namespace.getDescription()) : namespace.getDescription() != null)
      return false;
    return getTimestamp() != null ? getTimestamp().equals(namespace.getTimestamp()) : namespace.getTimestamp() == null;

  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getStreamingEngine() != null ? getStreamingEngine().hashCode() : 0);
    result = 31 * result + (getTimeSeriesDB() != null ? getTimeSeriesDB().hashCode() : 0);
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Namespace{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", streamingEngine='" + streamingEngine + '\'' +
            ", timeSeriesDB='" + timeSeriesDB + '\'' +
            ", description='" + description + '\'' +
            ", timestamp=" + timestamp +
            '}';
  }
}