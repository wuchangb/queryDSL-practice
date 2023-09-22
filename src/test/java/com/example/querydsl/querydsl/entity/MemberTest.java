package com.example.querydsl.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.List;

import static com.example.querydsl.querydsl.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;


    @BeforeEach
    public void before() {
        Team teamA = new Team("A");
        Team teamB = new Team("B");
        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 10,teamA);
        Member memberB = new Member("memberB", 20, teamA);
        Member memberC = new Member("memberC", 30, teamB);
        Member memberD = new Member("memberD", 40, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
    }
    @Test
    public void memberTest() {

        Team teamA = new Team("A");
        em.persist(teamA);

        Member memberA = new Member("one", 10, teamA);
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

    @Test
    public void JPQL() {

        String query = "select m from Member m " +
                "where m.username = :name";

        Member findMember = em.createQuery(query, Member.class)
                .setParameter("name", "memberA")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }

    @Test
    public void queryDSL() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Member findMember = queryFactory.selectFrom(member)
                .where(member.username.eq("memberA"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("memberA");
    }
}