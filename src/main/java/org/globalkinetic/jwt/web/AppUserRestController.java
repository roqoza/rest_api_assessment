package org.globalkinetic.jwt.web;

import java.util.ArrayList;
import java.util.List;

import org.globalkinetic.jwt.domain.AppUser;
import org.globalkinetic.jwt.repository.AppUserRepository;
import org.globalkinetic.jwt.service.impl.AppUserDetailsService;
import org.globalkinetic.jwt.util.JwtTokenUtil;
import org.globalkinetic.jwt.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for authentication and user details. All the web services of
 * this rest controller will be only accessible for ADMIN users only 
 */
@RestController
@RequestMapping(value = "/api")
public class AppUserRestController {
	
	@Autowired
    private AppUserDetailsService userDetailsService;
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private TimeProvider timeProvider;
        
	/**
	 * Web service for getting all the appUsers in the application.
	 * 
	 * @return list of all AppUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<AppUser> users() {
		List<AppUser> users = appUserRepository.findAll();
		return users;
	}
	
	/**
	 * Web service for getting all the authenticated appUsers in the application.
	 * 
	 * @return list of all AppUser
	 */	
	@RequestMapping(value = "/auth-users", method = RequestMethod.GET)
	public List<AppUser> authUsers() {
		List<AppUser> authenticatedUsers = new ArrayList<>();		
		List<AppUser> users = appUserRepository.findAll();
		for(AppUser user: users) {
			if(user.getToken() != null) {
				 UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
				 if(!(user.getToken().equals(null))) {
					 if(jwtTokenUtil.validateToken(user.getToken(), userDetails)) {
						 authenticatedUsers.add(user);
					 }
				 }				 
			}
		}
		 return authenticatedUsers;
	}
	
	/**
	 * Web service for getting all the users that have called login within the last 5 minutes.
	 * 
	 * @return list of all AppUser
	 */	
	@RequestMapping(value = "/last-login", method = RequestMethod.GET)
	public List<AppUser> lastLoginUsers() {
		List<AppUser> loginUsers = new ArrayList<>();		
		List<AppUser> users = appUserRepository.findAll();
		for(AppUser user: users) {
			if((timeProvider.lastLogin(user) < 5) && (user.getToken() != null)) {
				loginUsers.add(user);
			}			
		}
		 return loginUsers;
	}

	/**
	 * Web service for getting a user by his ID
	 * 
	 * @param id
	 *            appUser ID
	 * @return appUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<AppUser> userById(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}
	}

	/**
	 * Method for deleting a user by his ID
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<AppUser> deleteUser(@PathVariable Long id) {
		AppUser appUser = appUserRepository.findOne(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedUsername = auth.getName();
		if (appUser == null) {
			return new ResponseEntity<AppUser>(HttpStatus.NO_CONTENT);
		} else if (appUser.getUsername().equalsIgnoreCase(loggedUsername)) {
			throw new RuntimeException("You cannot delete your account");
		} else {			
			appUserRepository.delete(appUser);
			return new ResponseEntity<AppUser>(appUser, HttpStatus.OK);
		}

	}

	/**
	 * Method for adding a appUser
	 * 
	 * @param appUser
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null) {
			throw new RuntimeException("Username already exist");
		}
		appUser.setToken(null);
		AppUser user = appUserRepository.save(appUser);
		return new ResponseEntity<AppUser>(user, HttpStatus.CREATED);
	}

	/**
	 * Method for editing an user details
	 * 
	 * @param appUser
	 * @return modified appUser
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.PUT)
	public AppUser updateUser(@RequestBody AppUser appUser) {
		if (appUserRepository.findOneByUsername(appUser.getUsername()) != null
				&& appUserRepository.findOneByUsername(appUser.getUsername()).getId() != appUser.getId()) {
			throw new RuntimeException("Username already exist");
		}
		return appUserRepository.save(appUser);
	}
}
