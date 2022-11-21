package com.originit.response.result;

import lombok.Data;

@Data
public class SimpleData<T> {

    private T data;

    SimpleData(T data) {
        this.data = data;
    }

    public  static <D> SimpleData<D> of(D data) {
        return new SimpleData<>(data);
    }
}
