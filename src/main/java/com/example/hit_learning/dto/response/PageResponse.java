package com.example.hit_learning.dto.response;

import com.example.hit_learning.dto.OrderDTO;
import com.example.hit_learning.dto.request.PageRequest;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int pageIndex ;
    int pageSize  ;
    List<OrderDTO> orders = new ArrayList<>() ;
    int totalPage ;
    List<T> content ;


    public PageResponse(int pageIndex, int pageSize, List<OrderDTO> orders, List<T> content, int totalPage) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.orders = orders;
        this.content = content;
        this.totalPage = totalPage;
    }
}
