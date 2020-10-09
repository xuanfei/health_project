package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    public Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);
}
