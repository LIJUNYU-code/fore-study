package org.myproject.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;      // 当前页数据列表
    private Long total;           // 总记录数
    private Integer page;         // 当前页码
    private Integer size;         // 每页大小
    private Integer pages;        // 总页数

    public static <T> PageResult<T> of(List<T> records, Long total, Integer page, Integer size) {
        return PageResult.<T>builder()
                .records(records)
                .total(total)
                .page(page)
                .size(size)
                .pages((int) Math.ceil((double) total / size))
                .build();
    }
}