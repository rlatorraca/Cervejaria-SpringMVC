 
$(function() {
	
	var modal = $('#modalCadastroRapidoEstilo');
	var botaoSalvar = modal.find('.js-modal-cadastro-estilo-salvar-btn');
	var form = modal.find('form');
	form.on('submit', function(event) { event.preventDefault() }); //Nao deixar submeter o formulario
	var url = form.attr('action'); // Pegar o campo "action do formulario para criar a URL
	var inputNomeEstilo = $('#nomeEstilo'); 
	var containerMensagemErro = $('.js-mensagem-cadastro-rapido-estilo');
	
	modal.on('shown.bs.modal', onModalShow); //Evento lançado quando o MODAL (ABRIR) pega-se o inputNomeEstilo para dar FOCUS
	modal.on('hide.bs.modal', onModalClose)  //Evento lançado quando o MODAL (FECHAR) pegar-se para apagar o que se encontrava no "input"
	botaoSalvar.on('click', onBotaoSalvarClick); //Evanto lançado quando CLICKADO no botao para SUBMETER
	
	function onModalShow() {
		inputNomeEstilo.focus();
	}
	
	function onModalClose() {
		inputNomeEstilo.val('');
		containerMensagemErro.addClass('hidden');
		form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		var nomeEstilo = inputNomeEstilo.val().trim();
		$.ajax({
			url: url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeEstilo }),
			error: onErroSalvandoEstilo,
			success: onEstiloSalvo
		});
	}
	
	function onErroSalvandoEstilo(obj) {
		var mensagemErro = obj.responseText;
		containerMensagemErro.removeClass('hidden');
		containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		form.find('.form-group').addClass('has-error');
	}
	
	function onEstiloSalvo(estilo) {
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.codigo + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.codigo);
		modal.modal('hide');
	}
	
});