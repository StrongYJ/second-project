package com.secondproject.monthlycoffee.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.secondproject.monthlycoffee.entity.MemberInfo;
import com.secondproject.monthlycoffee.entity.type.LikeHate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.secondproject.monthlycoffee.entity.ExpenseInfo;

public interface ExpenseInfoRepository extends JpaRepository<ExpenseInfo, Long> {
    @Query("select e from ExpenseInfo e join e.member m where e.id = :id and m.id = :memberId")
    Optional<ExpenseInfo> findByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
    List<ExpenseInfo> findByMember(MemberInfo member);
    @Query(value = "select e from ExpenseInfo e join e.member m where date_format(e.date, '%y%m') between :startDate and :endDate and m.id = :memberId")
    List<ExpenseInfo> searchTotalExpense(@Param("startDate") Integer startDate, @Param("endDate") Integer endDate, @Param("memberId") Long memberId);
    @Query(value = "select e from ExpenseInfo e join e.member m where date_format(e.date, '%y%m') = :date and e.likeHate = :keyword and m.id = :memberId")
    List<ExpenseInfo> searchtotalList(@Param("date") Integer date, @Param("keyword") LikeHate keyword, @Param("memberId") Long memberId);
    @Query(value = "select e from ExpenseInfo e join e.member m where e.likeHate = :keyword and m.id = :memberId")
    List<ExpenseInfo> searchLikeHate(@Param("keyword") LikeHate keyword, @Param("memberId") Long memberId);
    @Query(value = "select e from ExpenseInfo e join e.member m where date_format(e.date, '%y%m') = :date and m.id = :memberId")
    List<ExpenseInfo> searchDate(@Param("date") Integer date, @Param("memberId") Long memberId);
    @Query(value = "select e from ExpenseInfo e join e.member m where date_format(e.date, '%y%m') = :date and e.category = :keyword and m.id = :memberId")
    List<ExpenseInfo> searchCategory(@Param("date") Integer date, @Param("keyword") String keyword, @Param("memberId") Long memberId);
    @Query(value = "select e from ExpenseInfo e join e.member m where date_format(e.date, '%y%m') = :date and e.brand = :keyword and m.id = :memberId")
    List<ExpenseInfo> searchBrand(@Param("date") Integer date, @Param("keyword") String keyword, @Param("memberId") Long memberId);
    @Query("select e from ExpenseInfo e where e.member = :member and e.date between :start and :end order by e.date")
    List<ExpenseInfo> findByYearMonth(@Param("member") MemberInfo member, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
