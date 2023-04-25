package com.coderscampus.asmaassignment13.web;

import java.util.ArrayList;
import java.util.List;
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

		Boolean boolValue = false;
		Long newAccountId = 0L;
		Set<User> users = userService.findAll();
		for (User user : users) {
			if (user.getUserId().equals(userId)) {

				Account newAccount = userService.addAccount(user);
				newAccountId = newAccount.getAccountId();
				model.put("account", newAccount);
				model.addAttribute("boolValue", boolValue);
				model.put("user", user);
				session.setAttribute("boolValue", boolValue);
			}
		}

		return "redirect:/users/" + userId + "/accounts/" + newAccountId;
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccount(@PathVariable Long userId, @PathVariable Long accountId, ModelMap model,
			HttpSession session) {

		Boolean boolValue = false;

		Set<User> users = userService.findAll();
		for (User user : users) {
			if (user.getUserId().equals(userId)) {
				List<Account> accounts = user.getAccounts();
				for (Account account : accounts) {
					if (account.getAccountId().equals(accountId)) {

						model.put("account", account);
						model.addAttribute("boolValue", boolValue);
						session.setAttribute("boolValue", boolValue);
						break;
					}
				}
				model.put("user", user);
				break;
			}
		}

		return "accounts";
	}

	@PostMapping("users/{userId}/accounts/{accountId}")
	public String postUserAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId,
			@RequestParam("accountName") String accountName) {

		Boolean boolValue = false;
		Set<User> users = userService.findAll();
		List<Account> accounts = new ArrayList<>();
		for (User user : users) {
			if (user.getUserId().equals(userId)) {
				accounts = user.getAccounts();
				for (Account account : accounts) {
					if (account.getAccountId().equals(accountId)) {
						account.setAccountName(accountName);
						accountService.save(account);
						model.put("account", account);
						break;
					}
				}

				int accountCount = accounts.size();
				model.put("user", user);
				model.put("accountCount", accountCount);
				model.put("accounts", accounts);
				model.addAttribute("boolValue", boolValue);
				break;
			}

		}
		return "redirect:/users/" + userId + "/accounts/" + accountId;
	}
}