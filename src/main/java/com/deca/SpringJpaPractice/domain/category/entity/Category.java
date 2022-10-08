package com.deca.SpringJpaPractice.domain.category.entity;

import com.deca.SpringJpaPractice.domain.item.entity.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
  @Id
  @GeneratedValue
  @Column(name = "category_id")
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
      name = "category_item",
      joinColumns = @JoinColumn(name = "category_id"),
      inverseJoinColumns = @JoinColumn(name = "itme_id"))
  private List<Item> items = new ArrayList<>();

  // 이름만 같지만 다른 엔티티랑 매핑하는 것 처럼 하면 됨
  @ManyToOne
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();
}
