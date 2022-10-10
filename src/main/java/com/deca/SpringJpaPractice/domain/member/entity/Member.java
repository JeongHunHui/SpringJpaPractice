package com.deca.SpringJpaPractice.domain.member.entity;

import com.deca.SpringJpaPractice.domain.address.entity.Address;
import com.deca.SpringJpaPractice.domain.order.entity.Order;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
  @Id
  @Column(name = "member_id")
  @GeneratedValue
  private Long id;

  private String name;

  // Jpa 내장타입
  @Embedded private Address address;

  // mappedBy = "member" -> Order 테이블의 member와 매핑 됐다는 뜻
  @OneToMany(mappedBy = "member")
  private List<Order> orders = new ArrayList<>();
}
