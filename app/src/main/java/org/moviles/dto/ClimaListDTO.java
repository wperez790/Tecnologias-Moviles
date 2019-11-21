package org.moviles.dto;

import org.moviles.model.Clima;

import java.util.ArrayList;
import java.util.List;

public class ClimaListDTO {

    public List<ClimaDTO> list;

    public List<Clima> getListClima(){
        List<Clima> lclima = new ArrayList<>();
        for(ClimaDTO climadto:list){
            lclima.add(climadto.getClima());
        }
        return lclima;
    }


}
