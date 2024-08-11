package com.example.caseim.model;
import com.example.caseim.validation.Capitalized;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BrandDto {
    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 2, max = 30)
    @Capitalized(message = "First letter of first name must be capitalized")
    private String name;
    private List<ModelDto> model;

}