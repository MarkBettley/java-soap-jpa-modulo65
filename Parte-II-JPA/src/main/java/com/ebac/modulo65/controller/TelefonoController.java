package com.ebac.modulo65.controller;

import com.ebac.modulo65.dto.Telefono;
import com.ebac.modulo65.service.TelefonoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class TelefonoController {

    @Autowired
    TelefonoService telefonoService;

    @GetMapping("/telefonos")
    public ResponseWrapper<List<Telefono>> obtenerTelefonos() {
        log.info("Solicitud para obtener telefonos");

        return new ResponseWrapper<>(
                true,
                "Listado de telefonos",
                ResponseEntity.ok(telefonoService.obtenerTelefonos()));
    }

    @GetMapping("/telefonos/{id}")
    public ResponseWrapper<Telefono> obtenerTelefonoPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener telefono {}", id);

        Optional<Telefono> telefono =
                telefonoService.obtenerTelefonoPorId(id);

        if (telefono.isPresent()) {
            return new ResponseWrapper<>(
                    true,
                    "Informacion del telefono " + id,
                    ResponseEntity.ok(telefono.get()));
        }

        return new ResponseWrapper<>(
                false,
                "El telefono indicado no existe",
                ResponseEntity.notFound().build());
    }

    @PostMapping("/telefonos")
    public ResponseWrapper<Telefono> crearTelefono(@RequestBody Telefono telefono) {
        log.info("Solicitud para crear telefono");

        try {
            Telefono creado = telefonoService.crearTelefono(telefono);

            return new ResponseWrapper<>(
                    true,
                    "Telefono creado exitosamente",
                    ResponseEntity.created(
                            URI.create("/telefonos/" + creado.getIdTelefono()))
                            .body(creado));

        } catch (Exception e) {
            log.warn("No se pudo crear telefono: {}", e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.badRequest().build());
        }
    }

    @PutMapping("/telefonos/{id}")
    public ResponseWrapper<Telefono> actualizarTelefono(
            @PathVariable Long id,
            @RequestBody Telefono telefonoActualizado) {

        log.info("Solicitud para actualizar telefono {}", id);

        Optional<Telefono> existente =
                telefonoService.obtenerTelefonoPorId(id);

        if (existente.isEmpty()) {
            return new ResponseWrapper<>(
                    false,
                    "El telefono indicado no existe",
                    ResponseEntity.notFound().build());
        }

        telefonoActualizado.setIdTelefono(
                existente.get().getIdTelefono());

        try {
            telefonoService.actualizarTelefono(telefonoActualizado);

            return new ResponseWrapper<>(
                    true,
                    "Telefono actualizado correctamente",
                    ResponseEntity.ok(telefonoActualizado));

        } catch (Exception e) {
            log.warn("No se pudo actualizar telefono {}: {}",
                    id, e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.badRequest().build());
        }
    }

    @DeleteMapping("/telefonos/{id}")
    public ResponseWrapper<Void> eliminarTelefono(@PathVariable Long id) {
        log.info("Solicitud para eliminar telefono {}", id);

        try {
            telefonoService.eliminarTelefono(id);

            return new ResponseWrapper<>(
                    true,
                    "Telefono eliminado correctamente",
                    ResponseEntity.noContent().build());

        } catch (Exception e) {
            log.warn("No se pudo eliminar telefono {}: {}",
                    id, e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.notFound().build());
        }
    }
}
