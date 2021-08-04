package com.example.wow.controller;

import com.example.wow.model.Item;
import com.example.wow.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
@CrossOrigin(origins = "http://localhost:4200")
public class ItemController {

  private final ItemService service;

  public ItemController(ItemService service) {
    this.service = service;
  }

  @PostMapping(value="/item",produces = MediaType.APPLICATION_JSON_VALUE)
  public Item save(@RequestBody Map<String,Object> dto){
    var mapper = new ObjectMapper();
    return this.service.save(mapper.convertValue(dto,Item.class));
  }

  @GetMapping(value = "/item/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
  public Optional<Item> findById(@PathVariable("id") String id ){
    return this.service.findById(String.valueOf(id));
  }

  @GetMapping(value = "/item",produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Item> findAll(){
    return this.service.findAll();
  }

  @DeleteMapping(value = "/item/{id}")
  public void deleteById(@PathVariable("id") String id ){
   this.service.deleteById(String.valueOf(id));
  }
}
