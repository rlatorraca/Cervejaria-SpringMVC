// Modularizacao do JAVA SCRIPT

// Representaocao do NAMESPEACE

// Namespace = Cervejaria OR {} (outro objeto, se brewer ja existir)
var Cervejaria = Cervejaria || {}

// Funcao construtora + Mordern Pattern
// MaskMoney esta em maisucula pois seguira o padra de Funcao Construtora
Cervejaria.EstiloCadastroRapido = (function(){ 
	
	function EstiloCadastroRapido (){
		this.modal = $('#modalCadastroRapidoEstilo');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-estilo-salvar-btn');
		this.form = this.modal.find('form');
		this.url = this.form.attr('action'); // Pegar o campo "action do formulario para criar a URL
		this.inputNomeEstilo = $('#nomeEstilo'); 
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-estilo');
		
	}
	
	
	//Usado para CONECTAR /INCIALIZAR funcoes
	EstiloCadastroRapido.prototype.iniciar = function(){
		this.form.on('submit', function(event) { event.preventDefault() }); //Nao deixar submeter o formulario
		
		// bind(this) ==> traz a funcao pra ser EXECUTADA dentro do contexto de "EstiloCadastroRapido"
		this.modal.on('shown.bs.modal', onModalShow.bind(this)); //Evento lançado quando o MODAL (ABRIR) pega-se o inputNomeEstilo para dar FOCUS 
		this.modal.on('hide.bs.modal', onModalClose.bind(this))  //Evento lançado quando o MODAL (FECHAR) pegar-se para apagar o que se encontrava no "input"
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this)); //Evanto lançado quando CLICKADO no botao para SUBMETER
		
	}
	
	function onModalShow() {
		this.inputNomeEstilo.focus();
	}
	
	function onModalClose() {
		this.inputNomeEstilo.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		this.nomeEstilo = this.inputNomeEstilo.val().trim();
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeEstilo }),
			error: onErroSalvandoEstilo.bind(this), // bind(this) ==> traz a funcao pra ser EXECUTADA dentro do contexto de "EstiloCadastroRapido"
			success: onEstiloSalvo.bind(this)       // bind(this) ==> traz a funcao pra ser EXECUTADA dentro do contexto de "EstiloCadastroRapido"
		});
	}
	
	function onErroSalvandoEstilo(obj) {
		var mensagemErro = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');
	}
	
	function onEstiloSalvo(estilo) {
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.codigo + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.codigo);
		this.modal.modal('hide');
	}

	return EstiloCadastroRapido;
	
})();
	 
$(function() {
	
	var estiloCadastroRapido = new Cervejaria.EstiloCadastroRapido();
	estiloCadastroRapido.iniciar();
	
});