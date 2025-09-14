package dev.lqwd.service.weahter_api;

import java.util.List;
import java.util.Optional;

public interface AbstractApiService<T> {

    T findFirst(String uri);
    List<T> fetchApiData(String uri);
}
