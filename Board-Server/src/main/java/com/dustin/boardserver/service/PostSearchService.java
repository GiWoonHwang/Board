package com.dustin.boardserver.service;


import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.request.PostSearchRequest;

import java.util.List;

public interface PostSearchService {
    List<PostDTO> getProducts(PostSearchRequest postSearchRequest);
}
