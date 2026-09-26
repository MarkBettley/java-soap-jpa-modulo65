package com.ebac.modulo65.controller;

import com.ebac.modulo65.dto.Usuario;
import com.ebac.modulo65.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre("Marco");
        usuario.setEdad(31);
    }

    @Test
    void obtenerUsuariosDebeRetornarLista() {
        List<Usuario> usuarios = List.of(usuario);

        when(usuarioService.obtenerUsuarios()).thenReturn(usuarios);

        ResponseWrapper<List<Usuario>> respuesta =
                usuarioController.obtenerUsuarios();

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.OK,
                respuesta.getResponseEntity().getStatusCode());
        assertEquals(1,
                respuesta.getResponseEntity().getBody().size());

        verify(usuarioService, times(1)).obtenerUsuarios();
    }

    @Test
    void obtenerUsuariosDebePermitirListaVacia() {
        when(usuarioService.obtenerUsuarios())
                .thenReturn(new ArrayList<>());

        ResponseWrapper<List<Usuario>> respuesta =
                usuarioController.obtenerUsuarios();

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.OK,
                respuesta.getResponseEntity().getStatusCode());
        assertTrue(respuesta.getResponseEntity().getBody().isEmpty());

        verify(usuarioService, times(1)).obtenerUsuarios();
    }

    @Test
    void obtenerUsuarioPorIdDebeRetornarUsuario() {
        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenReturn(Optional.of(usuario));

        ResponseWrapper<Usuario> respuesta =
                usuarioController.obtenerUsuarioPorId(1L);

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.OK,
                respuesta.getResponseEntity().getStatusCode());
        assertEquals("Marco",
                respuesta.getResponseEntity().getBody().getNombre());

        verify(usuarioService, times(1))
                .obtenerUsuarioPorId(1L);
    }

    @Test
    void obtenerUsuarioPorIdInexistenteDebeRetornar404() {
        when(usuarioService.obtenerUsuarioPorId(99L))
                .thenReturn(Optional.empty());

        ResponseWrapper<Usuario> respuesta =
                usuarioController.obtenerUsuarioPorId(99L);

        assertFalse(respuesta.isSuccess());
        assertEquals(HttpStatus.NOT_FOUND,
                respuesta.getResponseEntity().getStatusCode());

        verify(usuarioService, times(1))
                .obtenerUsuarioPorId(99L);
    }

    @Test
    void crearUsuarioDebeRetornar201() throws Exception {
        when(usuarioService.crearUsuario(usuario))
                .thenReturn(usuario);

        ResponseWrapper<Usuario> respuesta =
                usuarioController.crearUsuario(usuario);

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.CREATED,
                respuesta.getResponseEntity().getStatusCode());
        assertEquals(usuario,
                respuesta.getResponseEntity().getBody());

        verify(usuarioService, times(1))
                .crearUsuario(usuario);
    }

    @Test
    void crearUsuarioInvalidoDebeRetornar400() throws Exception {
        when(usuarioService.crearUsuario(usuario))
                .thenThrow(new Exception(
                        "No se permiten usuarios menores de 18 años"));

        ResponseWrapper<Usuario> respuesta =
                usuarioController.crearUsuario(usuario);

        assertFalse(respuesta.isSuccess());
        assertEquals(HttpStatus.BAD_REQUEST,
                respuesta.getResponseEntity().getStatusCode());

        verify(usuarioService, times(1))
                .crearUsuario(usuario);
    }

    @Test
    void actualizarUsuarioDebeRetornar200() throws Exception {
        Usuario actualizado = new Usuario();
        actualizado.setNombre("Marco Antonio");
        actualizado.setEdad(32);

        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenReturn(Optional.of(usuario));

        doNothing().when(usuarioService)
                .actualizarUsuario(actualizado);

        ResponseWrapper<Usuario> respuesta =
                usuarioController.actualizarUsuario(
                        1L, actualizado);

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.OK,
                respuesta.getResponseEntity().getStatusCode());
        assertEquals(1, actualizado.getIdUsuario());

        verify(usuarioService, times(1))
                .obtenerUsuarioPorId(1L);
        verify(usuarioService, times(1))
                .actualizarUsuario(actualizado);
    }

    @Test
    void actualizarUsuarioInexistenteDebeRetornar404()
            throws Exception {

        Usuario actualizado = new Usuario();

        when(usuarioService.obtenerUsuarioPorId(99L))
                .thenReturn(Optional.empty());

        ResponseWrapper<Usuario> respuesta =
                usuarioController.actualizarUsuario(
                        99L, actualizado);

        assertFalse(respuesta.isSuccess());
        assertEquals(HttpStatus.NOT_FOUND,
                respuesta.getResponseEntity().getStatusCode());

        verify(usuarioService, times(1))
                .obtenerUsuarioPorId(99L);
        verify(usuarioService, never())
                .actualizarUsuario(any());
    }

    @Test
    void eliminarUsuarioDebeRetornar204() throws Exception {
        doNothing().when(usuarioService)
                .eliminarUsuario(1L);

        ResponseWrapper<Void> respuesta =
                usuarioController.eliminarUsuario(1L);

        assertTrue(respuesta.isSuccess());
        assertEquals(HttpStatus.NO_CONTENT,
                respuesta.getResponseEntity().getStatusCode());

        verify(usuarioService, times(1))
                .eliminarUsuario(1L);
    }

    @Test
    void eliminarUsuarioInexistenteDebeRetornar404()
            throws Exception {

        doThrow(new Exception("El usuario indicado no existe"))
                .when(usuarioService)
                .eliminarUsuario(99L);

        ResponseWrapper<Void> respuesta =
                usuarioController.eliminarUsuario(99L);

        assertFalse(respuesta.isSuccess());
        assertEquals(HttpStatus.NOT_FOUND,
                respuesta.getResponseEntity().getStatusCode());

        verify(usuarioService, times(1))
                .eliminarUsuario(99L);
    }
}
