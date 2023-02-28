package com.secondproject.monthlycoffee.repository;

import java.util.Optional;

import com.secondproject.monthlycoffee.entity.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.CommentInfo;
import com.secondproject.monthlycoffee.entity.PostInfo;

public interface CommentInfoRepository extends JpaRepository<CommentInfo, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from CommentInfo c where c.post = :post")
    void deleteAllByPostInBatch(@Param("post") PostInfo post);

    @Modifying(clearAutomatically = true)
    @Query("delete from CommentInfo c where c.member = :member")
    void deleteByMember(@Param("member") MemberInfo member);

    @Override
    @Modifying(clearAutomatically = true)
    void delete(CommentInfo entity);

    @Query("select c from CommentInfo c join c.member m where c.id = :id and m.id = :memberId")
    Optional<CommentInfo> findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);


    long countByPost(PostInfo post);

    boolean existsByMember(MemberInfo member);
}
