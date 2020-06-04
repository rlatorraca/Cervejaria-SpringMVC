Cervejaria = Cervejaria || {};
//Funcao JS para atualizar ATIVAR e DESATIVAR um Usuario

Cervejaria.MultiSelecao = (function() {

	function MultiSelecao() {
		this.statusBtn = $('.js-status-btn');    // pega o Objeto para os botoes ATIVADO / DESATIVADO
		this.selecaoCheckbox = $('.js-selecao'); // pega o USUARIO selecionado para ser ATIVADO ou DESATIVDADO
	}

	MultiSelecao.prototype.iniciar = function() {
		this.statusBtn.on('click', onStatusBtnClicado.bind(this));
	}

	function onStatusBtnClicado(event) {
		var botaoClicado = $(event.currentTarget); // Pega o Bota que foi clicado
		var status = botaoClicado.data('status');  // Recebe o valor de ATIVAR ou DESATIVAR

		var checkBoxSelecionados = this.selecaoCheckbox.filter(':checked'); // Filtra APENAS os Usuarios SELECIONADOS
		var codigos = $.map(checkBoxSelecionados, function(c) {
			return $(c).data('codigo');// Cria um ARRAY para pegar TODOS SELECIONADOS
		});

		if (codigos.length > 0) {    // apenas submite por AJAX se tiver alguma Usuario selecioando
			$.ajax({
				url : '/cervejaria/usuarios/status',
				method : 'PUT',
				data : {
					codigos : codigos,
					status : status
				},
				success : function() {
					window.location.reload(); // faz o RELOAD da pagina, SEM perder o que ja esta digitado nos OUTROS CAMPOS
				}
			});

		}
	}

	return MultiSelecao;

}());

$(function() {
	var multiSelecao = new Cervejaria.MultiSelecao();
	multiSelecao.iniciar();
});