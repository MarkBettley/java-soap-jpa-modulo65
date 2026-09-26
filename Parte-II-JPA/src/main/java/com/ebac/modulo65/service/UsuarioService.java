package com.ebac.modulo65.service;

import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario usuario) throws Exception {
        log.info("Intentando crear usuario: {}", usuario.getNombre());

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            log.warn("No se puede crear usuario sin nombre");
            throw new Exception("El nombre del usuario es obligatorio");
        }

        if (usuario.getEdad() < 18) {
            log.warn("Intento de crear usuario menor de edad: {}", usuario.getEdad());
            throw new Exception("No se permiten usuarios menores de 18 años");
        }

        Usuario usuarioCreado = usuarioRepository.save(usuario);
        log.info("Usuario creado correctamente con id {}", usuarioCreado.getIdUsuario());
        return usuarioCreado;
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long idUsuario) {
        log.info("Buscando usuario con id {}", idUsuario);
        return usuarioRepository.findById(idUsuario);
    }

    public List<Usuario> obtenerUsuarios() {
        log.info("Obteniendo listado de usuarios");
        return usuarioRepository.findAll();
    }

    public void actualizarUsuario(Usuario usuario) throws Exception {
        log.info("Intentando actualizar usuario con id {}", usuario.getIdUsuario());

        if (!usuarioRepository.existsById((long) usuario.getIdUsuario())) {
            log.warn("No existe el usuario con id {}", usuario.getIdUsuario());
            throw new Exception("El usuario indicado no existe");
        }

        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new Exception("El nombre del usuario es obligatorio");
        }

        if (usuario.getEdad() < 18) {
            throw new Exception("No se permiten usuarios menores de 18 años");
        }

        usuarioRepository.save(usuario);
        log.info("Usuario {} actualizado correctamente", usuario.getIdUsuario());
    }

    public void eliminarUsuario(Long id) throws Exception {
        log.info("Intentando eliminar usuario con id {}", id);

        if (!usuarioRepository.existsById(id)) {
            log.warn("No se puede eliminar. Usuario {} inexistente", id);
            throw new Exception("El usuario indicado no existe");
        }

        usuarioRepository.deleteById(id);
        log.info("Usuario {} eliminado correctamente", id);
    }
}
