package sistemaos.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MenuAdvice {

    @ModelAttribute("uriAtual")
    public String uriAtual(HttpServletRequest request) {
        return request.getRequestURI();
    }
}