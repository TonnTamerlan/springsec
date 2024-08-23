package ua.oleksii.bank.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "lkajflksdjfakjldj9054049kjfl32u4";
    public static final String JWT_HEADER = "Authorization";

}
