package com.dishatech.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

/**
*
* @author Dinesh Sharma
*/
@DataObject(generateConverter = true, publicConverter = false)
public class Customer {

  private String id;
  private String firstName;
  private String lastName;

  public Customer (
    String id,
    String firstName,
    String lastName
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Customer(JsonObject json) {
	  CustomerConverter.fromJson(json, this);
  }

  public Customer(Customer other) {
    this.id = other.getId();
    this.firstName = other.getFirstName();
    this.lastName = other.getLastName();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    CustomerConverter.toJson(this, json);
    return json;
  }

  @Fluent public Customer setId(String id){
    this.id = id;
    return this;
  }
  
  public String getId() {
    return this.id;
  }

  @Fluent public Customer setFirstName(String firstName){
    this.firstName = firstName;
    return this;
  }
  
  public String getFirstName() {
    return this.firstName;
  }

  @Fluent public Customer setLastName(String lastName){
    this.lastName = lastName;
    return this;
  }
  public String getLastName() {
    return this.lastName;
  }

}
