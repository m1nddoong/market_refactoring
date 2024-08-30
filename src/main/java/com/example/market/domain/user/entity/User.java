package com.example.market.domain.user.entity;



import com.example.market.domain.user.constant.BusinessStatus;
import com.example.market.domain.user.constant.Role;
import com.example.market.global.common.BaseEntity;
import com.example.market.domain.shop.entity.Shop;
import com.example.market.domain.trade.entity.TradeItem;
import com.example.market.global.oauth2.constant.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String username;
    private String phone;
    private String nickname;
    private LocalDate birthday;
    private String profileImg;
    private String businessNum; // 사업자 등록 번호
    private Role role;

    @Enumerated(EnumType.STRING)
    private BusinessStatus businessStatus; // 사업자 전환 신청 상태

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "user")
    private List<TradeItem> tradeItem;

    @OneToOne(mappedBy = "user")
    private Shop shop;
}

