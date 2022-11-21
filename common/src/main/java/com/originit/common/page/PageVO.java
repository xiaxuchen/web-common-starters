package com.originit.common.page;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xxc
 */
@Data
public class PageVO<T> {

    private List<T> list;

    private Long total;

    private Long current;

    private Long size;


    public static <T> PageVO<T> of(List<T> pageList,Long total,Long current) {
        return of(pageList,total,current,t -> t);
    }



    public static <T,R> PageVO<R> of(List<T> pageList, Long total, Long current, Function<T,R> convertor) {
        Assert.notNull(convertor,"转换器不能为空");
        return create(pageList.stream().map(convertor).collect(Collectors.toList()),total,current,null);
    }

    public static <T> PageVO<T> empty() {
        return create(null,0L,1L,15L);
    }

    public static <T> PageVO<T> empty(long size) {
        return create(null,0L,1L,size);
    }
    
    public static <T> PageVO<T> create (List<T> list,Long total,Long current,Long size) {
        final PageVO<T> objectPageVO = new PageVO<>();
        objectPageVO.size = size;
        objectPageVO.current = current;
        objectPageVO.total = total;
        objectPageVO.list = list;
        if (objectPageVO.total == 0L) {
            objectPageVO.current = 1L;
        }
        return objectPageVO;
    }


}