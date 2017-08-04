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

package com.streamsets.pipeline.stage.origin.jdbc.CT.sqlserver.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class QueryUtil {
  private static final Logger LOG = LoggerFactory.getLogger(QueryUtil.class);
  static final String CT_TABLE_NAME = "CT";
  static final String TABLE_NAME = "P";

  public static final String SYS_CHANGE_VERSION = "SYS_CHANGE_VERSION";
  public static final int SYS_CHANGE_VERSION_TYPE = -5; // bigint

  private static final String CHANGE_TRACKING_TABLE_QUERY = "SET NOCOUNT ON;\n" +
      "SELECT min_valid_version \n" +
      "FROM sys.change_tracking_tables t\n" +
      "WHERE t.object_id = OBJECT_ID('%s')";

  private static final String CHANGE_TRACKING_CURRENT_VERSION_QUERY = "SELECT CHANGE_TRACKING_CURRENT_VERSION()";

  private static final String INIT_CHANGE_TRACKING_QUERY = "SET NOCOUNT ON;\n" +
      "DECLARE @synchronization_version BIGINT = %1$s;\n" +
      "\n" +
      "SELECT TOP %2$s * \n" +
      "FROM %3$s AS P\n" +
      "RIGHT OUTER JOIN CHANGETABLE(CHANGES %3$s, @synchronization_version) AS " + CT_TABLE_NAME + "\n" +
      "%4$s\n" +
      "%5$s";

  private static final String CHANGE_TRACKING_QUERY = "SET NOCOUNT ON;\n" +
      "SELECT TOP %1$s * \n" +
          "FROM %2$s AS " + TABLE_NAME + "\n" +
          "RIGHT OUTER JOIN CHANGETABLE(CHANGES %2$s, %3$s) AS " + CT_TABLE_NAME + "\n" +
          "%4$s\n" +
          "%5$s\n" +
          "%6$s";

  private static final Joiner COMMA_SPACE_JOINER = Joiner.on(", ");
  private static final Joiner AND_JOINER = Joiner.on(" AND ");

  private static final String COLUMN_GREATER_THAN_VALUE = "%s > %s";
  private static final String COLUMN_EQUALS_VALUE = "%s = %s";
  private static final String ON_CLAUSE = " ON %s";
  private static final String WHERE_CLAUSE = " WHERE %s ";
  private static final String ORDER_BY_CLAUSE = " ORDER BY %s ";
  private static final String OR_CLAUSE = " (%s) OR (%s) ";

  private QueryUtil() {}

  public static String getCurrentVersion() {
    return CHANGE_TRACKING_CURRENT_VERSION_QUERY;
  }

  public static String getMinVersion() {
    return CHANGE_TRACKING_TABLE_QUERY;
  }

  public static String buildQuery(Map<String, String> offsetMap, int maxBatchSize, String tableName, Collection<String> offsetColumns, Map<String, String> startOffset) {
    boolean isInitial = true;

    List<String> greaterCondition = new ArrayList<>();
    List<String> equalCondition = new ArrayList<>();
    List<String> orderCondition = new ArrayList<>();

    orderCondition.add(SYS_CHANGE_VERSION);

    for (String primaryKey: offsetColumns) {
      if (!primaryKey.equals(SYS_CHANGE_VERSION)) {
        equalCondition.add(String.format(COLUMN_EQUALS_VALUE, CT_TABLE_NAME + "." + primaryKey, TABLE_NAME + "." + primaryKey));
        orderCondition.add(CT_TABLE_NAME + "." + primaryKey);

        if (!Strings.isNullOrEmpty(offsetMap.get(primaryKey))) {
          greaterCondition.add(String.format(COLUMN_GREATER_THAN_VALUE, CT_TABLE_NAME + "." + primaryKey, offsetMap.get(primaryKey)));
          isInitial = false;
        }
      }
    }

    String equal = String.format(ON_CLAUSE, AND_JOINER.join(equalCondition));
    String orderby = String.format(ORDER_BY_CLAUSE, COMMA_SPACE_JOINER.join(orderCondition));

    if (!isInitial) {
      greaterCondition.add(String.format(COLUMN_EQUALS_VALUE, CT_TABLE_NAME + "." + SYS_CHANGE_VERSION, offsetMap.get(SYS_CHANGE_VERSION)));
      String condition1 = AND_JOINER.join(greaterCondition);
      String condition2 = String.format(COLUMN_GREATER_THAN_VALUE, CT_TABLE_NAME + "." + SYS_CHANGE_VERSION, offsetMap.get(SYS_CHANGE_VERSION));
      String greater = String.format(WHERE_CLAUSE, String.format(OR_CLAUSE, condition1, condition2));

      return String.format(CHANGE_TRACKING_QUERY, maxBatchSize, tableName, startOffset.get(SYS_CHANGE_VERSION), equal, greater, orderby);
    }

    return String.format(INIT_CHANGE_TRACKING_QUERY, startOffset.get(SYS_CHANGE_VERSION), maxBatchSize, tableName, equal, orderby);
  }
}
