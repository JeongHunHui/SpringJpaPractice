package com.deca.SpringJpaPractice.domain.order.repository;

import com.deca.SpringJpaPractice.domain.member.entity.QMember;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import com.deca.SpringJpaPractice.domain.order.entity.OrderStatus;
import com.deca.SpringJpaPractice.domain.order.entity.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.deca.SpringJpaPractice.domain.member.entity.QMember.member;
import static com.deca.SpringJpaPractice.domain.order.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
  private final EntityManager em;

  public void save(Order order) {
    em.persist(order);
  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }

  public List<Order> findAll(OrderSearch orderSearch) {
    // order와 order와 연관된 member를 join
    // 검색 조건에 따라서 동적인 쿼리가 필요
    // ex) 조건에 이름이 ??인 or 모든 이름 + 주문 상태가 NULL, ORDER, CANCEL 인지
    QOrder order = QOrder.order;
    QMember member = QMember.member;
    JPAQuery<Order> query = new JPAQuery<>(em);

    return query
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(statusEq(orderSearch.getOrderStatus()),
                    nameLike(orderSearch.getMemberName()))
            .limit(1000)
            .fetch();
  }

  private BooleanExpression statusEq(OrderStatus statusCond) {
    if (statusCond == null){
      return null;
    }
    return order.status.eq(statusCond);
  }

  private BooleanExpression nameLike(String nameCond) {
    if (!StringUtils.hasText(nameCond)){
      return null;
    }
    return member.name.like(nameCond);
  }
}
