package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.Invitations;

@Repository
public interface InvitationsRepo extends JpaRepository<Invitations, Long>{

}
