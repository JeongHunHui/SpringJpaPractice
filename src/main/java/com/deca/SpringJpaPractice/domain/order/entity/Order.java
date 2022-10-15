package com.deca.SpringJpaPractice.domain.order.entity;

import com.deca.SpringJpaPractice.domain.delivery.entity.Delivery;
import com.deca.SpringJpaPractice.domain.delivery.entity.DeliveryStatus;
import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.orderItem.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @OneToMany(mappedBy = "order", cascade = ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  // Order 엔티티를 조회하며 Delivery를 같이 조회하는 경우가 많으므로
  // Order에서 FK를 관리
  @OneToOne(fetch = LAZY, cascade = ALL)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate;

  // 기본이 EnumType.ORDINAL -> 숫자로 들어감(READY=1, COMP=2...)
  // -> 중간에 다른 상태가 생기면 망함 -> EnumType.STRING ㄱㄱ
  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

  // === 연관관계 메소드 === //
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  // order의 orderItems에 orderItem을 추가하고
  // orderItem의 order도 설정해준다
  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  // == 생성 메소드 == //
  // 생성하는 시점 변경 시 아래 생성 메소드만 변경하면 됨
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setDelivery(delivery);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }

  // == 비즈니스 로직 == //
  /** 주문 취소 */
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }
    this.setStatus(OrderStatus.CANCEL);
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  // == 조회 로직 == //
  /** 전체 주문 가격 조회 */
  public int getTotalPrice(){
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
        totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }
}
