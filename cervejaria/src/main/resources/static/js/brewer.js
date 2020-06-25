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
		this.decimal.maskMoney({ decimal: ",", thousands: ".", allowZero: true });
		this.plain.maskMoney({ precision: 0 , thousands: ",", allowZero: true });
		
		// using masknumber.js
		//this.decimal.maskMoney({ decimal: ",", thousands: "."});
		//this.plain.maskMoney({ interger: true , thousands: "," });
		
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

//Formata com NUMERAL.js (Framework)
Cervejaria.formatarMoeda = function(valor) {
	numeral.locale('pt-br');
	return numeral(valor).format('0,0.00');
}

//UNFormata com NUMERAL.js (Framework)
Cervejaria.recuperarValor = function(valorFormatado) {
	numeral.locale('pt-br');	
	var numero = numeral(valorFormatado);	
	return numero._value;
}


Cervejaria.UrlBundle = (function(){
	
	//Inicializacao
	function UrlBundle() {	
		
		this.inputUrlEn = $('.js-languages-en');
		this.inputUrlFr = $('.js-languages-fr');
		this.inputUrlPt = $('.js-languages-pt');
		

		
	}
	
	//Execucao
	UrlBundle.prototype.enable = function(){	
		
		getUrl(this.inputUrlFr.data('lang'));
		getUrl(this.inputUrlPt.data('lang'));
		getUrl(this.inputUrlEn.data('lang'));
		
		//$('.js-languages-en').on('click', onSelectecLanguage.bind(this));
		//$('.js-languages-fr').on('click', onSelectecLanguage.bind(this));
		//$('.js-languages-pt').on('click', onSelectecLanguage.bind(this));	
		
	}
	
	function getUrl(language) {
		var linkRaw = window.location.href 
		var existOnUrl = linkRaw.search('lang=');
		var link ;
		if (existOnUrl != -1){ // Se achou o lang n
			 var url = window.location.href;
             url = url.split('?lang=');
             url = url[0];             
             link = url + "?lang=" + language;
             
		} else {
			link = window.location.href + "?lang=" + language;
			
			
		}	
		
		var atributo = ".js-languages-"+language.slice(0,2);
		$(atributo).removeAttr("href");
		$(atributo).attr("href", link);
		retorno = $(atributo).attr('href'); 
		
				
		return retorno;
	}
	
	function onSelectecLanguage(evento){	
		var languageChosen = $(evento.currentTarget); // Pega quem DISPAROU o EVENTO de CLICK
		var tongue = languageChosen[0].dataset.lang;
		var lang = tongue.slice(0,2);
		
		//setUptongue(lang);
	}
	
	function setUpTongue(tongue){
		$('.js-languages-en').removeClass('selected');
		$('.js-languages-fr').removeClass('selected');
		$('.js-languages-pt').removeClass('selected');		
		var atributo = ".js-languages-"+tongue
		$(atributo).addClass('selected');		
	}
	
	return UrlBundle;
})();






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
	
	var urlBundle = new Cervejaria.UrlBundle();
	urlBundle.enable();
});