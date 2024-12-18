package com.example.hit_learning.service;

import com.example.hit_learning.dto.request.ItemRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ItemResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.entity.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    ItemResponse addItem(ItemRequest request) ;
    ItemResponse updItem(String itemId, ItemRequest request) ;
    List<ItemResponse> delItem(String... itemIds) ;
    Item findById(String itemId) ;
    PageResponse<ItemResponse> findAll(PageRequest request) ;
    PageResponse<ItemResponse> findBySection(String sectionId, PageRequest request) ;

}
