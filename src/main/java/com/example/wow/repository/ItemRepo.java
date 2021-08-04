package com.example.wow.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.wow.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends MongoRepository<Item, String> {
}