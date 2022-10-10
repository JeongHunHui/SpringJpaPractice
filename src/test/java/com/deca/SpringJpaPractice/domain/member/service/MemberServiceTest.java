package com.deca.SpringJpaPractice.domain.member.service;

import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.member.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
// 테스트에서 사용시 rollback시킴
@Transactional
public class MemberServiceTest {
  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;

  @Test
  // @Transactional로 인해 모든 내용이 rollback됨
  // @Rollback(false)을 붙혀주면 롤백 하지 않음(DB에 반영됨)
  public void 회원가입() throws Exception {
    // given
    Member member = new Member();
    member.setName("kim");

    // when
    Long memberId = memberService.join(member);

    // then
    // assertEquals(예상값, 실제값) -> 두 값이 다르면 테스트 실패
    // assertEquals(테스트 실패시 표시할 메세지, 예상값, 실제값) 이렇게도 가능
    // assertEquals(테스트 실패시 표시할 메세지, 예상값, 실제값, 허용 오차범위) 이렇게도 가능
    assertEquals(member, memberRepository.findOne(memberId));
  }

  // IllegalStateException이 발생하면 테스트 성공
  @Test(expected = IllegalStateException.class)
  public void 중복회원체크() throws Exception {
    // given
    Member member1 = new Member();
    member1.setName("kim");

    Member member2 = new Member();
    member2.setName("kim");

    // when
    memberService.join(member1);
    memberService.join(member2);

    // then
    // 위에서 에러가 발생 하지 않으면 중복 체크가 실패한 것 이므로
    // fail로 실패했다고 알려줘야함
    fail("중복 체크 실패");
  }
}
