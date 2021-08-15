package com.latelier.api.domain.course.service;

import com.latelier.api.domain.course.packet.response.ResCategory;
import com.latelier.api.domain.course.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public List<ResCategory> getAllCategories() {

        return categoryRepository.findByParentIsNullOrderById().stream()
                .map(ResCategory::of)
                .collect(Collectors.toList());
    }

}
