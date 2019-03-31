package com.intuit.craftdemoapps.api.intuitamigo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class UserSessionUtil {

	private UserSessionUtil() {
		
	}
	
	public static String getUsername() {
		SecurityContext secureContext = SecurityContextHolder.getContext();
		Authentication auth = secureContext.getAuthentication();
		Object principal = auth.getPrincipal();

		String username = null;
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			username = userDetails.getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}
}
