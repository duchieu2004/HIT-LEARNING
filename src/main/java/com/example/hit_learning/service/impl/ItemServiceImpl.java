package com.example.hit_learning.service.impl;

import com.example.hit_learning.dto.request.ItemRequest;
import com.example.hit_learning.dto.request.PageRequest;
import com.example.hit_learning.dto.response.ItemResponse;
import com.example.hit_learning.dto.response.PageResponse;
import com.example.hit_learning.entity.Item;
import com.example.hit_learning.entity.Section;
import com.example.hit_learning.exception.AppException;
import com.example.hit_learning.exception.ErrorCode;
import com.example.hit_learning.mapper.ItemMapper;
import com.example.hit_learning.repository.ItemRepository;
import com.example.hit_learning.repository.RedisRepository;
import com.example.hit_learning.repository.SectionRepository;
import com.example.hit_learning.service.ItemService;
import com.example.hit_learning.repository.MinIORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    final String KEY = "item" ;
    final SectionRepository sectionRepository ;
    final ItemRepository itemRepository ;
    final ItemMapper itemMapper ;
    final MinIORepository minIORepository;
    final RedisRepository redisRepository;




    @Override
    public ItemResponse addItem(ItemRequest request) {
        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(
                () -> new AppException(ErrorCode.SECTION_NOT_EXISTED)
        ) ;
        MultipartFile file = request.getFile();

        if(request.getFile().getSize()/ 1000000 > 1000){
            throw new AppException(ErrorCode.ITEM_FILE_LONG);
        }

        var data = minIORepository.upload(file) ;
        Item item ;
        item = itemMapper.toItem(request) ;
        item.setSection(section);
        item.setVideoId(data.get("videoId"));
        item.setVideoName(data.get("videoName"));
        item = itemRepository.save(item) ;
        deleteRedis();
        var response = itemMapper.toResponse(item) ;
        return response;
    }

    @Override
    public ItemResponse updItem(String itemId, ItemRequest request) {

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new AppException(ErrorCode.ITEM_NOT_EXISTED)
        );
        if(request.getFile() != null) {
            MultipartFile file = request.getFile();
            if(!item.getVideoName().equals(file.getOriginalFilename())){
                minIORepository.deleteFile(item.getVideoId());
                var data = minIORepository.upload(file) ;
                item.setVideoId(data.get("videoId"));
                item.setVideoName(data.get("videoName"));

            }
        }
        itemMapper.updItem(item, request);
        item = itemRepository.save(item) ;
        deleteRedis();
        var response = itemMapper.toResponse(item) ;
        return response;
    }


    @Override
    public List<ItemResponse> delItem(String... itemIds) {
        List<ItemResponse> ans = new ArrayList<>()  ;
        for(String x : itemIds){
            var itemOptional = itemRepository.findById(x) ;
            if(itemOptional.isPresent()){
                var xy = itemOptional.get() ;
                itemRepository.delete(xy);
                minIORepository.deleteFile(xy.getVideoId());
                ans.add(itemMapper.toResponse(xy)) ;
            }
        }
        deleteRedis();
        return ans;
    }

    @Override
    public Item findById(String itemId) {
        final String field = itemId ;
        Object value = redisRepository.getHash(KEY, itemId) ;
        Item item ;
        if(value == null) {
            item = itemRepository.findById(itemId).orElseThrow(
                    ()-> new AppException(ErrorCode.ITEM_NOT_EXISTED)
            );
        }
        else{
            item = (Item) redisRepository.convertToObject(value+"", Item.class);
        }
        int view = item.getView();
        int version = item.getVersion();
        while (true) {
            try {
                if (item == null) {
                    break;
                }
                itemRepository.updateView(view, itemId, version);
                break;
            } catch (AppException e) {
                throw e;
            } catch (RuntimeException e) {
                version++;
                System.out.println(e.getMessage());
            }
        }
        item.setVersion(version+1);
        item.setView(view+1);
        redisRepository.delete(KEY, field);
        String json = redisRepository.convertToJson(item) ;
        redisRepository.setHashRedis(KEY, field, json);
        return item ;
    }

    @Override
    public PageResponse<ItemResponse> findAll(PageRequest request) {
        String field=request.toString() ;
        Object value= redisRepository.getHash(KEY, field);
        if(value == null) {
            Pageable pageable = request.getPageable();
            Page<Item> page = itemRepository.findAll(pageable);
            List<ItemResponse> ans = new ArrayList<>();
            for (Item x : page.getContent()) {
                var xy = itemMapper.toResponse(x);
                ans.add(xy);
            }
            PageResponse<ItemResponse> response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders() , ans , page.getTotalPages() ) ;
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }
        else{
            PageResponse<ItemResponse> response = (PageResponse<ItemResponse>) redisRepository.convertToObject(value+"", PageResponse.class);
            return response;
        }

    }

    @Override
    public PageResponse<ItemResponse> findBySection(String sectionId , PageRequest request) {
        String field="sectionId" + sectionId + request.toString() ;

        Object json=redisRepository.getHash(KEY, field) ;
        if(json == null){
            Section section = sectionRepository.findById(sectionId).orElseThrow(
                    () -> new AppException(ErrorCode.SECTION_NOT_EXISTED)
            ) ;
            Pageable pageable = request.getPageable() ;
            Page<Item> page = itemRepository.findAllBySection(section, pageable) ;
            List<ItemResponse> ans = new ArrayList<>() ;
            for(Item x : page.getContent()){
                var xy = itemMapper.toResponse(x) ;
                ans.add(xy) ;
            }
            PageResponse<ItemResponse> response = new PageResponse<>(request.getPageIndex(), request.getPageSize(), request.getOrders() , ans , page.getTotalPages() ) ;
            redisRepository.setHashRedis(KEY, field, redisRepository.convertToJson(response));
            return response;
        }else{
            PageResponse<ItemResponse> response = (PageResponse<ItemResponse>) redisRepository.convertToObject(json+"", PageResponse.class);
            return response;
        }
    }
    private void deleteRedis(){
        redisRepository.delete(KEY);
    }
}
