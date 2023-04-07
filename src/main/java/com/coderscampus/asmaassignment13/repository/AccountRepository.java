package com.coderscampus.asmaassignment13.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.asmaassignment13.domain.Account;
import com.coderscampus.asmaassignment13.domain.User;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	List<Account> findByUsersContaining(User user);

}
