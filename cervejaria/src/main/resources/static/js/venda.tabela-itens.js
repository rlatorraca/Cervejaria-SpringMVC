Cervejaria.TabelaItens = (function() {
	
	function TabelaItens(autocomplete) {
		this.autocomplete = autocomplete;
		this.tabelaCervejasContainer = $('.js-tabela-cervejas-container');
		this.uuid = $('#uuid').val();
	}
	
	TabelaItens.prototype.iniciar = function() {
		this.autocomplete.on('item-selecionado', onItemSelecionado.bind(this));
	}
	
	function onItemSelecionado(evento, item) {
		var resposta = $.ajax({
			url: 'item',   // Sem a "/" troca a pagina de procura apenas para "item" e nao inclue o "Localhost"
			method: 'POST',
			data: {
				codigoCerveja : item.codigo,
				uuid : this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	

	function onItemAtualizadoNoServidor(html) {
		this.tabelaCervejasContainer.html(html);
		$('.js-tabela-cerveja-quantidade-item').on('change', onQuantidadeItemAlterado.bind(this)); // Ao mudar a QUANTIDADE de Cerveja chamada funcao
		$('.js-tabela-item').on('dblclick', onDoubleClick); // Double-click na CERVEJA
		$('.js-exclusao-item-btn').on('click', onExclusaoItemClick.bind(this)); // Ao cliccar em Exclui apos Double Click na Cerveja assina a funcao "onExclusaoItemClick"
	}
	
	// Para alterar a quantidade de cervejas adicionadas
	function onQuantidadeItemAlterado(evento) {
		var input = $(evento.target); // Recebe INPUT que fez a alteracao
		var quantidade = input.val(); // Dentro do INPUT pega a QUANTIDADE de cervejas por cada ITEM DE CERVEJA
		
		if (quantidade <= 0) {
			input.val(1);
			quantidade = 1;
		}
		
		var codigoCerveja = input.data('codigo-cerveja'); // Pega o CODIGO da CERVEJA ESCOLHIDA
		
		//Atualiza a QUANTIDADE no SERVIDOR Via AJAX
		var resposta = $.ajax({
			url: 'item/' + codigoCerveja,
			method: 'PUT',
			data: {
				quantidade: quantidade,
				uuid: this.uuid
			}
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this));
	}
	
	function onDoubleClick(evento) {
		//var item = $(evento.currentTarget); // Pega o EVENTO (cerveja escolhida na TABELA)
		//item.toggleClass('solicitando-exclusao'); // INCLUI ou EXCLUI a classe 'solicitando-exclusao' para mostrar o MODAL na parte Direita da Tela com ACEITE ou NAO (exclusao)
		// $(this) ==> escutou o EVENTO = "$(evento.currentTarget)"
		// OR
		$(this).toggleClass('solicitando-exclusao');
	}
	
	
	//Exclui a CERVEJA DA TELA
	function onExclusaoItemClick(evento) {
		var codigoCerveja = $(evento.target).data('codigo-cerveja'); // Pega o codigo da Cerveja usando o INPUT do BOTAO usando  "data:codigo-cerveja"
		var resposta = $.ajax({
			url: 'item/' + this.uuid + '/' + codigoCerveja,
			//url: 'item/' + codigoCerveja,
			method: 'DELETE'
		});
		
		resposta.done(onItemAtualizadoNoServidor.bind(this)); // Chama a funcao ACIMA (para atualizar os dados no servidor / DB)
	}
	
	function bindQuantidade() {
		var quantidadeItemInput = $('.js-tabela-cerveja-quantidade-item');
		quantidadeItemInput.on('change', onQuantidadeItemAlterado.bind(this));
		quantidadeItemInput.maskMoney({ precision: 0, thousands: '' });
	}
	
	function bindTabelaItem() {
		var tabelaItem = $('.js-tabela-item');
		tabelaItem.on('dblclick', onDoubleClick);
		$('.js-exclusao-item-btn').on('click', onExclusaoItemClick.bind(this));
		return tabelaItem;
	}
	
	return TabelaItens;
	
}());

$(function() {
	
	var autocomplete = new Cervejaria.Autocomplete();
	autocomplete.iniciar();
	
	var tabelaItens = new Cervejaria.TabelaItens(autocomplete);
	tabelaItens.iniciar();
});	