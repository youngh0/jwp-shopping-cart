package woowacourse.shoppingcart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDeletionRequest {

    private static final String INVALID_PASSWORD = "Invalid Password";

    @NotBlank(message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    public CustomerDeletionRequest() {
    }

    public CustomerDeletionRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
