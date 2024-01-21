package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;

@Builder
public record RegisterUserDto (String firstName, String lastName, String email, String password){
}
