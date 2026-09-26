package com.ebac.soap;

import com.ebac.soap.saludo.SaludoRequest;
import com.ebac.soap.saludo.SaludoResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class SaludoEndpoint {

    private static final String NAMESPACE_URI =
            "http://soap.ebac.com/saludo";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "SaludoRequest")
    @ResponsePayload
    public SaludoResponse saludar(@RequestPayload SaludoRequest request) {

        SaludoResponse response = new SaludoResponse();
        response.setMensaje("Hola, " + request.getNombre() + "!");

        return response;
    }
}
