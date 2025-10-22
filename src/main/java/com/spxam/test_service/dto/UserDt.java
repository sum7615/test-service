package com.spxam.test_service.dto;
import java.util.Set;

public record UserDt (String userName, String fullName,
                      Set<String> roles,
                      Set<String> actions,
                      Set<String>emails,
                      Set<String>phoneNumbers
) {

}