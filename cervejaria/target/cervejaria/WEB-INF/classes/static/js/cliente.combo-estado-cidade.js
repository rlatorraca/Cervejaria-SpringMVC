var Cervejaria = Cervejaria || {};

Cervejaria.ComboEstado = (function() {
	
	function ComboEstado() {
		this.combo = $('#estado');
		this.emitter = $({});							// Auxilia no lançamento de EVENTOS  - OBJEO DO JQUERY
		this.on = this.emitter.on.bind(this.emitter);   // Lança Eventos
	}
	
	ComboEstado.prototype.iniciar = function() {
		this.combo.on('change', onEstadoAlterado.bind(this)); // No caso do MUDANCA no estado do #estado
	}
	
	function onEstadoAlterado() {
		this.emitter.trigger('alterado', this.combo.val()); // DISPARA ou LANÇA um evento chamado "ALTERADO", mandando o valor do #estado
	}
	
	return ComboEstado;
	
}());

Cervejaria.ComboCidade = (function() {
	
	function ComboCidade(comboEstado) {
		this.comboEstado = comboEstado;
		this.combo = $('#cidade');
		this.imgLoading = $('.js-image-loading');
		this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
	}
	
	ComboCidade.prototype.iniciar = function() {
		resetar.call(this); // Chama a funcao RESET() (abaixo ) para reinicializar funcoes padroes
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this));    // RECEBE da funcao "onEstadoAlterado" a funcao linha (16) ==> this.emitter.trigger('alterado', this.combo.val());
		var codigoEstado = this.comboEstado.combo.val();
		inicializarCidades.call(this, codigoEstado);
	}
	
	function onEstadoAlterado(evento, codigoEstado) {
		this.inputHiddenCidadeSelecionada.val('');
		inicializarCidades.call(this, codigoEstado);
	}
	
	function inicializarCidades(codigoEstado) {
		if (codigoEstado) {
			var resposta = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: { 'estado': codigoEstado }, 
				
				beforeSend: iniciarRequisicao.bind(this),
				
				complete: finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarCidadesFinalizado.bind(this));
		} else {
			resetar.call(this);
		}
	}
	
	function onBuscarCidadesFinalizado(cidades) {
		var options = [];
		cidades.forEach(function(cidade) {
			options.push('<option value="' + cidade.codigo + '">' + cidade.nome + '</option>');
		});
		
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		var codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
		if (codigoCidadeSelecionada) {
			this.combo.val(codigoCidadeSelecionada);
		}
	}
	
	function resetar() {
		this.combo.html('<option value="">Selecione a cidade</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao() {
		resetar.call(this);
		this.imgLoading.show();
	}
	
	function finalizarRequisicao() {
		this.imgLoading.hide();
	}
	
	return ComboCidade;
	
}());

$(function() {
	
	var comboEstado = new Cervejaria.ComboEstado();
	comboEstado.iniciar();
	
	var comboCidade = new Cervejaria.ComboCidade(comboEstado);
	comboCidade.iniciar();
	
});