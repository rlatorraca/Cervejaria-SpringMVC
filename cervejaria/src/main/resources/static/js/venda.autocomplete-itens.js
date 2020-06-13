Cervejaria = Cervejaria || {};

Cervejaria.Autocomplete = (function() {
	
	function Autocomplete() {
		this.skuOuNomeInput = $('.js-sku-nome-cerveja-input');
		var htmlTemplateAutocomplete = $('#template-autocomplete-cerveja').html(); // Pega o HTML do SCRIPT do "handlebars"
		this.template = Handlebars.compile(htmlTemplateAutocomplete);
		this.emitter = $({});  // EMISSOR de um EVENTO criado 
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	Autocomplete.prototype.iniciar = function() {
		var options = {
			url: function(skuOuNome) {				
				return this.skuOuNomeInput.data('url') + '?skuOuNome=' + skuOuNome;
			}.bind(this),
			getValue: 'nome', // Ira mostra na tela ao Usuario
			minCharNumber: 3, // A partir de 3 caracteres comeca a fazer a busca
			requestDelay: 300, // Após 300 milisegundos iria buscar a letra no servidor
			ajaxSettings: {
				contentType: 'application/json' //Diz que eh um arquivo JSON
			},
			template: {
				type: 'custom',  // Dizendo que estaq CUSTOMIZANDO o template
				method: template.bind(this)				
			},
			list: {
				onChooseEvent: onItemSelecionado.bind(this)
			}
		};
		
		this.skuOuNomeInput.easyAutocomplete(options);
		
	}
	
	function onItemSelecionado() {
		this.emitter.trigger('item-selecionado', this.skuOuNomeInput.getSelectedItemData());
		this.skuOuNomeInput.val(''); // Deixa a entrada para SKU ou NOME CERVEJA "LIMPO" para nova entrada
		this.skuOuNomeInput.focus(); // Da o "Foco" para nova entrada de SKU ou NOME CERVEJA 
	}
	
	function template(nome, cerveja) {
		cerveja.valorFormatado = Cervejaria.formatarMoeda(cerveja.valor);
		return this.template(cerveja);
	}
	
	return Autocomplete
	
}());
