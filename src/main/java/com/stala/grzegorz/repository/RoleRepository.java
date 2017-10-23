package com.stala.grzegorz.repository;

import com.stala.grzegorz.model.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
