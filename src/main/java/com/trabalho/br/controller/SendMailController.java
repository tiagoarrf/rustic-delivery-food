package com.trabalho.br.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.trabalho.br.model.Cliente;
import com.trabalho.br.service.ClienteService;

@Controller
public class SendMailController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("/pedido/confirmado")
    public ModelAndView sendMail() {

        Cliente cliente = clienteService.clienteLogado();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Mil Sabores Delivery");
        message.setText("Olá " + cliente.getNome() + ", seu pedido foi recebido com sucesso!");
        message.setTo(cliente.getEmail());
        message.setFrom("deliverymilsabores@gmail.com");

        ModelAndView mv = new ModelAndView("pedido/confirmacao");

        try {
            mailSender.send(message);
            mv.addObject("emailUser", cliente.getEmail());
            mv.addObject("nomeUser", cliente.getNome());
            mv.addObject("cliente", cliente);
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("mensagem", "Erro ao enviar email para: " + cliente.getEmail());
            return mv;
        }
    }

}