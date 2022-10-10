package com.deca.SpringJpaPractice.domain.member.repository;

import com.deca.SpringJpaPractice.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 컴포넌트 스캔으로 자동으로 스프링 빈에 등록됨
@Repository
@RequiredArgsConstructor
public class MemberRepository {
  // @PersistenceContext를 붙히면 스프링이 EntityManager를 만들어서 주입해줌
  // 생성자 주입도 가능
  // 스프링 Data JPA에서는 @PersistenceContext 대신 @AutoWired 가능
  // -> Service와 같이 이런식으로 의존성 주입이 가능
  private final EntityManager em;

  public void save(Member member) {
    em.persist(member);
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  public List<Member> findAll() {
    return em.createQuery("select m from Member m", Member.class).getResultList();
  }

  public List<Member> findByName(String name) {
    return em.createQuery("select m from Member m where m.name = :name", Member.class)
        .setParameter("name", name)
        .getResultList();
  }
}
