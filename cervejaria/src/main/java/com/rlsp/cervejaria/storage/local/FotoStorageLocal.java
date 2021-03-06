package com.rlsp.cervejaria.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.rlsp.cervejaria.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

/*
 * FotoStorageLocal e FotoStorageS3 ambos implementam FotoStorage (interface)
 *  - 
 */
@Profile("local")
@Component 
public class FotoStorageLocal implements FotoStorage {


	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	private static final String THUMBNAIL_PREFIX = "thumbnail.";
	
	private Path local;				//  Gravacao Final do Arqui com a foto da Cerveja
	//private Path localTemporario;	//	Para fazer uma gravacao TEMPORARiA
	
	public FotoStorageLocal() {
		/**
		 * System.getenv("HOME") = e o HOME do usuario
		 * .cervejariafotos = DIRETORIO criado para GRAVAR as fotos
		 */
		this(getDefault().getPath(System.getenv("HOME"), ".cervejariafotos"));
	}
	
	public FotoStorageLocal(Path path) {
		this.local = path;
		criarPastas(); // Cria a pasta temporaria
	}

	@Override
	public String salvar(MultipartFile[] files) {
	//public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename()); // Modifica o nome para acrescentar UUID (caraceteres)
			try {
				//arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
				arquivo.transferTo(new File(this.local.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome));
			} catch (IOException e) {
				//throw new RuntimeException("Erro salvando a foto na pasta temporária", e);
				throw new RuntimeException("Erro salvando a foto na pasta ", e);
			}
		}
		
		try {
			Thumbnails.of(this.local.resolve(novoNome).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL); // Cria os thumbnails das fotos das cervejas
		} catch (IOException e) {
			throw new RuntimeException("Erro gerando thumbnail", e);
		}
		
		return novoNome;
	}
	
//	@Override
//	public byte[] recuperarFotoTemporaria(String nome) {
//		try {
//			return Files.readAllBytes(this.localTemporario.resolve(nome));
//		} catch (IOException e) {
//			throw new RuntimeException("Erro lendo a foto temporária", e);
//		}
//	}
	
	private void criarPastas() {
		try {
			Files.createDirectories(this.local); // Cria diretorio 
			//this.localTemporario = getDefault().getPath(this.local.toString(), "temp"); // Cria uma pasta /temp, dentro do diretorio "cervejariaFoto"
			//Files.createDirectories(this.localTemporario);
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pastas criadas para salvar fotos.");
				LOGGER.debug("Pasta default: " + this.local.toAbsolutePath());
				//LOGGER.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro criando pasta para salvar foto", e);
		}
	}
	
	/*
	 * private String renomearArquivo(String nomeOriginal) { String novoNome =
	 * UUID.randomUUID().toString() + "_" + nomeOriginal;
	 * 
	 * if (LOGGER.isDebugEnabled()) {
	 * LOGGER.debug(String.format("Nome original: %s, novo nome: %s", nomeOriginal,
	 * novoNome)); }
	 * 
	 * return novoNome;
	 * 
	 * }
	 */
	
//	@Override
//	public void salvar(String foto) {
//		try {
//			Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
//		} catch (IOException e) {
//			throw new RuntimeException("Erro movendo a foto para destino final", e);
//		}
//		
//		try {
//			Thumbnails.of(this.local.resolve(foto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL); // Cria os thumbnails das fotos das cervejas
//		} catch (IOException e) {
//			throw new RuntimeException("Erro gerando thumbnail", e);
//		}
//		
//	}

	@Override
	public byte[] recuperar(String nome) {
		try {
			return Files.readAllBytes(this.local.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto", e);
		}
	}


	@Override
	public byte[] recuperarThumbnail(String foto) {	
		return recuperar(THUMBNAIL_PREFIX + foto); // Retorna o Prefixo dos Thumbnails para ser usado no EMAIL de confirmacaos
	}

	/**
	 * Exclui a foto da cerveja quando a CERVEJA É EXCLUIDA do DB
	 */
	@Override
	public void excluir(String foto) {
		try {
			Files.deleteIfExists(this.local.resolve(foto));
			Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + foto));
		} catch (IOException e) {
			LOGGER.warn(String.format("Erro ao tentar deletar foto %s. Messagem de erro: %s", foto, e.getMessage()));
		}
		
		
	}

	@Override
	public String getUrl(String foto) {
		System.out.println("getURL - LOCAL");
		
		return "http://localhost:8080/cervejaria/fotos/" + foto;
	}

}
