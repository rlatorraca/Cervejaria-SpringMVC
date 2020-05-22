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

Cervejaria.MaskPhoneNumber = (function() {
	
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');
	}
	
	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function (val) {
		  return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		var options = {
		  onKeyPress: function(val, e, field, options) {
		      field.mask(maskBehavior.apply({}, arguments), options);
		    }
		};
		
		this.inputPhoneNumber.mask(maskBehavior, options);
	}
	
	return MaskPhoneNumber;
	
}());

$(function() {
	var maskMoney = new Cervejaria.MaskMoney();
	maskMoney.enable();
	
	var maskPhoneNumber = new Cervejaria.MaskPhoneNumber();
	maskPhoneNumber.enable();
});