package com.latelier.api.controller;

import com.latelier.api.domain.course.packet.response.ResCategory;
import com.latelier.api.domain.course.service.CategoryService;
import com.latelier.api.domain.model.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    @ApiOperation(
            value = "모든 카테고리정보 얻기",
            notes = "모든 카테고리에 대한 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리정보 조회 성공")})
    public ResponseEntity<Result<List<ResCategory>>> getAllCategories() {


        return ResponseEntity.ok(Result.of(categoryService.getAllCategories()));
    }

}
