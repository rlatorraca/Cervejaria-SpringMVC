Cervejaria = Cervejaria || {};

Cervejaria.Autocomplete = (function() {
	
	function Autocomplete() {
		this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
		var htmlTemplateAutocomplete = $('#template-autocomplete-cerveja').html(); // Pega o HTML do SCRIPT do "handlebars"
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
	}
	
	Autocomplete.prototype.iniciar = function() {
		var options = {
			url: function(skuOuNome) {
				return '/cervejaria/cervejas?skuOuNome=' + skuOuNome;
			},
			getValue: 'nome', // Ira mostra na tela ao Usuario
			minCharNumber: 3, // A partir de 3 caracteres comeca a fazer a busca
			requestDelay: 300, // Após 300 milisegundos iria buscar a letra no servidor
			ajaxSettings: {
				contentType: 'application/json' //Diz que eh um arquivo JSON
			},
			template: {
				type: 'custom',  // Dizendo que estaq CUSTOMIZANDO o template
				method: function(nome, cerveja) {   
					cerveja.valorFormatado = Cervejaria.formatarMoeda(cerveja.valor);
					return this.template(cerveja);
				}.bind(this)
			}
		};
		console.log(">>> skuOuNomeInput ==> " + this.skuOuNomeInput);
		this.skuOuNomeInput.easyAutocomplete(options);
		console.log(">>> skuOuNomeInput ==> " + this.skuOuNomeInput);
	}
	console.log(">>> AutoComplete ==> " + Autocomplete);
	return Autocomplete
	
}());

$(function() {
	
	var autocomplete = new Cervejaria.Autocomplete();
	autocomplete.iniciar();
	
})