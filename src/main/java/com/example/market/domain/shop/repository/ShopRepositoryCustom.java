package com.example.market.domain.shop.repository;

import com.example.market.domain.shop.dto.SearchShopDto;
import com.example.market.domain.shop.dto.ShopDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepositoryCustom {
    Page<ShopDto> getShopListWithPages(SearchShopDto dto, Pageable pageable);
    List<ShopDto> getShopListWithOpenRequestStatus();

    List<ShopDto> getShopListWithCloseRequestStatus();
}
