package com.rlsp.cervejaria.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Beer {


	@NotBlank(message = "SKU is mandatory")// Verifica se a String não é NULA ou VAZIA
	private String sku;
	
	@NotBlank(message = "Name is mandatory")// Verifica se a String não é NULA ou VAZIA
	private String name;
	
	@NotBlank(message = "Description is mandatory")// Verifica se a String não é NULA ou VAZIA
	@Size(min=1, max=50, message="Description needs maximum 50 characters")
	private String description;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
