package org.globalkinetic.jwt.repository;

import org.globalkinetic.jwt.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	public AppUser findOneByUsername(String username);
	 public AppUser findByUsername( String username );
}
