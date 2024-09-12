package com.sparta3tm.common.support;

public class RoleCheck {

    public boolean CHECK_MASTER(String userRole) {
        return userRole.equals("MASTER");
    }

    public boolean CHECK_MANAGER(String userRole) {
        return userRole.equals("HUB_MANAGER");
    }

    public boolean CHECK_SHIPPER(String userRole) {
        return userRole.equals("SHIPPER");
    }

    public boolean CHECK_COMPANY(String userRole) {
        return userRole.equals("COMPANY");
    }
}
