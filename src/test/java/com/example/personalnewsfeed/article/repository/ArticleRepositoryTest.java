package com.example.personalnewsfeed.article.repository;

import com.example.personalnewsfeed.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 삭제되지_않은_게시글_전체를_가져올_수_있다() {}

}