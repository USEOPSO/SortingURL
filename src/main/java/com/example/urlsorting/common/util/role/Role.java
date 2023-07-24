package com.example.urlsorting.common.util.role;

public enum Role {
	USER(0),
	ADMIN(1);

	private final int role;

	Role(int i) {
		this.role = i;
	}

	public int getRole() {
		return role;
	}
}
