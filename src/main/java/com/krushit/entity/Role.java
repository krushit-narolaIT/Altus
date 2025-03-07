package com.krushit.entity;

public enum Role {
    ROLE_CUSTOMER("Customer",3),
    ROLE_SUPER_ADMIN("Admin", 1),
    ROLE_DRIVER("Driver",2);

    private String roleName;
    private int roleId;

    Role(String name, int id) {
        roleName = name;
        roleId = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role getRole(int roleId) {
        for (Role role : Role.values()) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role ID: " + roleId);
    }
}
