package com.deca.SpringJpaPractice.domain.order.entity;

import com.deca.SpringJpaPractice.domain.delivery.entity.Delivery;
import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.orderItem.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItems = new ArrayList<>();

  // Order 엔티티를 조회하며 Delivery를 같이 조회하는 경우가 많으므로
  // Order에서 FK를 관리
  @OneToOne
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate;

  // 기본이 EnumType.ORDINAL -> 숫자로 들어감(READY=1, COMP=2...)
  // -> 중간에 다른 상태가 생기면 망함 -> EnumType.STRING ㄱㄱ
  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
}
