package com.dustin.boardserver.mapper;

import com.dustin.boardserver.dto.PostDTO;
import com.dustin.boardserver.dto.request.PostSearchRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostSearchMapper {
    public List<PostDTO> selectPosts(PostSearchRequest postSearchRequest);
}
