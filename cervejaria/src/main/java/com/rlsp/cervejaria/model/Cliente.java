package com.rlsp.cervejaria.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;

import com.rlsp.cervejaria.model.validation.ClienteGrupoProvider;
import com.rlsp.cervejaria.model.validation.group.CnpjGroup;
import com.rlsp.cervejaria.model.validation.group.CpfGroup;

@Entity
@Table(name = "cliente")
@GroupSequenceProvider(ClienteGrupoProvider.class) // Serve para validar usando CPF / CNPJ, usando a Classe "ClienteGrupoProvider", com base na SEQUENCIA definida nessa classe ja citada
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotNull(message = "Pessoa Físcia ou Jurídica é obrigatório")
	@Enumerated(EnumType.STRING) // Salava  o NOME do ENUM  (e nao ORDINAL = valores)
	@Column(name = "tipo_pessoa")
	private TipoPessoa tipoPessoa;

	@CPF(groups = CpfGroup.class)
	@CNPJ(groups = CnpjGroup.class)
	@NotBlank(message = "CPF/CNPJ é obrigatório")
	@Column(name = "cpf_cnpj")
	private String cpfOuCnpj;

	private String telefone;

	@Email(message = "Email inválido")
	private String email;

	@Embedded
	private Endereco endereco;

	//----------------------------IMPLEMENTACOES NECESSARIAS -----------------------------------
	
	/**
	 * Metodos de CAllBack do JPA
	 */
	@PrePersist @PreUpdate
	private void prePersistPreUpdate() {
		this.cpfOuCnpj = this.cpfOuCnpj.replaceAll("\\.|-|/", "");
	}
	
	public String getCpfOuCnpjSemFormatacao() {
		return TipoPessoa.removerFormatacao(this.cpfOuCnpj);
	}

	//----------------------------GETTERS and SETTERS-----------------------------------

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
