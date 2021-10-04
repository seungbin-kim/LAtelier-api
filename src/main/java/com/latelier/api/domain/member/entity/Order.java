package com.latelier.api.domain.member.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.member.enumeration.OrderState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "ORDER_SEQ_GENERATOR",
        sequenceName = "ORDER_SEQ")
@Table(name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "imp_uid_unique", columnNames = {"impUid"})})
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ORDER_SEQ_GENERATOR")
    @Column(columnDefinition = "bigint")
    private Long id;

    @Column(length = 30)
    private String impUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint")
    private Member member;

    @Column(length = 60)
    private String name;

    @Column(columnDefinition = "int")
    private Integer totalPrice;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @OneToMany(mappedBy = "order")
    private List<OrderCourse> orderCourses = new ArrayList<>();

    private Order(final String impUid,
                  final Member member,
                  final String name,
                  final Integer totalPrice,
                  final OrderState state) {

        this.impUid = impUid;
        this.member = member;
        this.name = name;
        this.totalPrice = totalPrice;
        this.state = state;
    }


    public static Order of(final String impUid,
                           final Member member,
                           final String name,
                           final Integer totalPrice,
                           final OrderState state) {

        return new Order(impUid, member, name, totalPrice, state);
    }

}
