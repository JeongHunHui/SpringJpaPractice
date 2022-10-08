package com.deca.SpringJpaPractice.domain.delivery.entity;

import com.deca.SpringJpaPractice.domain.address.entity.Address;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {
  @Id
  @GeneratedValue
  @Column(name = "delivery_id")
  private Long id;

  @OneToOne(mappedBy = "delivery", fetch = LAZY)
  private Order order;

  @Embedded private Address address;

  // 기본이 EnumType.ORDINAL -> 숫자로 들어감(READY=1, COMP=2...)
  // -> 중간에 다른 상태가 생기면 망함 -> EnumType.STRING ㄱㄱ
  @Enumerated(EnumType.STRING)
  private DeliveryStatus status; // READY, COMP
}
