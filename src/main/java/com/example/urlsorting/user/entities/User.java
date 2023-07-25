package com.example.urlsorting.user.entities;

import java.util.List;

import com.example.urlsorting.common.util.role.Role;
import com.example.urlsorting.sorting.entities.Sort;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(length = 30)
	private String name;

	@Column(unique = true, length = 100)
	private String email;

	@Column(length = 100)
	private String password;

	@Column()
	private String token;

	@Column()
	private Role role = Role.USER;


	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Sort> sorts;

	public void setToken(String token) {
		this.token = token;
	}
}
