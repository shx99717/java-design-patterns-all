package com.shx99717.builder;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class App {

  public static void main(String[] args) {

    User user1 = new User.Builder()
            .firstName("Hello")
            .lastName("World")
            .age(30)
            .phone("123456")
            .address("the address 1")
            .build();

    LOGGER.info("The user1 is " + user1);


    User user2 = new User.Builder()
            .firstName("Foo")
            .lastName("Bar")
            .age(21)
            //.phone("654322") Optional
            .address("the address 2")
            .build();

    LOGGER.info("The user2 is " + user2);


    /* This will throw Exception as age which is required not specified
    User user3 = new User.Builder()
            .firstName("Foo")
            .lastName("Bar")
            .build();
    LOGGER.info("The user3 is " + user3);
    */
  }
}
