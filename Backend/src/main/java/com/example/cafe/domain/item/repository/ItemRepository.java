package com.example.cafe.domain.item.repository;

import com.example.cafe.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // JpaRepository가 제공하는 기본 메소드들로 CRUD 구현
    // - save(item) : 생성/수정
    // - findById(id) : ID로 조회
    // - findAll() : 전체 조회
    // - deleteById(id) : 삭제

    //issue #76에 의해 추가 된 쿼리입니다.
    @Query("select i from Item i where i.itemId in :itemIds")
    List<Item> findSpecificItems(@Param("itemIds") List<Long> itemIds);
}
