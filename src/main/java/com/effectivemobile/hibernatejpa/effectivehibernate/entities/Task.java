package com.effectivemobile.hibernatejpa.effectivehibernate.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private Long id;

    private String title;

    private String description;

    private Boolean status;

    private LocalDateTime dateCalendar;

}
