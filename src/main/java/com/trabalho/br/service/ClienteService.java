package com.trabalho.br.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.trabalho.br.model.Cliente;
import com.trabalho.br.model.Role;
import com.trabalho.br.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	private RoleService roleService;

	public ModelAndView save(Cliente cliente, BindingResult result) {
		if (buscarEmail("gerente@exemplo.com") == null) {
			Role role = new Role();
			role.setPapel("ROLE_GERENTE");
			roleService.salvarRoleDinamicamente(role);
			setarGerente();
			role.setPapel("ROLE_USER");
			roleService.salvarRoleDinamicamente(role);
		}

		if (result.hasErrors()) {
			ModelAndView mv = new ModelAndView("/cliente/cadastrar?form-error");
			return mv;
		}

		if (this.buscarEmail(cliente.getEmail()) == null) {
			cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));
			Role role = new Role();
			role.setPapel("ROLE_USER");
			List<Role> roles = new ArrayList<>();
			roles.add(role);
			cliente.setRoles(roles);
			clienteRepository.save(cliente);
		}
		ModelAndView mv = new ModelAndView("redirect:/entrar");
		return mv;
	}

	public ModelAndView update(Cliente cliente, BindingResult result) {
		if (buscarEmail("gerente@exemplo.com") == null) {
			Role role = new Role();
			role.setPapel("ROLE_GERENTE");
			roleService.salvarRoleDinamicamente(role);
			setarGerente();
			role.setPapel("ROLE_USER");
			roleService.salvarRoleDinamicamente(role);
		}

		if (result.hasErrors()) {
			ModelAndView mv = new ModelAndView("redirect:/cliente/atualizar/?error");
			return mv;
		}

		if (this.buscarEmail(cliente.getEmail()) != null) {
			cliente.setSenha(new BCryptPasswordEncoder().encode(cliente.getSenha()));
			Role role = new Role();
			role.setPapel("ROLE_USER");
			List<Role> roles = new ArrayList<>();
			roles.add(role);
			cliente.setRoles(roles);
			clienteRepository.save(cliente);
		}
		ModelAndView mv = new ModelAndView("redirect:/cliente/atualizar/?sucess");
		return mv;
	}

	public List<Cliente> listClientes() {
		return clienteRepository.findAll();
	}

	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}

	public Cliente serchById(Long id) {
		return clienteRepository.getOne(id);
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.getOne(id);
	}

	public Cliente buscarEmail(String email) {
		return clienteRepository.findByEmail(email);
	}

	public Cliente clienteLogado() {
		Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails user = (UserDetails) auth;
		return clienteRepository.findByEmail(user.getUsername());
	}

	public void setarGerente() {
		Role role = new Role();
		role.setPapel("ROLE_GERENTE");// pass: 123
		Cliente gerente = new Cliente(null, Arrays.asList(role), "Gerente", "000.000.000-00",
				null, null, "gerente@exemplo.com",
				"$2a$10$lWEmW3k9ekVDMtyOUNiQ2e6SWrQWik/laqVSPUCyMNW4bjscDOYEa", null);
		clienteRepository.save(gerente);
	}

	public boolean logado() {
		Object auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails user = (UserDetails) auth;
		if (clienteRepository.findByEmail(user.getUsername()).isEnabled()) {
			return true;
		} else
			return false;
	}

}
