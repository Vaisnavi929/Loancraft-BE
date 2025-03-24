package com.narvee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_invitation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invitations {
  @Id
  @GeneratedValue(strategy =  GenerationType.IDENTITY)
  private Long invitationId;
  private String name;
  private String email;
  private String phNo;
  @Column (name = "refereedBy")
  private Long userId;
}
