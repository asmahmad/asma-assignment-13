package com.coderscampus.asmaassignment13.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coderscampus.asmaassignment13.domain.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
