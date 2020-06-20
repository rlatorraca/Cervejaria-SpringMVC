Cervejaria = Cervejaria || {};

Cervejaria.DialogoExcluir = (function() {
	
	function DialogoExcluir() {
		this.exclusaoBtn = $('.js-exclusao-btn')
	}
	
	DialogoExcluir.prototype.iniciar = function() {
		this.exclusaoBtn.on('click', onExcluirClicado.bind(this));
		
		// Verifica se ja tem o paramentro "?excluido"
		if (window.location.search.indexOf('excluido') > -1) {
			swal('Pronto!', 'Excluído com sucesso!', 'success'); // Mostra a mensagem de EXCLUIDO com SUCESSO
		}
	}
	
	function onExcluirClicado(evento) {
		event.preventDefault(); // Para nao executar o Comportamento DEFAULT
		var botaoClicado = $(evento.currentTarget); // Pega quem DISPAROU o EVENTO de CLICK
		var url = botaoClicado.data('url'); // Pega a URL (dentro da URL ja existe o CODIGO para excluir)
		var objeto = botaoClicado.data('objeto'); // Pega o Objeto
		
		//swal = é um framework JS ja incluido no Layout da aplicacao (https://sweetalert2.github.io/)
		swal({
			title: 'Tem certeza?',
			text: 'Excluir "' + objeto + '"? Você NÃO poderá recuperar depois.',
			showCancelButton: true,
			confirmButtonColor: '#DD6B55',
			confirmButtonText: 'Sim, exclua agora!',
			closeOnConfirm: false
		}, onExcluirConfirmado.bind(this, url));
	}
	
	function onExcluirConfirmado(url) {
		$.ajax({
			url: url,
			method: 'DELETE',
			success: onExcluidoSucesso.bind(this),
			error: onErroExcluir.bind(this)
		});
	}
	
	function onExcluidoSucesso() {
		//window.location.reload ==> faz o reload da pagina HTML
		var urlAtual = window.location.href; // Pega a URL atual
		var separador = urlAtual.indexOf('?') > -1 ? '&' : '?'; //Verifica se tem "?", coloca o "&", senao usa "?"
		var novaUrl = urlAtual.indexOf('excluido') > -1 ? urlAtual : urlAtual + separador + 'excluido'; // Cria a Nova URL acrescentando "?excluido"
		
		window.location = novaUrl; // o endereco da pagina sera acrescentado por "?excluido" ou "&excluido" (2 ou mais delecoes)
	}
	
	function onErroExcluir(e) {
		console.log('ahahahah', e.responseText);
		swal('Oops!', e.responseText, 'error'); // Pega o TEXTO do ERRO do JS e apresenta na tela 
	}
	
	return DialogoExcluir;
	
}());

$(function() {
	var dialogo = new Cervejaria.DialogoExcluir();
	dialogo.iniciar();
});