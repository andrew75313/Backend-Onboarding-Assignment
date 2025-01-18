package com.sparta.backendonboardingassignment.domain.users.entity;

public enum UsersRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    MANAGER(Authority.MANAGER);  // 관리자 권한

    private final String authority;

    UsersRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String MANAGER = "ROLE_MANAGER";
    }
}
