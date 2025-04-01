package com.krushit.common.enums;

import com.krushit.common.Message;

public enum Role {
    ROLE_CUSTOMER("Customer",2),
    ROLE_SUPER_ADMIN("Admin", 1),
    ROLE_DRIVER("Driver",3);

    private final Role roleName;
    private final int roleId;

    Role(Role name, int id) {
        roleName = name;
        roleId = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public Role getRoleName() {
        return roleName;
    }

    public static Role getType(int roleId) {
        for (Role role : Role.values()) {
            if (role.getRoleId() == roleId) {
                return role;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_ROLE + roleId);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
