package com.library.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	@Column(unique=true)
	private String email;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	 @Enumerated(EnumType.STRING)
	 private MembershipStatus membershipStatus;

	 private boolean suspended;

	 private LocalDate membershipExpiryDate;

	 public Long getId() {
		 return id;
	 }

	 public void setId(Long id) {
		 this.id = id;
	 }

	 public String getFirstName() {
		 return firstName;
	 }

	 public void setFirstName(String firstName) {
		 this.firstName = firstName;
	 }

	 public String getLastName() {
		 return lastName;
	 }

	 public void setLastName(String lastName) {
		 this.lastName = lastName;
	 }

	 public String getEmail() {
		 return email;
	 }

	 public void setEmail(String email) {
		 this.email = email;
	 }

	 public String getPassword() {
		 return password;
	 }

	 public void setPassword(String password) {
		 this.password = password;
	 }

	 public Role getRole() {
		 return role;
	 }

	 public void setRole(Role role) {
		 this.role = role;
	 }

	 public MembershipStatus getMembershipStatus() {
		 return membershipStatus;
	 }

	 public void setMembershipStatus(MembershipStatus membershipStatus) {
		 this.membershipStatus = membershipStatus;
	 }

	 public boolean isSuspended() {
		 return suspended;
	 }

	 public void setSuspended(boolean suspended) {
		 this.suspended = suspended;
	 }

	 public LocalDate getMembershipExpiryDate() {
		 return membershipExpiryDate;
	 }

	 public void setMembershipExpiryDate(LocalDate membershipExpiryDate) {
		 this.membershipExpiryDate = membershipExpiryDate;
	 }
	 
	 
}
