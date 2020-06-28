package com.rlsp.cervejaria.storage.s3;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.rlsp.cervejaria.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;


@Profile("prod")
@Component
public class FotoStorageS3 implements FotoStorage {

	private static final Logger logger = LoggerFactory.getLogger(FotoStorageS3.class);

	private static final String BUCKET = "cervejariarlsp";
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public String salvar(MultipartFile[] files) {
		
		String novoNome = null;
		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			
			try {
				AccessControlList acl = new AccessControlList();
				acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); // Configura as permissoes para que TODOS possam ler os arquivos (foto)
				System.out.println("... Dentro de SALVAR FotoStorageS3");
				enviarFoto(novoNome, arquivo, acl);
				enviarThumbnail(novoNome, arquivo, acl);
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando arquivo no S3", e);
			}
		}
		
		return novoNome;
	}

	@Override
	public byte[] recuperar(String foto) {
		InputStream is = amazonS3.getObject(BUCKET, foto).getObjectContent(); // Recupera o OBJETO da AWS (no caso a FOTO)
		try {
			return IOUtils.toByteArray(is); //IOUtils é da AWS
		} catch (IOException e) {
			logger.error("Não conseguiu recuerar foto do S3", e);
		}
		return null;
	}

	@Override
	public byte[] recuperarThumbnail(String foto) {
		return recuperar(FotoStorage.THUMBNAIL_PREFIX + foto); // Recupera o OBJETO da AWS (no caso a Thumbnail)
	}

	@Override
	public void excluir(String foto) {
		amazonS3.deleteObjects(new DeleteObjectsRequest(BUCKET).withKeys(foto, THUMBNAIL_PREFIX + foto)); // DELETA a foto + thumbnails da AWS
	}

	@Override
	public String getUrl(String foto) {
		if (!StringUtils.isEmpty(foto)) {
			System.out.println("getURL - AWS");
			return "https://cervejariarlsp.s3.amazonaws.com/" + foto;
		}
		
		return null;
	}
	
	/**
	 * Envia a foto para AWS
	 */
	private ObjectMetadata enviarFoto(String novoNome, MultipartFile arquivo, AccessControlList acl)
			throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(arquivo.getContentType()); // tipo de conteudo do arquivo (ex: png, jpg, etc) 
		metadata.setContentLength(arquivo.getSize()); // tamanho do arquivo
		
		amazonS3.putObject(new PutObjectRequest(BUCKET, novoNome, arquivo.getInputStream(), metadata)
					.withAccessControlList(acl)); // Envia um OBJETO para AWS (no caso a foto)
		return metadata;
	}

	/**
	 * Envia o THUMBNAIL para a AWS	 * 
	 */
	private void enviarThumbnail(String novoNome, MultipartFile arquivo, AccessControlList acl)	throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Thumbnails.of(arquivo.getInputStream()).size(40, 68).toOutputStream(os); // Redimension o arquivo para o tamanho do Thumbnail
		byte[] array = os.toByteArray();
		InputStream is = new ByteArrayInputStream(array);
		
		ObjectMetadata thumbMetadata = new ObjectMetadata();
		thumbMetadata.setContentType(arquivo.getContentType());
		thumbMetadata.setContentLength(array.length); // tamanho do Array com o thumbnail
		
		amazonS3.putObject(new PutObjectRequest(BUCKET, THUMBNAIL_PREFIX + novoNome, is, thumbMetadata)
					.withAccessControlList(acl));// Envia um OBJETO para AWS (no caso a foto)
	}

}
