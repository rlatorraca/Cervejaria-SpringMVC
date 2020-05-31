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

Cervejaria.MaskCep = (function() {
	
	function MaskCep() {
		this.inputCep = $('.js-cep');
	}
	
	MaskCep.prototype.enable = function() {
		this.inputCep.mask('00.000-000');
	}
	
	return MaskCep;
	
}());

Cervejaria.MaskDate = (function() {
	
	function MaskDate() {
		this.inputDate = $('.js-date');
	}
	
	MaskDate.prototype.enable = function() {
		this.inputDate.mask('00/00/0000');
		this.inputDate.datepicker({
			orientation: 'bottom',
			language: 'pt-BR',
			autoclose: true
		});
	}
	
	return MaskDate;
	
}()); 

Cervejaria.Security = (function() {
	
	function Security() {
		this.token = $('input[name=_csrf]').val();
		this.header = $('input[name=_csrf_header]').val();
	}
	
	Security.prototype.enable = function() {
		$(document).ajaxSend(function(event, jqxhr, settings) {
			jqxhr.setRequestHeader(this.header, this.token);
		}.bind(this));
	}
	
	return Security;
	
}());

$(function() {
	var maskMoney = new Cervejaria.MaskMoney();
	maskMoney.enable();
	
	var maskPhoneNumber = new Cervejaria.MaskPhoneNumber();
	maskPhoneNumber.enable();
	
	var maskCep = new Cervejaria.MaskCep();
	maskCep.enable();
	
	var maskDate = new Cervejaria.MaskDate();
	maskDate.enable();
	
	var security = new Cervejaria.Security();
	security.enable();
});