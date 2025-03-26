package com.krushit.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = UpdatePasswordDTO.UpdatePasswordDTOBuilder.class)
public class UpdatePasswordDTO {
    private final String oldPassword;
    private final String newPassword;

    private UpdatePasswordDTO(UpdatePasswordDTOBuilder builder) {
        this.oldPassword = builder.oldPassword;
        this.newPassword = builder.newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public static class UpdatePasswordDTOBuilder {
        private String oldPassword;
        private String newPassword;

        public UpdatePasswordDTOBuilder setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
            return this;
        }

        public UpdatePasswordDTOBuilder setNewPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public UpdatePasswordDTO build() {
            return new UpdatePasswordDTO(this);
        }
    }
}
