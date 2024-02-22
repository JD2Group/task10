package org.example.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorUpdateDTORequest {
    private String newName;
    private String newSurname;
    private String newEmail;
    private String oldEmail;
}
