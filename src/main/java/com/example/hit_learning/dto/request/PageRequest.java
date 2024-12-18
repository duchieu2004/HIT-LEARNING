package com.example.hit_learning.dto.request;

import com.example.hit_learning.dto.OrderDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageRequest {
    @Builder.Default
    int pageIndex = 1;
    @Builder.Default
    int pageSize = 999999;
    List<OrderDTO> orders = new ArrayList<>() ;

    @JsonIgnore
    public Pageable getPageable(){
        List<Sort.Order> sortOrder =new ArrayList<>()  ;
        for(OrderDTO x :orders){
            sortOrder.add(new Sort.Order( x.getDirection(), x.getProperties()));
        }
        Sort sort = Sort.by(sortOrder) ;
        return org.springframework.data.domain.PageRequest.of(pageIndex-1,pageSize, sort) ;
    }
}
