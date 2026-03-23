package com.infnet.pb.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratamento para regras de negócio (ex: e-mail duplicado fora do form validation)
    @ExceptionHandler(RegraNegocioException.class)
    public ModelAndView handleRegraNegocio(RegraNegocioException ex, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/general-error"); // Nome do template Thymeleaf
        return mav;
    }

    // Tratamento para ID não encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ModelAndView handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("error/404");
        return mav;
    }

    // Tratamento GENÉRICO (Fail Safe) - Para qualquer coisa inesperada (NullPointer, Banco caiu, etc)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex, HttpServletRequest req) {
        // Logar o erro aqui seria fundamental em produção (SLF4J)
        System.err.println("ERRO CRÍTICO: " + ex.getMessage());
        ex.printStackTrace();

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Ocorreu um erro inesperado no sistema. Nossa equipe foi notificada.");
        mav.addObject("technicalDetail", ex.getClass().getSimpleName()); // Só para debug, remover em prod real
        mav.setViewName("error/500");
        return mav;
    }
}