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
package com.streamsets.pipeline.kafka.impl;

import kafka.utils.VerifiableProperties;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class ExpressionPartitioner implements Partitioner, kafka.producer.Partitioner {

  public ExpressionPartitioner(VerifiableProperties props) {

  }

  public ExpressionPartitioner() {

  }

  @Override
  public int partition(
    String topic,
    Object key,
    byte[] keyBytes,
    Object value,
    byte[] valueBytes,
    Cluster cluster
  ) {
    return Integer.parseInt((String)key);
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {

  }

  @Override
  public int partition(Object key, int numPartitions) {
    return Integer.parseInt((String)key);
  }
}
