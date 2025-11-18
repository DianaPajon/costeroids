package com.itrsa;

import com.itrsa.costeroids.logic.dto.input.KeyDTO;
import com.itrsa.costeroids.logic.dto.output.BulletDTO;
import com.itrsa.costeroids.logic.dto.output.CoordinateDTO;
import com.itrsa.costeroids.logic.dto.output.ShipDTO;
import com.itrsa.costeroids.logic.dto.output.StateDTO;
import com.itrsa.costeroids.logic.engine.GameEngine;
import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;

@SerdeImport(StateDTO.class)
@SerdeImport(ShipDTO.class)
@SerdeImport(CoordinateDTO.class)
@SerdeImport(KeyDTO.class)
@SerdeImport(BulletDTO.class)
public class Application {
    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}