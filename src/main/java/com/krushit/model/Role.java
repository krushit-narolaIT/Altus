package com.krushit.model;

public enum Role {
    ROLE_CUSTOMER("Customer",2),
    ROLE_SUPER_ADMIN("Admin", 1),
    ROLE_DRIVER("Driver",3);

    private final String roleName;
    private final int roleId;

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

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
