package com.coderscampus.asmaassignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.Address;
import com.coderscampus.asmaassignment13.domain.User;
import com.coderscampus.asmaassignment13.repository.AddressRepository;
import com.coderscampus.asmaassignment13.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AddressRepository addressRepo;

	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findByNameAndUsername(String name, String username) {
		return userRepo.findByNameAndUsername(name, username);
	}

	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}

	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() > 0)
			return users.get(0);
		else
			return new User();
	}

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public User findById(Long userId) {
		
		java.util.Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	public User saveUser(User user) {
		if (user.getUserId() == null) {
			
			setDefaultAccounts(user);
		}

		if (user.getAddress() == null) {
			
			Address address = new Address();
			setDefaultAddress(user, address);
			
		} else {
			
			Address address = new Address();
			updateAddress(user, address);
		}

		return userRepo.save(user);

	}

	private void setDefaultAddress(User user, Address address) {
		address.setAddressLine1("123 Fake Street");
		address.setAddressLine2("Unit 14");
		address.setCity("Some City");
		address.setCountry("Some Country");
		address.setRegion("Some Region");
		address.setZipCode("12345");
		address.setUser(user);
		address.setUserId(user.getUserId());
		user.setAddress(address);
	}

	private void setDefaultAccounts(User user) {
		Account checking = new Account();
		checking.setAccountName("Checking Account");
		checking.getUsers().add(user);
		Account savings = new Account();
		savings.setAccountName("Savings Account");
		savings.getUsers().add(user);

		user.getAccounts().add(checking);
		user.getAccounts().add(savings);
		accountService.save(checking);
		accountService.save(savings);
	}

	public void updateAddress(User user, Address address) {
		address.setAddressLine1(user.getAddress().getAddressLine1());
		address.setAddressLine2(user.getAddress().getAddressLine2());
		address.setCity(user.getAddress().getCity());
		address.setCountry(user.getAddress().getCountry());
		address.setRegion(user.getAddress().getRegion());
		address.setZipCode(user.getAddress().getZipCode());
		address.setUser(user);
		address.setUserId(user.getUserId());
		user.setAddress(address);
	}
	
	public void saveUser(User user, Address address) {

		user.setAddress(address);
		address.setUser(user);
		addressRepo.save(address);
		userRepo.save(user);

	}

	public void delete(Long userId) {
		
		userRepo.deleteById(userId);
		
	}

	public Account addAccount(User user) {
		Account account = new Account();
		int accountCount = user.getAccounts().size() + 1;
		account.setAccountName("Account#" + accountCount);
		user.getAccounts().add(account);
		account.getUsers().add(user);
		accountService.save(account);
		userRepo.save(user);
		return account;

	}

}
