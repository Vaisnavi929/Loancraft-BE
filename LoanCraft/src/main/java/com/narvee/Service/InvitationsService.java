package com.narvee.Service;

import com.narvee.entity.Invitations;

import jakarta.mail.MessagingException;

public interface InvitationsService {
	
	public String saveInvititions(Invitations invitations) throws MessagingException;
	//public String sendInvitation(Invitations invitations) throws MessagingException ;
}
