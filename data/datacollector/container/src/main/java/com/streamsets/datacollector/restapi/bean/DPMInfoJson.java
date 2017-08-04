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
package com.streamsets.datacollector.restapi.bean;

import java.util.List;
import java.util.Map;

public class DPMInfoJson {
  private String baseURL;
  private String userID;
  private String userPassword;
  private String organization;
  private List<String> labels;
  private List<Map<String, Object>> dpmUserList;
  private List<Map<String, Object>> dpmGroupList;

  public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }

  public List<Map<String, Object>> getDpmUserList() {
    return dpmUserList;
  }

  public void setDpmUserList(List<Map<String, Object>> dpmUserList) {
    this.dpmUserList = dpmUserList;
  }

  public List<Map<String, Object>> getDpmGroupList() {
    return dpmGroupList;
  }

  public void setDpmGroupList(List<Map<String, Object>> dpmGroupList) {
    this.dpmGroupList = dpmGroupList;
  }
}
