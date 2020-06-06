package com.rlsp.cervejaria.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rlsp.cervejaria.controller.page.PageWrapper;
import com.rlsp.cervejaria.model.Cerveja;
import com.rlsp.cervejaria.model.Usuario;
import com.rlsp.cervejaria.repository.GruposRepository;
import com.rlsp.cervejaria.repository.UsuariosRepository;
import com.rlsp.cervejaria.repository.filter.UsuarioFilter;
import com.rlsp.cervejaria.service.CadastroUsuarioService;
import com.rlsp.cervejaria.service.StatusUsuario;
import com.rlsp.cervejaria.service.exception.EmailUsuarioJaCadastradoException;
import com.rlsp.cervejaria.service.exception.SenhaObrigatoriaUsuarioException;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private GruposRepository grupos;
	
	@Autowired
	private UsuariosRepository usuarios;

	@RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/CadastroUsuario");
		mv.addObject("grupos",grupos.findAll());
		return mv;
	}
	
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}
		
		try {
			cadastroUsuarioService.salvar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			result.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		} catch (SenhaObrigatoriaUsuarioException e) {
			result.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}
		
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
		return new ModelAndView("redirect:/usuarios/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter, @PageableDefault(size = 3) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("/usuario/PesquisaUsuarios");		
		mv.addObject("grupos", grupos.findAll());
		
		//mv.addObject("usuarios", usuarios.filtrar(usuarioFilter));
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
		
		
	}
	
	
	/**
	 * @PutMapping("/status") ==> serve para ATUALIZAR parte da pagina de PesquisaUSuarios
	 *  - Especificamente a parte dos CHECKBOXs de ATIVO/INATIVO
	 *  
	 *  @ResponseStatus(HttpStatus.OK) ==> como NAO RETORNA uma view (pagina) por estar trabalhando com JScript/AJAX
	 */
	@PutMapping("/status")
	//@RequestMapping(name = "/status", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam("codigos[]") Long[] codigos, @RequestParam("status") StatusUsuario statusUsuario) {
			
		
		cadastroUsuarioService.alterarStatus(codigos, statusUsuario);
	}
	
}
