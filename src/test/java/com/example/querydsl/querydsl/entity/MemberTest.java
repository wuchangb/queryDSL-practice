package com.example.querydsl.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void memberTest() {
        Team teamA = new Team("A");
        em.persist(teamA);

        Member memberA = new Member("one", 10,teamA);
        Member memberB = new Member("two", 20);
        Member memberC = new Member("three", 30);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);

        List<Member> list = em.createQuery("select m from Member m where m.team.name = 'A'", Member.class).getResultList();
        for (Member m : list) {
            System.out.println("m = " + m.getTeam().getName());
        }

    }

}