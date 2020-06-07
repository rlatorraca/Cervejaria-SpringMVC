Cervejaria = Cervejaria || {};

//Funcao para fazer a pesquisa rapida de Clientes em "Pagina de Pedidos"
Cervejaria.PesquisaRapidaCliente = (function() {
	
	function PesquisaRapidaCliente() {
		this.pesquisaRapidaClientesModal = $('#pesquisaRapidaClientes'); // Cria um OBJETO ara o ID principal / primeiro
		this.nomeInput = $('#nomeClienteModal'); // Pegua o NOME DO CLIENTE para ser pesquisado
		this.pesquisaRapidaBtn = $('.js-pesquisa-rapida-clientes-btn');  // Pega os dados do BOTAO que SUBMETE a pesquisa rapida
		this.containerTabelaPesquisa = $('#containerTabelaPesquisaRapidaClientes'); 
		this.htmlTabelaPesquisa = $('#tabela-pesquisa-rapida-cliente').html(); // Pega o container HTML do HANDLEBARS (SCRIPT)
		this.template = Handlebars.compile(this.htmlTabelaPesquisa);  //Compile o SCRIPT do HANDLEBARS
		this.mensagemErro = $('.js-mensagem-erro');
	}
	
	PesquisaRapidaCliente.prototype.iniciar = function() {
		this.pesquisaRapidaBtn.on('click', onPesquisaRapidaClicado.bind(this));  //Ao CLICAR no BOTAO aciona o AJAX
		this.pesquisaRapidaClientesModal.on('shown.bs.modal', onModalShow.bind(this));  // Coloca o FOCO (Focus) no campo do MODAL

	}
	
	function onModalShow() {
		this.nomeInput.focus();
	}
	
	function onPesquisaRapidaClicado(event) {
		
		event.preventDefault(); // NAO FAZ o comportamento DEFAULT = Submeter o Formulario
		
		$.ajax({
			url: this.pesquisaRapidaClientesModal.find('form').attr('action'),
			method: 'GET',
			contentType: 'application/json',
			data: {
				nome: this.nomeInput.val()
			}, 
			//beforeSend: adicionarCsrfToken,
			success: onPesquisaConcluida.bind(this),
			error: onErroPesquisa.bind(this)
		});
	}
	
	function adicionarCsrfToken(xhr) {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		console.log(token);
		console.log(header);
		xhr.setRequestHeader(header, token);
	}
	
	function onPesquisaConcluida(resultado) {
		this.mensagemErro.addClass('hidden');
		
		var html = this.template(resultado); // Pega o RESULTADO da PESQUISA EM JSON e coloca dentro do SCRIPT do HANDLEBARS
		this.containerTabelaPesquisa.html(html);
		
		var tabelaClientePesquisaRapida = new Cervejaria.TabelaClientePesquisaRapida(this.pesquisaRapidaClientesModal);
		tabelaClientePesquisaRapida.iniciar();
	} 
	
	// Mostra o ERRO se VAZIO ou MENOR que 3 letras na pesquisa
	function onErroPesquisa() {
		this.mensagemErro.removeClass('hidden');
	}
	
	return PesquisaRapidaCliente;
	
}());


// Representa os CLIENTES da TABELA de PESQUISA RAPIDA de CLIENTES
Cervejaria.TabelaClientePesquisaRapida = (function() {
	
	function TabelaClientePesquisaRapida(modal) {
		this.modalCliente = modal;
		this.cliente = $('.js-cliente-pesquisa-rapida');
	}
	
	TabelaClientePesquisaRapida.prototype.iniciar = function() {
		this.cliente.on('click', onClienteSelecionado.bind(this));
	}
	
	//Pega o Cliente Selecionado
	function onClienteSelecionado(evento) {
		this.modalCliente.modal('hide');  // Fecha o MODAL da Tela ao CLICAR no NOME DO CLIENTE
		
		var clienteSelecionado = $(evento.currentTarget);
		$('#nomeCliente').val(clienteSelecionado.data('nome'));
		$('#codigoCliente').val(clienteSelecionado.data('codigo'));
	}
	
	return TabelaClientePesquisaRapida;
	
}());

$(function() {
	var pesquisaRapidaCliente = new Cervejaria.PesquisaRapidaCliente();
	pesquisaRapidaCliente.iniciar();
});