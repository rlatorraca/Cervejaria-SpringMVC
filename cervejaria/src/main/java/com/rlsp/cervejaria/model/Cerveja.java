package com.rlsp.cervejaria.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import com.rlsp.cervejaria.validation.SKU;


@Entity
@Table(name = "cerveja")
public class Cerveja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@SKU
	@NotBlank(message = "SKU é obrigatório")// Verifica se a String não é NULA ou VAZIA
	private String sku;
	
	@NotBlank(message = "Name é obrigatório")// Verifica se a String não é NULA ou VAZIA
	private String nome;
	
	@NotBlank(message = "Descricao é obrigatória")// Verifica se a String não é NULA ou VAZIA
	@Size(min=1, max=50, message="Descrção deve ter no máximo 50 caracteres")
	private String descricao;
	
	@NotNull(message = "Valor é obrigatório")
	@DecimalMin(value = "0.50", message = "O valor da cerveja deve ser maior que R$0,50")
	@DecimalMax(value = "9999999.99", message = "O valor da cerveja deve ser menor que R$9.999.999,99")
	private BigDecimal valor;

	@NotNull(message = "O teor alcóolico é obrigatório")
	@DecimalMax(value = "100.0", message = "O valor do teor alcóolico deve ser menor que 100")
	@Column(name = "teor_alcoolico")
	private BigDecimal teorAlcoolico;

	@NotNull(message = "A comissão é obrigatória")
	@DecimalMax(value = "25.0", message = "A comissão deve ser igual ou menor que 25")
	private BigDecimal comissao;

	@NotNull(message = "A quantidade em estoque é obrigatória")
	@Max(value = 99999, message = "A quantidade em estoque deve ser menor que 99.999")
	@Column(name = "quantidade_estoque")
	private Integer quantidadeEstoque;

	@NotNull(message = "A origem é obrigatória")
	@Enumerated(EnumType.STRING)
	private Origem origem;

	@NotNull(message = "O sabor é obrigatório")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;

	@NotNull(message = "O estilo é obrigatório")
	@ManyToOne
	@JoinColumn(name = "codigo_estilo")
	private Estilo estilo;
	
	private String foto;
	
	@Column(name="content_type")
	private String contentType;
	
	/**
	 * Retorna a foto de MockUp em caso de nao inclusao de foto no Cadastro da Cerveja	 * 
	 */
	public String getFotoOrMockUp() {
		return !StringUtils.isEmpty(foto) ? foto : "cerveja-mock.png";
	}

	
	/**
	 * METODOS DE CALLBACK (do JPA) 
	 */
	@PrePersist //Antes de PERSISTIR (sera executado)
	@PreUpdate //Antes de fazer update (sera Executado
	private void prePersitUpdate() {
		this.sku = sku.toUpperCase();
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}	
	

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
		Cerveja other = (Cerveja) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	
	
}
