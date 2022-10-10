package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class User {
    String firstName;
    String lastName;
    String email;
    String userId;

}

