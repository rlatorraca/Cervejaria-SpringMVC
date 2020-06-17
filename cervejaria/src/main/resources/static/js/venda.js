Cervejaria.Venda = (function() {
	
	function Venda(tabelaItens) {
		this.tabelaItens = tabelaItens;
		this.valorTotalBox = $('.js-valor-total-box'); // VALOR TOTAL na TELA
		this.valorFreteInput = $('.js-valorFrete'); // VALOR FRENTE na TELA
		this.valorDescontoInput = $('.js-valorDesconto'); // VALOR DESCONTO na TELA
		this.valorTotalBoxContainer = $('.js-valor-total-box-container');
		

		
		this.valorTotalItens = this.tabelaItens.valorTotal();
		this.valorFrete = this.valorFreteInput.data('valor');
		this.valorDesconto = this.valorDescontoInput.data('valor');
		
	}
	
	Venda.prototype.iniciar = function() {
		this.tabelaItens.on('tabela-itens-atualizada', onTabelaItensAtualizada.bind(this));
		
		this.valorFreteInput.on('keyup', onValorFreteAlterado.bind(this)); // Atualiza AUTOMATICAMENTE
		this.valorDescontoInput.on('keyup', onValorDescontoAlterado.bind(this));
		
		this.tabelaItens.on('tabela-itens-atualizada', onValoresAlterados.bind(this));
		this.valorFreteInput.on('keyup', onValoresAlterados.bind(this));
		this.valorDescontoInput.on('keyup', onValoresAlterados.bind(this));
		
		onValoresAlterados.call(this); // Chama ao Iniciar carregando a tela
	}
	
	//Nao permite ENTRADA DE VALORES NEGATIVOS para os ITENS de cada cerveja
	function onTabelaItensAtualizada(evento, valorTotalItens) {
		this.valorTotalItens = valorTotalItens == null ? 0 : valorTotalItens; // Retorna ZERO ou o VALOR TOTAL do ITENS
	}
	

	// Faz a modificacao "on fly" para valores de entrada de FRETE, convertando para valores sem MASCARA
	function onValorFreteAlterado(evento) {
		// Usa numeral.js (framework)
		var frete = $(evento.target).val();
		this.valorFrete = Cervejaria.recuperarValor(frete); // Transforma para um valor SEM MASCARA
		
		
	}
	
	
	// Faz a modificacao "on fly" para valores de entrada de DESCONTO, convertando para valores sem MASCARA
	function onValorDescontoAlterado(evento) {
		// Usa numeral.js (framework)
		var desconto = $(evento.target).val();
		this.valorDesconto = Cervejaria.recuperarValor(desconto);	
		
			
	}
	
	// Faz a modificacao "on fly" para valores de do VALOR TOTAL ( ja retirados FRETE + DESCONTO se existirem)
	function onValoresAlterados() {
		// Usa numeral.js (framework)
		//var valorTotal = Number(this.valorTotalItens) + Number(this.valorFrete) - Number(this.valorDesconto);
		var valorTotal = Number(this.valorTotalItens) + Number(this.valorFrete) - Number(this.valorDesconto);
		this.valorTotalBox.html(Cervejaria.formatarMoeda(valorTotal));
		
		this.valorTotalBoxContainer.toggleClass('negativo', valorTotal < 0);
	}
	
	return Venda;
	
}());

$(function() {
	
	var autocomplete = new Cervejaria.Autocomplete();
	autocomplete.iniciar();
	
	var tabelaItens = new Cervejaria.TabelaItens(autocomplete);
	tabelaItens.iniciar();
	
	var venda = new Cervejaria.Venda(tabelaItens);
	venda.iniciar();
	
});