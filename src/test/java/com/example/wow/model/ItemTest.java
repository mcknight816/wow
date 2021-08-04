package com.example.wow.model;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Scope;

@Scope("test")
class ItemTest {

  @Test
  void shouldCreateItem(){
    EasyRandom generator = new EasyRandom();
    Assertions.assertNotNull(generator.nextObject(Item.class));
  }

  @Test
  void shouldBuildItem(){
    Assertions.assertNotNull(Item.builder().build());
  }
}
