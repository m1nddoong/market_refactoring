package com.example.market.domain.used_trade.service;

import com.example.market.domain.auth.entity.User;
import com.example.market.domain.used_trade.entity.TradeItem;
import com.example.market.global.common.AuthenticationFacade;
import com.example.market.global.util.FileHandlerUtils;
import com.example.market.domain.used_trade.dto.TradeItemDto;
import com.example.market.domain.used_trade.entity.TradeItem.ItemStatus;
import com.example.market.domain.used_trade.repository.TradeItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeItemService {
    private final TradeItemRepository tradeItemRepository;
    private final AuthenticationFacade authenticationFacade;
    private final FileHandlerUtils fileHandlerUtils;

    // 중고 거래 물품 조회
    public TradeItemDto readTradeItem(Long tradeItemId) {
        return TradeItemDto.fromEntity(tradeItemRepository.findById(tradeItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    // 중고 거래 물품 생성
    public TradeItemDto createTradeItem(
            TradeItemDto dto,
            MultipartFile tradeItemImg
    ) {
        User currenntUser = authenticationFacade.extractUser();
        // 최초 물품 등록 시 이미지 업로드
        String imagePath = fileHandlerUtils.saveImage(tradeItemImg);

        // usedTradeItem 생성 및 저장, dto 반환
        return TradeItemDto.fromEntity(tradeItemRepository.save(TradeItem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .image(imagePath)
                .price(dto.getPrice())
                .itemStatus(ItemStatus.ON_SALE)
                .user(currenntUser)
                .build()));
    }

    // 중고 거래 물품 수정
    public TradeItemDto updateTradeItem(
            TradeItemDto dto,
            MultipartFile tradeItemImg,
            Long tradeItemId
    ) {
        // 현재 인증된 사용자가 누구인지, 어떤 물건인지 찾고
        User currentUser = authenticationFacade.extractUser();
        TradeItem targetUsedTradeItem = tradeItemRepository.findById(tradeItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 내가 수정하고자 하는 물건을 소유한 사람이 맞는지 email 을 가지고 판단
        if (!currentUser.getEmail().equals(targetUsedTradeItem.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // 기존 이미지 삭제 후 새로운 이미지 저장
        String oldImage = targetUsedTradeItem.getImage();
        if (oldImage != null) fileHandlerUtils.deleteImage(oldImage);

        String imagePath = fileHandlerUtils.saveImage(tradeItemImg);
        imagePath = imagePath.replaceAll("\\\\", "/");
        currentUser.setProfileImg(imagePath);

        targetUsedTradeItem.setTitle(dto.getTitle());
        targetUsedTradeItem.setDescription(dto.getDescription());
        targetUsedTradeItem.setPrice(dto.getPrice());
        targetUsedTradeItem.setImage(imagePath);

        return TradeItemDto.fromEntity(tradeItemRepository.save(targetUsedTradeItem));
    }

    // 증고 거래 물품 삭제
    public void deleteTradeItem(Long tradeItemId) {
        User currentUser = authenticationFacade.extractUser();
        TradeItem targetUsedTradeItem = tradeItemRepository.findById(tradeItemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 내가 삭제하고자 하는 물건을 소유한 사람이 맞는지 email 을 가지고 판단
        if (!currentUser.getEmail().equals(targetUsedTradeItem.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        tradeItemRepository.deleteById(tradeItemId);
    }
}
