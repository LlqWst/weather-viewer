package dev.lqwd.service.weahter_api;

import java.util.List;

public interface AbstractApiService<T> {

    List<T> fetchApiData(String uri);
}
