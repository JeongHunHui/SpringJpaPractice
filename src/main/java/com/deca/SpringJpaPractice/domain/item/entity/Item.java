package com.deca.SpringJpaPractice.domain.item.entity;

import com.deca.SpringJpaPractice.domain.category.entity.Category;
import com.deca.SpringJpaPractice.domain.item.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
// 상속관계 전략
// Table Per Class 상속받는 객체마다 테이블 생성
// Joined 정규화된?
// Single Table 한 테이블에 다 넣기 -> 아래 에선 이걸 사용
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

  // == 비즈니스 로직 == //
  // 엔티티가 주도적으로 해결할 수 있는 경우는 엔티티 안에 비즈니스 로직 넣는게 좋음
  /** stock 증가 */
  public void addStock(int quantity) {
    this.stockQuantity = quantity;
  }
  /** stock 감소 */
  public void removeStock(int quantity) {
    int restStock = this.stockQuantity - quantity;
    if (restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = restStock;
  }
}

// -> Item 테이블에 Item을 상속받은 엔티티의 필드들이 전부 들어감
// ex) Item을 상속받은 book의 author, isbn 이 Item 테이블의 컬럼에 포함되어있음
