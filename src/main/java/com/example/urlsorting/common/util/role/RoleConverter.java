package com.example.urlsorting.common.util.role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Role role) {
		if (role == null) {
			return null;
		}
		return role.getRole();
	}

	@Override
	public Role convertToEntityAttribute(Integer role) {
		if (role == null) {
			return null;
		}

		for (Role r : Role.values()) {
			if (r.getRole() == role) {
				return r;
			}
		}

		throw new IllegalArgumentException("Unknown role: " + role);
	}
}

