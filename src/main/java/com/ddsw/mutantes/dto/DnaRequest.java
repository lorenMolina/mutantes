package com.ddsw.mutantes.dto;

import com.ddsw.mutantes.validators.ValidDna;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DnaRequest {
    @ValidDna
    private String[] dna;
}