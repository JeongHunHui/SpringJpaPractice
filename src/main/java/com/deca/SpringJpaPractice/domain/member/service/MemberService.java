package com.deca.SpringJpaPractice.domain.member.service;

import com.deca.SpringJpaPractice.domain.member.entity.Member;
import com.deca.SpringJpaPractice.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 데이터 변경은 트랜젝셔널이 있어야함
// -> public 메소드 들은 다 걸림 @Transactional에
@Transactional(readOnly = true) // 읽기 전용, 성능 최적화
/*
  @AutoWired
  public MemberService(MemberRepository memberRepository) {
      this.memberRepository = memberRepository;
  }
  // @RequiredArgsConstructor는 final인 필드만으로 생성자 만들어줌
  // -> 위의 생성자 주입 코드를 대체해줌(Lombok)
*/
@RequiredArgsConstructor
public class MemberService {
  // 변경될 일이 없기 때문 final
  private final MemberRepository memberRepository;

  /** 회원 가입 */
  @Transactional
  public Long join(Member member) {
    // 중복 회원 검사(중복 시 에러 발생)
    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  /** 중복 회원 검사 */
  // 이 메서드가 동시에 호출될 경우 동일한 이름의 회원 생성 가능
  // 그러므로 DB에 Unique 제약조건 등의 최후의 수단 추가하기
  private void validateDuplicateMember(Member member) {
    // EXCEPTION
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  /** 회원 전체 조회 */
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  /** id로 회원 조회 */
  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }
}
