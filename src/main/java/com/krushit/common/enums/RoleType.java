package com.krushit.common.enums;

import com.krushit.common.Message;

public enum RoleType {
    ROLE_SUPER_ADMIN("Admin", 1),
    ROLE_CUSTOMER("Customer", 2),
    ROLE_DRIVER("Driver", 3);

    private final String roleName;
    private final int roleId;

    RoleType(String name, int id) {
        roleName = name;
        roleId = id;
    }

    public static RoleType getType(int roleId) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getRoleId() == roleId) {
                return roleType;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_ROLE + roleId);
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleName='" + roleName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
