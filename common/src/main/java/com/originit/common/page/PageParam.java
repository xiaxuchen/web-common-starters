package com.originit.common.page;

import lombok.Data;


/**
 * 分页参数
 * @author xxc
 */
@Data
public class PageParam {

    private Long size;

    private Long page;

    public static long defaultPage = 1;

    public static long defaultSize = 20;


    public static PageParam nonNullDefault(Long page,Long size,PageParam param) {
        if (param != null) {
            return param;
        }
        final PageParam pageParam = new PageParam();
        pageParam.setPage(page);
        pageParam.setSize(size);
        return pageParam;
    }

    public static PageParam nonNullDefault(PageParam param) {
        return nonNullDefault(defaultPage,defaultSize,param);
    }
}
