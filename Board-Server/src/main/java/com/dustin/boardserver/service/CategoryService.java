package com.dustin.boardserver.service;


import com.dustin.boardserver.dto.CategoryDTO;

public interface CategoryService {

    void register(String accountId, CategoryDTO categoryDTO);

    void update(CategoryDTO categoryDTO);

    void delete(int categoryId);
}
