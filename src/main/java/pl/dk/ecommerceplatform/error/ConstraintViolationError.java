package pl.dk.ecommerceplatform.error;

import lombok.Getter;

record ConstraintViolationError (String field, String message){
}
