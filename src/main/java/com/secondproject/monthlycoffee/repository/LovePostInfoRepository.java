package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.LovePostInfo;
import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface LovePostInfoRepository extends JpaRepository<LovePostInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from LovePostInfo l where l.post = :post")
    void deleteAllByPostInBatch(@Param("post") PostInfo post);

    @Modifying(clearAutomatically = true)
    @Query("delete from LovePostInfo l where l.member = :member")
    void deleteByMember(@Param("member") MemberInfo member);

    Optional<LovePostInfo> findByPostAndMember(PostInfo post, MemberInfo member);

    long countByPost(PostInfo post);

    boolean existsByMember(MemberInfo member);

    @Query("select count(*) > 0 from LovePostInfo l join l.post p join l.member m where p = :post and m.id = :memberId")
    boolean existsByPostAndMemberId(@Param("post") PostInfo postInfo, @Param("memberId") Long memberId);
}
