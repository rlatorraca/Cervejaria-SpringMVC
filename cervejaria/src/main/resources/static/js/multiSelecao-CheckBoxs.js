Cervejaria = Cervejaria || {};
//Funcao JS para atualizar ATIVAR e DESATIVAR um Usuario

Cervejaria.MultiSelecao = (function() {

	function MultiSelecao() {
		this.statusBtn = $('.js-status-btn');    // pega o Objeto para os botoes ATIVADO / DESATIVADO
		this.selecaoCheckbox = $('.js-selecao'); // pega o USUARIO (individualmente) que foi SELECIONADO 
		this.selecaoTodosCheckbox = $('.js-selecao-todos'); // pega os JS para o BOTAO que seleciona TODOS os USUARIOS
	}

	MultiSelecao.prototype.iniciar = function() {
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));
		this.selecaoTodosCheckbox.on('click', onSelecaoTodosClicado.bind(this)); // Chama a Funcao onSelecaoTodosClicado() ao clicar em 'js-selecao-todos'
		this.selecaoCheckbox.on('click', onSelecaoClicado.bind(this));
	}

	function onStatusBtnClicado(event) {
		var botaoClicado = $(event.currentTarget); // Pega o Bota que foi clicado
		var status = botaoClicado.data('status');  // Recebe o valor de ATIVAR ou DESATIVAR
		var url = botaoClicado.data('url');

		var checkBoxSelecionados = this.selecaoCheckbox.filter(':checked'); // Filtra APENAS os Usuarios SELECIONADOS
		var codigos = $.map(checkBoxSelecionados, function(c) {
			return $(c).data('codigo');// Cria um ARRAY para pegar TODOS SELECIONADOS
		});

		if (codigos.length > 0) {    // apenas submite por AJAX se tiver alguma Usuario selecioando
			$.ajax({
				url : url,
				method : 'PUT',
				data : {
					codigos : codigos,
					status : status
				},
				beforeSend: adicionarCsrfToken,
				success : function() {
					window.location.reload(); // faz o RELOAD da pagina, SEM perder o que ja esta digitado nos OUTROS CAMPOS
				}
			});

		}
	}
	
	function onSelecaoTodosClicado() {
		var status = this.selecaoTodosCheckbox.prop('checked'); // Verifica se o BOTAO de SELECIONADO TODOS esta MARCADO /CHECKADO 
		this.selecaoCheckbox.prop('checked', status); // SELECIONA / DESMARCA a depdender do STATUS do valor de ".js-selecao-todos' (marca todos / desmcarca todos) 
		statusBotaoAcao.call(this, status);   // Chama funcao para HABILITAR / DESABILITAR os botoes 'Ativar' / 'Desativar'
	}
	
	// Algum USUARIO foi Selecionado
	function onSelecaoClicado() {
		var selecaoCheckboxChecados = this.selecaoCheckbox.filter(':checked'); // Faz um FILTRO dos Checbox MARCADOS (retornando os CHECADOS)
		this.selecaoTodosCheckbox.prop('checked', selecaoCheckboxChecados.length >= this.selecaoCheckbox.length); // Verifica se TODOS estao CHECADOS (usando '.js-selecao' para pegar o total de Checkbox existenteste)
		statusBotaoAcao.call(this, selecaoCheckboxChecados.length); // Chama funcao para HABILITAR / DESABILITAR os botoes 'Ativar' / 'Desativar'
	}
	
	// ATIVA / DESATIVA botao usando a classe 'DISABLE'
	function statusBotaoAcao(ativar) {
		ativar ? this.statusBtn.removeClass('disabled') : this.statusBtn.addClass('disabled');
	}
	
	function adicionarCsrfToken(xhr) {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		xhr.setRequestHeader(header, token);
	}

	return MultiSelecao;

}());

$(function() {
	var multiSelecao = new Cervejaria.MultiSelecao();
	multiSelecao.iniciar();
});