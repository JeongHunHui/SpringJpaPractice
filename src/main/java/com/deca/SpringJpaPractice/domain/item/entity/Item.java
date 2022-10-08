package com.deca.SpringJpaPractice.domain.item.entity;

import com.deca.SpringJpaPractice.domain.category.entity.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// 상속관계 전략 -> 싱글테이블 전략
// Table Per Class 상속받는 객체마다 테이블 생성
// Joined 정규화된?
// Single Table 한 테이블에 다 넣기
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// book이면 어케할거냐? album이면 어케할거냐?
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {
  @Id
  @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();
}
