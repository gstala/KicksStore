package com.stala.grzegorz;

import com.stala.grzegorz.model.Size;
import com.stala.grzegorz.model.User;
import com.stala.grzegorz.model.security.Role;
import com.stala.grzegorz.model.security.UserRole;
import com.stala.grzegorz.service.SizeService;
import com.stala.grzegorz.service.UserService;
import com.stala.grzegorz.utility.Constants;
import com.stala.grzegorz.utility.SecurityUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StoreApplication implements CommandLineRunner{


	@Autowired
	private UserService userService;

	@Autowired
	private SizeService sizeService;

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("John");
		user1.setLastName("Adams");
		user1.setUsername("john");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("gresek23@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));

		userService.createUser(user1, userRoles);

		User user2 = new User();
		user2.setFirstName("Grzegorz");
		user2.setLastName("Stala");
		user2.setUsername("grzech");
		user2.setPassword(SecurityUtility.passwordEncoder().encode("g"));
		user2.setEmail("gstala01@gmail.com");
		Set<UserRole> userRoles2 = new HashSet<>();
		Role role2= new Role();
		role2.setName("ROLE_ADMIN");
		userRoles2.add(new UserRole(user2, role2));

		userService.createUser(user2, userRoles2);

		Constants.PRODUCT_SIZE_MAP.forEach((size, length) -> sizeService.save(new Size(size,length)));

	}
}
