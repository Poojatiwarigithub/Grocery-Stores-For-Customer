package com.customers.grocerystoresforcustomer.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinCodelValidator {


    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "^[1-9][0-9]{5}$";

    public PinCodelValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
