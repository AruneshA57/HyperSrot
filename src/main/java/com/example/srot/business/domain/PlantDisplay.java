package com.example.srot.business.domain;

import com.example.srot.data.model.Plant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantDisplay {
    private String tag;
    private String capacity;

    public PlantDisplay(Plant plant) {
        this.tag = plant.getTags();
        this.capacity = String.valueOf(plant.getPlantCapacity());
    }
}
