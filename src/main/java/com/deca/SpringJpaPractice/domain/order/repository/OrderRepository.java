package com.deca.SpringJpaPractice.domain.order.repository;

import com.deca.SpringJpaPractice.domain.member.entity.QMember;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import com.deca.SpringJpaPractice.domain.order.entity.OrderStatus;
import com.deca.SpringJpaPractice.domain.order.entity.QOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

  private final JPAQueryFactory jpaQueryFactory;

  public void save(Order order) {
    em.persist(order);
  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }

  public List<Order> findAll(OrderSearch orderSearch) {

    QOrder order = QOrder.order;
    QMember member = QMember.member;

    // 검색 조건에 따라서 동적인 쿼리가 필요
    // ex) 이름 = ??, 주문상태 = ?? 등으로 필터링 가능
    return jpaQueryFactory
            // order 테이블에서(select(order).from(order)와 동일)
            .selectFrom(order).
            // order와 order와 연관된 member를 join
            .join(order.member, member)
            // 검색 조건의 주문상태가 order의 주문상태와 같은가?
            .where(statusEq(orderSearch.getOrderStatus()),
                    // 검색 조건의 이름이 회원의 이름과 같은가?
                    nameLike(orderSearch.getMemberName()))
            // 검색결과 1000개로 제한
            .limit(1000)
            // 결과 조회
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
