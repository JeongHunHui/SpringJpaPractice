package com.deca.SpringJpaPractice.domain.item.repository;

import com.deca.SpringJpaPractice.domain.item.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
  private final EntityManager em;

  public void save(Item item) {
    // DB에 없으면 생성, 있으면 업데이트
    if (item.getId() == null) {
      em.persist(item);
    } else {
      em.merge(item);
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("select i from Item i", Item.class).getResultList();
  }
}
