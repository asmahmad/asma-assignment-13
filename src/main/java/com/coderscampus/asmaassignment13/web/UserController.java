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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.User;
import com.coderscampus.asmaassignment13.service.AccountService;
import com.coderscampus.asmaassignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		List<Account> accounts = user.getAccounts();

		userService.saveUser(user);
		accountService.saveAll(accounts);
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
	public String getOneUser(ModelMap model, @PathVariable Long userId, @SessionAttribute("boolValue") Boolean boolValue) {
		// User user = userService.findById(userId);
		//boolean boolValue = (Boolean) model.getAttribute("boolValue");
	
		Set<User> user = userService.findAllUsersWithAccountsAndAddresses();
		List<Account> accounts = new ArrayList<>();
		// List<Object[]> accounts = userService.findAccountbyUserId(userId)
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
		System.out.println("users/userId........	" + boolValue);
		model.put("users", Arrays.asList(user));
		// model.put("user", user.iterator().next());
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(User user, @PathVariable Long userId) {
		user.setUserId(userId);
		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();

	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

}
