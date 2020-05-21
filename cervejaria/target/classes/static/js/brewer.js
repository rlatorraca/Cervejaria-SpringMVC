// Modularizacao do JAVA SCRIPT

// Representaocao do NAMESPEACE

// Namespace = Cervejaria OR {} (outro objeto, se brewer ja existir)
var Cervejaria = Cervejaria || {}

// Funcao construtora + Mordern Pattern
// MaskMoney esta em maisucula pois seguira o padra de Funcao Construtora
Cervejaria.MaskMoney = (function(){
	
	//Inicializacao
	function MaskMoney(){
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');		
	}
	
	//Execucao
	MaskMoney.prototype.enable = function(){
		this.decimal.maskMoney({ decimal: ".", thousands: ",", allowZero: true });
		this.plain.maskMoney({ precision: 0 , thousands: ",", allowZero: true });
		
	}
	
	return MaskMoney;
})();

$(function() {
	var maskMoney = new Cervejaria.MaskMoney();
	maskMoney.enable();
});