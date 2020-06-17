Cervejaria = Cervejaria || {};

Cervejaria.BotaoSubmit = (function() {
	
	function BotaoSubmit() {
		this.submitBtn = $('.js-submit-btn');
		this.formulario = $('.js-formulario-principal');
	}
	
	BotaoSubmit.prototype.iniciar = function() {
		this.submitBtn.on('click', onSubmit.bind(this));
	}
	
	function onSubmit(evento) {
		evento.preventDefault(); // Nao faz o EVENTO PADRAO do botao (que é enviar / submter o FORM)
		
		var botaoClicado = $(evento.target); // Pega o QUAL FOI o Botao Cliclado
		var acao = botaoClicado.data('acao'); // Pega o "data:acao" do BOTAO CLICADO
		
		var acaoInput = $('<input>'); // Cria uma tag "input"
		acaoInput.attr('name', acao); //Adiciona um ATRIBUTO "name", com o valor da "data:acao" do botao clicado 
		
		this.formulario.append(acaoInput); // Inclui o INPUT" criado anteriormente dentro do FORM
		this.formulario.submit(); // faz o SUBMIT do FORM
	}
	
	return BotaoSubmit
	
}());

$(function() {
	
	var botaoSubmit = new Cervejaria.BotaoSubmit();
	botaoSubmit.iniciar();
	
});