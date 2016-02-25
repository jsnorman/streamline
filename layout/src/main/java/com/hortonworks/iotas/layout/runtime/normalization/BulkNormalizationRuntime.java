/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.hortonworks.iotas.layout.runtime.normalization;

import com.hortonworks.iotas.common.IotasEvent;
import com.hortonworks.iotas.common.Schema;
import com.hortonworks.iotas.layout.design.normalization.BulkNormalizationConfig;
import com.hortonworks.iotas.layout.runtime.script.GroovyScript;
import com.hortonworks.iotas.layout.runtime.script.engine.GroovyScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class normalizes for a given input schema to output schema using bulk script.
 *
 */
public class BulkNormalizationRuntime extends NormalizationRuntime {
    private static final Logger LOG = LoggerFactory.getLogger(BulkNormalizationRuntime.class);

    public static final String INPUT_SCHEMA_BINDING = "__inputSchema";
    public static final String OUTPUT_SCHEMA_BINDING = "__outputSchema";

    private final GroovyScript<Map<String, Object>> groovyScript;

    public BulkNormalizationRuntime(BulkNormalizationConfig bulkNormalizationConfig) {
        super(bulkNormalizationConfig);

        String normalizationScript = bulkNormalizationConfig.normalizationScript;

        Map<String, Object> initialBindings = new HashMap<String, Object>() {{
            // creating new instances to avoid modifying the actual schemas by script.
            put(INPUT_SCHEMA_BINDING, Schema.of(normalizationConfig.getInputSchema().getFields()));
            put(OUTPUT_SCHEMA_BINDING, Schema.of(normalizationConfig.getOutputSchema().getFields()));}};

        groovyScript = normalizationScript != null && !normalizationScript.isEmpty()
                ? new GroovyScript<Map<String, Object>>(normalizationScript, new GroovyScriptEngine(), initialBindings) : null;
    }

    @Override
    public Map<String, Object> normalize(IotasEvent iotasEvent) throws NormalizationException {
        LOG.debug("Running bulk normalization script [{}] for received event [{}] ", groovyScript, iotasEvent);

        Map<String, Object> result = null;
        if (groovyScript != null) {
            try {
                result = groovyScript.evaluate(iotasEvent);
            } catch (ScriptException e) {
                throw new NormalizationException(e);
            }
        } else {
            result = iotasEvent.getFieldsAndValues();
            LOG.info("Nothing to normalize as normalization script is not configured");
        }

        return result;
    }
}
