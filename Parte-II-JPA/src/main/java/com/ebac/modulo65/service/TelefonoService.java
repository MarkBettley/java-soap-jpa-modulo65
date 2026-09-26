package com.ebac.modulo65.service;

import com.ebac.modulo65.dto.Telefono;
import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.repository.TelefonoRepository;
import com.ebac.modulo65.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelefonoService {

    @Autowired
    TelefonoRepository telefonoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public Telefono crearTelefono(Telefono telefono) throws Exception {
        log.info("Intentando crear telefono");

        validarTelefono(telefono);

        Usuario usuario = usuarioRepository
                .findById((long) telefono.getUsuario().getIdUsuario())
                .orElseThrow(() ->
                        new Exception("El usuario asociado al telefono no existe"));

        telefono.setUsuario(usuario);

        Telefono telefonoCreado = telefonoRepository.save(telefono);
        log.info("Telefono creado correctamente con id {}",
                telefonoCreado.getIdTelefono());

        return telefonoCreado;
    }

    public Optional<Telefono> obtenerTelefonoPorId(Long idTelefono) {
        log.info("Buscando telefono con id {}", idTelefono);
        return telefonoRepository.findById(idTelefono);
    }

    public List<Telefono> obtenerTelefonos() {
        log.info("Obteniendo listado de telefonos");
        return telefonoRepository.findAll();
    }

    public void actualizarTelefono(Telefono telefono) throws Exception {
        log.info("Intentando actualizar telefono con id {}",
                telefono.getIdTelefono());

        if (!telefonoRepository.existsById((long) telefono.getIdTelefono())) {
            throw new Exception("El telefono indicado no existe");
        }

        validarTelefono(telefono);

        Usuario usuario = usuarioRepository
                .findById((long) telefono.getUsuario().getIdUsuario())
                .orElseThrow(() ->
                        new Exception("El usuario asociado al telefono no existe"));

        telefono.setUsuario(usuario);
        telefonoRepository.save(telefono);

        log.info("Telefono {} actualizado correctamente",
                telefono.getIdTelefono());
    }

    public void eliminarTelefono(Long id) throws Exception {
        log.info("Intentando eliminar telefono con id {}", id);

        if (!telefonoRepository.existsById(id)) {
            throw new Exception("El telefono indicado no existe");
        }

        telefonoRepository.deleteById(id);
        log.info("Telefono {} eliminado correctamente", id);
    }

    private void validarTelefono(Telefono telefono) throws Exception {
        if (telefono.getNumero() == null || telefono.getNumero().isBlank()) {
            throw new Exception("El numero de telefono es obligatorio");
        }

        if (telefono.getNumero().length() > 15) {
            throw new Exception("Telefono invalido");
        }

        if (telefono.getUsuario() == null) {
            throw new Exception("El telefono debe estar asociado a un usuario");
        }
    }
}
