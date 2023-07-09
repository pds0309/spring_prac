package com.pds.infoservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document // spring-data mongodb entity(document)
public class MovieInfo {
    @Id
    private String movieInfoId;
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;

    public void setName(String name) {
        this.name = name;
    }

}
