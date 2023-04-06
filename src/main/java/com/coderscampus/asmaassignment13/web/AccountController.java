package com.coderscampus.asmaassignment13.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.User;
import com.coderscampus.asmaassignment13.service.AccountService;
import com.coderscampus.asmaassignment13.service.UserService;

@Controller
public class AccountController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;

	@PostMapping("users/{userId}/accounts")
	public String postUserAccount(ModelMap model, @PathVariable Long userId, HttpSession session) {
		System.out.println("Hi........");
		Boolean boolValue = false;
		Long newAccountId = 0L;
		Set<User> users = userService.findAllUsersWithAccountsAndAddresses();
		for (User user : users) {
			if (user.getUserId().equals(userId)) {
				Account newAccount = userService.addAccount(user);
				newAccountId = newAccount.getAccountId();
				model.put("account", newAccount);
				model.addAttribute("boolValue", boolValue);
				session.setAttribute("boolValue", boolValue);
				System.out.println("users/userId/accounts........	" + boolValue);
			}
		}

		return "redirect:/users/" + userId + "/accounts/" + newAccountId;
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccount(@PathVariable String userId, @PathVariable String accountId, ModelMap model,
			HttpSession session) {
		Boolean boolValue = false;
		Long userIdL = Long.parseLong(userId);
		Long accountIdL = Long.parseLong(accountId);
		// Your code logic here
		// Optional<Account> account = accountService.findbyId(accountIdL);

		Set<User> users = userService.findAllUsersWithAccountsAndAddresses();
		for (User user : users) {
			if (user.getUserId().equals(userIdL)) {
				List<Account> accounts = user.getAccounts();
				for (Account account : accounts) {
					if (account.getAccountId().equals(accountIdL)) {

						model.put("account", account);
						model.addAttribute("boolValue", boolValue);
						session.setAttribute("boolValue", boolValue);
						System.out.println("users/userId/accounts/accountIdGETMAPPING........	" + boolValue);
					}
				}
			}
			model.put("user", user);
		}

		return "accounts";
	}

	@PostMapping("users/{userId}/accounts/{accountId}")
	public String postUserAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId,
			@RequestParam("accountName") String accountName) {
//		List<Object[]> accounts = userService.findAccountbyUserId(userId);
//		User user = userService.findById(userId);
//		//List<Account> accounts = accountService.getUserAccounts(userId);
//		model.put("accounts", accounts);
//		model.put("user", user);
//		return "accounts";
		Boolean boolValue = false;
		Set<User> user = userService.findAllUsersWithAccountsAndAddresses();
		List<Account> accounts = new ArrayList<>();

		// List<Object[]> accounts = userService.findAccountbyUserId(userId);
		for (User u : user) {
			if (u.getUserId().equals(userId)) {
				accounts = u.getAccounts();
				for (Account a : accounts) {
					if (a.getAccountId().equals(accountId)) {
						a.setAccountName(accountName);
						accountService.save(accountName, a);
						model.put("account", a);
						break;
					}
				}
				int accountCount = accounts.size();
				model.put("user", u);
				model.put("accountCount", accountCount);
				model.put("accounts", accounts);
				model.addAttribute("boolValue", boolValue);
				System.out.println("users/userId..accountId Post......	" + boolValue);
				break;
			}
		}
		return "redirect:/users/" + userId + "/accounts/" + accountId;
	}
}