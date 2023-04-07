package com.coderscampus.asmaassignment13.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.Address;
import com.coderscampus.asmaassignment13.domain.User;
import com.coderscampus.asmaassignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model, HttpSession session) {
		Set<User> users = userService.findAll();
		Boolean boolValue = true;
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		model.addAttribute("boolValue", boolValue);
		session.setAttribute("boolValue", boolValue);

		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId,
			@SessionAttribute("boolValue") Boolean boolValue) {

		Set<User> user = userService.findAll();
		List<Account> accounts = new ArrayList<>();
		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				accounts = u.getAccounts();

				int accountCount = accounts.size();
				model.put("user", u);
				model.put("accountCount", accountCount);
				model.put("accounts", accounts);
				model.addAttribute("boolValue", boolValue);
				break;
			}
		}
		model.put("users", Arrays.asList(user));
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(User user, @PathVariable Long userId, Address address) {
		Set<User> allUsers = userService.findAll();
		for (User foundUser : allUsers) {

			foundUser.setName(user.getName());
			foundUser.setPassword(user.getPassword());
			foundUser.setUsername(user.getUsername());
			userService.updateAddress(user, address);
			userService.saveUser(foundUser, address);

		}	
		return "redirect:/users/" + user.getUserId();
	}
	
	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		
		return "redirect:/users";
	}

}
