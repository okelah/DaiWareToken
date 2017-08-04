/**
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.stage.processor.statsaggregation;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ConfigGroups;
import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Processor;
import com.streamsets.pipeline.api.StageDef;
import com.streamsets.pipeline.api.el.SdcEL;
import com.streamsets.pipeline.configurablestage.DProcessor;

@StageDef(
    version=1,
    label="Aggregates Pipeline Metrics",
    description="",
    icon="stats.png",
    onlineHelpRefUrl = ""
)
@ConfigGroups(Groups.class)
@GenerateResourceBundle
public class MetricAggregationDProcessor extends DProcessor {

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${PIPELINE_CONFIG}",
    label = "Pipeline Configuration",
    description = "Pipeline whose metrics are to be aggregated",
    displayPosition = 10,
    group = "STATS"
  )
  public String pipelineConfig;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${RULES_CONFIG}",
    label = "Pipeline Rules Configuration",
    description = "Rules Configuration of pipeline whose metrics are to be aggregated",
    displayPosition = 20,
    group = "STATS"
  )
  public String rulesConfig;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${PIPELINE_URL}",
    label = "Pipeline Url",
    description = "The URL of the pipeline which must be included in the email alerts",
    displayPosition = 30,
    group = "STATS"
  )
  public String pipelineUrl;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${GET_LATEST_METRICS_URL}",
    label = "Remote Timeseries URL",
    description = "The target URL from which the latest aggregated metrics can be fetched",
    displayPosition = 40,
    group = "STATS"
  )
  public String targetUrl;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${sdc:authToken()}",
    label = "Auth Token",
    description = "The auth token generated by DPM",
    displayPosition = 50,
    group = "STATS"
  )
  public String authToken;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${sdc:id()}",
    label = "Sdc Id",
    displayPosition = 60,
    group = "STATS",
    elDefs = SdcEL.class
  )
  public String sdcId;

  @ConfigDef(
    required = true,
    type = ConfigDef.Type.STRING,
    defaultValue = "${JOB_ID}",
    label = "Job Id",
    displayPosition = 70,
    group = "STATS"
  )
  public String jobId;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "-1",
      label = "Retry Attempts",
      group = "STATS",
      description = "Max no of retries to fetch latest metrics from time-series DPM App." +
          " To retry indefinitely, use -1. The wait time between retries starts at 2 seconds" +
          " and doubles until reaching 16 seconds.",
      displayPosition = 80)
  public int retryAttempts = -1;

  @ConfigDef(
    required = false,
    type = ConfigDef.Type.NUMBER,
    defaultValue = "10",
    label = "Alert Texts To Retain",
    group = "STATS",
    description = "Number of alert texts to retain in memory",
    displayPosition = 80)
  public int alertTextsToRetain = 10;

  @Override
  protected Processor createProcessor() {
    return new MetricAggregationProcessor(
        pipelineConfig,
        rulesConfig,
        pipelineUrl,
        targetUrl,
        authToken,
        sdcId,
        jobId,
        retryAttempts,
        alertTextsToRetain
    );
  }

}
