package com.stala.grzegorz.service;

import com.stala.grzegorz.model.User;
import com.stala.grzegorz.model.UserShipping;
import com.stala.grzegorz.model.security.PasswordResetToken;
import com.stala.grzegorz.model.security.UserRole;

import java.util.Set;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);

	void updateUserShipping(UserShipping userShipping, User user);

	void setUserDefaultShipping(Long userShippingId, User user);
}
