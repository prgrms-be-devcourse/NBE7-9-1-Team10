package com.example.cafe.domain.item.repository;

import com.example.cafe.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // JpaRepository가 제공하는 기본 메소드들로 CRUD 구현
    // - save(item) : 생성/수정
    // - findById(id) : ID로 조회
    // - findAll() : 전체 조회
    // - deleteById(id) : 삭제

}
