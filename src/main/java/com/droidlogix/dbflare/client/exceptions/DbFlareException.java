package com.droidlogix.dbflare.client.exceptions;

import java.util.ArrayList;
import java.util.List;

public class DbFlareException extends Exception {
    private List<String> errors = new ArrayList<>();

    public DbFlareException() {
        super();
    }

    public DbFlareException(String message, List<String> errors) {
        super(message);
        if(errors != null) {
            this.errors = errors;
        }
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
