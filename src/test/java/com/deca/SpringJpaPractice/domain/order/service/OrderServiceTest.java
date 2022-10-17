package com.deca.SpringJpaPractice.domain.order.service;

import com.deca.SpringJpaPractice.domain.address.entity.Address;
import com.deca.SpringJpaPractice.domain.item.entity.Book;
import com.deca.SpringJpaPractice.domain.item.entity.Item;
import com.deca.SpringJpaPractice.domain.item.exception.NotEnoughStockException;
import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import com.deca.SpringJpaPractice.domain.order.entity.OrderStatus;
import com.deca.SpringJpaPractice.domain.order.repository.OrderRepository;
import com.deca.SpringJpaPractice.domain.order.repository.OrderSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
  @Autowired EntityManager em;
  @Autowired OrderService orderService;
  @Autowired OrderRepository orderRepository;

  @Test
  public void 상품주문() throws Exception {
    // given
    Member member = createMember("회원1");

    Book book = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;
    // when
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // then
    Order getOrder = orderRepository.findOne(orderId);
    assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
    assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
    assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
    assertEquals("주문 수량만큼 재고가 줄여야 한다.", 8, book.getStockQuantity());
  }

  @Test(expected = NotEnoughStockException.class)
  public void 상품주문_재고수량초과() throws Exception {
    // given
    Member member = createMember("회원1");
    Item item = createBook("시골 JPA", 10000, 10);
    int orderCount = 11;

    // when
    orderService.order(member.getId(), item.getId(), orderCount);

    // then
    fail("재고 수량 부족 예외가 발생해야 한다.");
  }

  @Test
  public void 주문취소() throws Exception {
    // given
    Member member = createMember("회원1");
    Book item = createBook("시골 JPA", 10000, 10);
    int orderCount = 2;
    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
    // when
    orderService.cancelOrder(orderId);

    // then
    Order getOrder = orderRepository.findOne(orderId);
    assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
    assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
  }

  @Test
  public void 주문검색() throws Exception {
    // given
    Member member = createMember("회원1");
    Member member2 = createMember("회원2");
    Book book = createBook("시골 JPA", 5000, 10);
    Book book2 = createBook("도시 JPA", 10000, 20);
    Long orderId = orderService.order(member.getId(), book.getId(), 2);
    Long orderId2 = orderService.order(member.getId(), book2.getId(), 3);
    Long orderId3 = orderService.order(member2.getId(), book.getId(), 4);
    orderRepository.findOne(orderId).cancel();
    Order order = orderRepository.findOne(orderId);
    Order order2 = orderRepository.findOne(orderId2);
    Order order3 = orderRepository.findOne(orderId3);

    // when
    // 주문자가 "회원1"인 주문 검색
    OrderSearch search = new OrderSearch();
    search.setMemberName("회원1");
    List<Order> orders = orderRepository.findAll(search);
    // 전체 검색
    OrderSearch search2 = new OrderSearch();
    List<Order> orders2 = orderRepository.findAll(search2);
    // 상태가 "ORDER"인 주문 검색
    OrderSearch search3 = new OrderSearch();
    search3.setOrderStatus(OrderStatus.ORDER);
    List<Order> orders3 = orderRepository.findAll(search3);

    // then
    // 검색결과 1(회원1)
    assertEquals("결과1의 결과 수는 2이다.", 2, orders.size());
    assertEquals("결과1에 주문1이 포함되어 있어야 한다.", true, orders.contains(order));
    assertEquals("결과1에 주문2가 있어야 한다.", true, orders.contains(order2));
    assertEquals("결과1에 주문3이 포함되어 있으면 안된다.", false, orders.contains(order3));
    // 검색결과 2(전체)
    assertEquals("결과2의 결과 수는 2이다.", 3, orders2.size());
    assertEquals("결과2에 주문1이 포함되어 있어야 한다.", true, orders2.contains(order));
    assertEquals("결과2에 주문2가 포함되어 있어야 한다.", true, orders2.contains(order2));
    assertEquals("결과2에 주문3이 포함되어 있어야 한다.", true, orders2.contains(order3));
    // 검색결과 3(주문상태 ORDER)
    assertEquals("결과3의 결과 수는 2이다.", 2, orders3.size());
    assertEquals("결과3에 주문1이 포함되어 있으면 안된다.", false, orders3.contains(order));
    assertEquals("결과3에 주문2가 포함되어 있어야 한다.", true, orders3.contains(order2));
    assertEquals("결과3에 주문3이 포함되어 있어야 한다.", true, orders3.contains(order3));
  }

  private Member createMember(String name) {
    Member member = new Member();
    member.setName(name);
    member.setAddress(new Address("서울", "강가", "123-123"));
    em.persist(member);
    return member;
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }
}
