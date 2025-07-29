package com.narvee.Service;


import com.narvee.request.dto.AuthResponse;

public interface IAuthenticationService {

	AuthResponse authenticate(String usernameOrEmail);

}
