package pl.sages.javadevpro.projecttwo.api.user.dto;

import lombok.Value;

import java.util.List;

@Value
public class PageUserDto {

    List<UserDto> users;
    Integer currentPage;
    Integer totalPages;
    Long totalElements;
}