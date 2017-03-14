/**
  * Copyright 2017 Hortonworks.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at

  *   http://www.apache.org/licenses/LICENSE-2.0

  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
 **/
package com.hortonworks.streamline.streams.catalog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hortonworks.streamline.storage.annotation.StorableEntity;
import org.apache.commons.lang3.StringUtils;
import com.hortonworks.registries.common.Schema;
import com.hortonworks.streamline.storage.PrimaryKey;
import com.hortonworks.streamline.storage.Storable;
import com.hortonworks.streamline.streams.layout.component.rule.action.Action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A branch rule as represented in the UI layout
 */
@StorableEntity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopologyBranchRule extends BaseTopologyRule {
    public static final String NAMESPACE = "topology_branchrule";

    public static final String ID = "id";
    public static final String TOPOLOGY_ID = "topologyId";
    public static final String VERSION_ID = "versionId";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STREAM = "stream";
    public static final String OUTPUT_STREAMS = "outputStreams";
    public static final String CONDITION = "condition";
    public static final String PARSED_RULE_STR = "parsedRuleStr";
    public static final String ACTIONS = "actions";

    private Long id;
    private Long topologyId;
    private Long versionId;
    private String name;
    private String description;
    /*
     * A branch info object will have either
     * 1. the input stream and
     * 2. the condition string
     *    its translated into a select * from <stream> where <condition>
     */
    private String stream;
    private String condition;
    private String parsedRuleStr;
    private List<Action> actions;
    private Long versionTimestamp;
    // optional list of output streams that this rule emits to
    private List<String> outputStreams;

    // for jackson
    public TopologyBranchRule() {
    }

    public TopologyBranchRule(TopologyBranchRule other) {
        setId(other.getId());
        setTopologyId(other.getTopologyId());
        setVersionId(other.getVersionId());
        setName(other.getName());
        setDescription(other.getDescription());
        setStream(other.getStream());
        setCondition(other.getCondition());
        setParsedRuleStr(other.getParsedRuleStr());
        if (other.getActions() != null) {
            setActions(other.getActions().stream().map(Action::copy).collect(Collectors.toList()));
        }
        if (other.getOutputStreams() != null) {
            setOutputStreams(new ArrayList<>(other.getOutputStreams()));
        }
        setVersionTimestamp(other.getVersionTimestamp());
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("timestamp")
    public Long getVersionTimestamp() {
        return versionTimestamp;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("timestamp")
    public void setVersionTimestamp(Long versionTimestamp) {
        this.versionTimestamp = versionTimestamp;
    }

    @JsonIgnore
    @Override
    public PrimaryKey getPrimaryKey() {
        Map<Schema.Field, Object> fieldToObjectMap = new HashMap<>();
        fieldToObjectMap.put(new Schema.Field(ID, Schema.Type.LONG), this.id);
        fieldToObjectMap.put(new Schema.Field(VERSION_ID, Schema.Type.LONG), this.versionId);
        return new PrimaryKey(fieldToObjectMap);
    }

    @JsonIgnore
    @Override
    public String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTopologyId() {
        return topologyId;
    }

    public void setTopologyId(Long topologyId) {
        this.topologyId = topologyId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public List<String> getOutputStreams() {
        return outputStreams;
    }

    public void setOutputStreams(List<String> outputStreams) {
        this.outputStreams = outputStreams;
    }

    @JsonIgnore
    @Override
    public String getParsedRuleStr() {
        return parsedRuleStr;
    }

    @JsonIgnore
    public void setParsedRuleStr(String parsedRuleStr) {
        this.parsedRuleStr = parsedRuleStr;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @JsonIgnore
    @Override
    public Schema getSchema() {
        return Schema.of(
                Schema.Field.of(ID, Schema.Type.LONG),
                Schema.Field.of(VERSION_ID, Schema.Type.LONG),
                Schema.Field.of(TOPOLOGY_ID, Schema.Type.LONG),
                Schema.Field.of(NAME, Schema.Type.STRING),
                Schema.Field.of(DESCRIPTION, Schema.Type.STRING),
                Schema.Field.of(STREAM, Schema.Type.STRING),
                Schema.Field.of(OUTPUT_STREAMS, Schema.Type.STRING),
                Schema.Field.of(CONDITION, Schema.Type.STRING),
                Schema.Field.of(PARSED_RULE_STR, Schema.Type.STRING),
                Schema.Field.of(ACTIONS, Schema.Type.STRING)
        );
    }

    @Override
    public Map<String, Object> toMap() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = super.toMap();
        try {
            map.put(OUTPUT_STREAMS, outputStreams != null ? mapper.writeValueAsString(outputStreams) : "");
            map.put(ACTIONS, actions != null ? mapper.writerFor(new TypeReference<List<Action>>() {
            }).writeValueAsString(actions) : "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Storable fromMap(Map<String, Object> map) {
        setId((Long) map.get(ID));
        setVersionId((Long) map.get(VERSION_ID));
        setTopologyId((Long) map.get(TOPOLOGY_ID));
        setName((String) map.get(NAME));
        setDescription((String) map.get(DESCRIPTION));
        setCondition((String) map.get(CONDITION));
        setParsedRuleStr((String) map.get(PARSED_RULE_STR));
        setStream((String) map.get(STREAM));
        try {
            ObjectMapper mapper = new ObjectMapper();
            String outputStreamsStr = (String) map.get(OUTPUT_STREAMS);
            if (!StringUtils.isEmpty(outputStreamsStr)) {
                List<String> outputStreams = mapper.readValue(outputStreamsStr, new TypeReference<List<String>>() {
                });
                setOutputStreams(outputStreams);
            }
            String actionsStr = (String) map.get(ACTIONS);
            if (!StringUtils.isEmpty(actionsStr)) {
                List<Action> actions = mapper.readValue(actionsStr, new TypeReference<List<Action>>() {
                });
                setActions(actions);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopologyBranchRule that = (TopologyBranchRule) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return versionId != null ? versionId.equals(that.versionId) : that.versionId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (versionId != null ? versionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BranchRuleInfo{" +
                "id=" + id +
                ", topologyId=" + topologyId +
                ", versionId=" + versionId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stream='" + stream + '\'' +
                ", condition='" + condition + '\'' +
                ", parsedRuleStr='" + parsedRuleStr + '\'' +
                ", actions=" + actions +
                ", outputStreams=" + outputStreams +
                "} " + super.toString();
    }
}