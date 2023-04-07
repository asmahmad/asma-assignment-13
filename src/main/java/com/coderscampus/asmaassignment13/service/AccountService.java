package com.coderscampus.asmaassignment13.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.User;
import com.coderscampus.asmaassignment13.repository.AccountRepository;
import com.coderscampus.asmaassignment13.repository.UserRepository;
@Service
public class AccountService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	private AccountRepository accountRepo;
	
	public Optional<Account> findbyId(Long accountId) {
		
		return accountRepo.findById(accountId);
	}

	public void save(Account account) {
		
	     accountRepo.save(account);
		
	}
	public void saveAllAccount(List<Account> accounts) {
		
	     accountRepo.saveAll(accounts);
		
	}

	public List<Account> findByUsersContaining(User user) {
		
		return accountRepo.findByUsersContaining(user);
	}

	public void saveAll(List<Account> accounts) {
		accountRepo.saveAll(accounts);
		
	}


}
