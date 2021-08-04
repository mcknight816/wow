package com.example.wow.service;

import com.example.wow.repository.ItemRepo;
import com.example.wow.model.Item;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.Mockito;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExtendWith(SpringExtension.class)
@Scope("test")
class ItemServiceTest {

  @MockBean
  private ItemRepo repo;

  Item item1;
  Item item2;
  ItemService service;

  @BeforeEach
  void before(){
    EasyRandom generator = new EasyRandom();
    item1 = generator.nextObject(Item.class);
    item2 = generator.nextObject(Item.class);
    service = new ItemService(this.repo);
  }

  @Test
  void shouldSave(){
    Mockito.when(repo.save(Mockito.any(Item.class))).thenReturn(item1);
    Item data = service.save(Item.builder().build());
    Assertions.assertEquals(data,item1);
  }

  @Test
  void findById(){
    Mockito.when(repo.findById(Mockito.any(String.class))).thenReturn(Optional.of(item1));
    Assertions.assertNotNull(service.findById(String.valueOf("1")));
  }

  @Test
  void deleteById(){
    Assertions.assertAll(() -> service.deleteById(String.valueOf("1")));
  }

  @Test
  void findAll(){
    Mockito.when(repo.findAll()).thenReturn(Arrays.asList(item1,item2));
    List<Item> all = StreamSupport.stream(service.findAll().spliterator(), false).collect(Collectors.toList());
    Assertions.assertEquals(2, all.size());
  }
}
