package com.elves.dscommerce.dto.exceptions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends CustomError{

    private List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }


    public List<FieldMessage> getErros() {
        return erros;
    }

    public  void addError(String fieldname, String Message){
        FieldMessage error = new FieldMessage(fieldname,Message);
        erros.add(error);
    }
}
