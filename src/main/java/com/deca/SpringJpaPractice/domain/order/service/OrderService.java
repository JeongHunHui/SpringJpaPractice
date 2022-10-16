package com.deca.SpringJpaPractice.domain.order.service;

import com.deca.SpringJpaPractice.domain.delivery.entity.Delivery;
import com.deca.SpringJpaPractice.domain.item.entity.Item;
import com.deca.SpringJpaPractice.domain.item.repository.ItemRepository;
import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.member.repository.MemberRepository;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import com.deca.SpringJpaPractice.domain.order.repository.OrderRepository;
import com.deca.SpringJpaPractice.domain.orderItem.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final ItemRepository itemRepository;

  /** 주문 */
  @Transactional
  public Long order(Long memberId, Long itemId, int count) {
    // 엔티티 조회
    Member member = memberRepository.findOne(memberId);
    Item item = itemRepository.findOne(itemId);

    // 배송정보 생성
    Delivery delivery = new Delivery();
    delivery.setAddress(member.getAddress());

    // 주문상품 셍성
    // 예제의 간소화를 위해 주문상품은 1가지만 넘기도록 설정
    OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

    // 주문 생성
    Order order = Order.createOrder(member, delivery, orderItem);

    // 주문 저장
    // order엔티티의 orderItem cascade = ALL
    orderRepository.save(order);

    return order.getId();
  }

  /** 주문 취소 */
  @Transactional
  public void cancelOrder(Long orderId) {
    // 주문 엔티티 조회
    Order order = orderRepository.findOne(orderId);
    // 주문 취소
    order.cancel();
  }

  // 검색
//  public List<Order> findOrders() {}
}
