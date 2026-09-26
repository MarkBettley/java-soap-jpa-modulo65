package com.ebac.modulo65.controller;

import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public ResponseWrapper<List<Usuario>> obtenerUsuarios() {
        log.info("Solicitud para obtener usuarios");

        List<Usuario> usuarios = usuarioService.obtenerUsuarios();

        return new ResponseWrapper<>(
                true,
                "Listado de usuarios",
                ResponseEntity.ok(usuarios));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseWrapper<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        log.info("Solicitud para obtener usuario {}", id);

        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);

        if (usuario.isPresent()) {
            return new ResponseWrapper<>(
                    true,
                    "Informacion del usuario " + id,
                    ResponseEntity.ok(usuario.get()));
        }

        log.warn("Usuario {} no encontrado", id);

        return new ResponseWrapper<>(
                false,
                "El usuario indicado no existe",
                ResponseEntity.notFound().build());
    }

    @PostMapping("/usuarios")
    public ResponseWrapper<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        log.info("Solicitud para crear usuario");

        try {
            Usuario creado = usuarioService.crearUsuario(usuario);

            return new ResponseWrapper<>(
                    true,
                    "Usuario creado exitosamente",
                    ResponseEntity.created(
                            URI.create("/usuarios/" + creado.getIdUsuario()))
                            .body(creado));

        } catch (Exception e) {
            log.warn("No se pudo crear usuario: {}", e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.badRequest().build());
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseWrapper<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuarioActualizado) {

        log.info("Solicitud para actualizar usuario {}", id);

        Optional<Usuario> existente =
                usuarioService.obtenerUsuarioPorId(id);

        if (existente.isEmpty()) {
            return new ResponseWrapper<>(
                    false,
                    "El usuario indicado no existe",
                    ResponseEntity.notFound().build());
        }

        usuarioActualizado.setIdUsuario(existente.get().getIdUsuario());

        try {
            usuarioService.actualizarUsuario(usuarioActualizado);

            return new ResponseWrapper<>(
                    true,
                    "Usuario actualizado correctamente",
                    ResponseEntity.ok(usuarioActualizado));

        } catch (Exception e) {
            log.warn("No se pudo actualizar usuario {}: {}",
                    id, e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.badRequest().build());
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseWrapper<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("Solicitud para eliminar usuario {}", id);

        try {
            usuarioService.eliminarUsuario(id);

            return new ResponseWrapper<>(
                    true,
                    "Usuario eliminado correctamente",
                    ResponseEntity.noContent().build());

        } catch (Exception e) {
            log.warn("No se pudo eliminar usuario {}: {}",
                    id, e.getMessage());

            return new ResponseWrapper<>(
                    false,
                    e.getMessage(),
                    ResponseEntity.notFound().build());
        }
    }
}
