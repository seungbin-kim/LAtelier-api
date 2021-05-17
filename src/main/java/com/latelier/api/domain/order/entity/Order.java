package com.latelier.api.domain.order.entity;

import com.latelier.api.domain.model.BaseTimeEntity;
import com.latelier.api.domain.order.enumeration.OrderStatus;
import com.latelier.api.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
    name = "ORDER_SEQ_GENERATOR",
    sequenceName = "ORDER_SEQ",
    allocationSize = 10)
public class Order extends BaseTimeEntity {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "ORDER_SEQ_GENERATOR")
  @Column(columnDefinition = "bigint")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", columnDefinition = "bigint")
  private User user;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

}
