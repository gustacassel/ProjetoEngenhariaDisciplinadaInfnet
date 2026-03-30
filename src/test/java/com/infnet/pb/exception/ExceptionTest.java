package com.infnet.pb.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionTest {
    @Test
    void testExceptions() {
        RecursoNaoEncontradoException ex1 = new RecursoNaoEncontradoException("Erro 1");
        assertEquals("Erro 1", ex1.getMessage());

        RegraNegocioException ex2 = new RegraNegocioException("Erro 2");
        assertEquals("Erro 2", ex2.getMessage());
    }
}
