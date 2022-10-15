package com.deca.SpringJpaPractice.domain.orderItem.entity;

import com.deca.SpringJpaPractice.domain.item.entity.Item;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class OrderItem {
  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  /** 주문 가격 */
  private int orderPrice;
  /** 주문 수량 */
  private int count;

  // == 생성 메소드 == //
  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);
    item.removeStock(count);
    return orderItem;
  }

  // == 비즈니스 로직 == //
  // 주문을 취소하면 주문 수량만큼 item의 재고를 늘려줘야함
  public void cancel() {
    getItem().addStock(count);
  }

  // == 조회 로직 == //
  public int getTotalPrice() {
    return getCount() * getOrderPrice();
  }
}
