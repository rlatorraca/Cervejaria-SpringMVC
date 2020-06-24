package com.rlsp.cervejaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.rlsp.cervejaria.dto.FotoDTO;
import com.rlsp.cervejaria.storage.FotoStorage;
import com.rlsp.cervejaria.storage.FotoStorageRunnable;

/**
 * RestController x Controller
 *  - RestController = continua sendo um COntroller mas na resposta ja sera um @ResponseBody (logo nao precisa ser anotado no retorno como @ResponseBody)
 *  	** isso pois estamos trabalhando como AJAX
 * 
 * MultipartFile[] files = 
 * 
 * @RequestParam("files[]") = é a VARIAVEL que ira passar o ARRAY com os bites da FOTO
 * 
 * DeferredResult<FotoDTO> ==> trabalha com um retorno Assicrono (liberando a thread e a pegando posterioremente quando for preciso retornar informacao a quem requisitou)
 * 	- Retorno deferido
 *  - FotoDTO = criado para fazer a transferencia de informacoes da FOTO
 */

@RestController
@RequestMapping("/fotos")
public class FotosController {

	@Autowired
	private FotoStorage fotoStorage;
	
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();

		/**
		 * Thread nova para retornar a Informacao
		 *  - new FotoStorageRunnable : sera a thread que fara apos trabalhado o arquivo
		 * 
		 */
		Thread thread = new Thread(new FotoStorageRunnable(files, resultado, fotoStorage));
		thread.start();

		System.out.println(">>> upload");
		return resultado;
	}
	
//	@GetMapping("/temp/{nome:.*}")
//	public byte[] recuperarFotoTemporaria(@PathVariable("nome") String nomeFoto) {
//		return fotoStorage.recuperarFotoTemporaria(nomeFoto);
//	}
	
	@GetMapping("/{nome:.*}")
	public byte[] recuperar(@PathVariable String nome) {
		return fotoStorage.recuperar(nome);
	}
	
}
