var Cervejaria = Cervejaria || {};

Cervejaria.UploadFoto = (function() {
	
	function UploadFoto() {
		this.inputNomeFoto = $('input[name=foto]');
		this.inputContentType = $('input[name=contentType]');
		
		this.htmlFotoCervejaTemplate = $('#foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoCervejaTemplate);
		
		this.containerFotoCerveja = $('.js-container-foto-cerveja');
		
		this.uploadDrop = $('.js-upload');
	}
	
	
	UploadFoto.prototype.iniciar = function () {
		
		var bar = document.getElementById('js-progressbar');

	    UIkit.upload('.js-upload', {
	        
	        type :'json',
	        method : 'POST',
	        allow: '*.(jpg|jpeg|png)',
	        multiple: false,
	        url: this.containerFotoCerveja.data('url-fotos'),
	        
	        beforeAll: function () {
	            console.log('beforeAll', arguments);
	            
	        },
	        load: function () {
	            console.log('load', arguments);
	           
	        },
	        error: function () {
	            console.log('error', arguments);
	        },
	        
//	            console.log(arguments[0].response.nome);
//	            this.inputNomeFoto.val(arguments[0].response.nome);
	            //$('input[name=foto]').val(arguments[0].response.nome);
	            
//	            this.inputContentType.val(arguments[0].response.contentType);
				//$('input[name=contentType]').val(arguments[0].response.contentType);
	            
//	            this.uploadDrop.addClass('hidden');
//	    		var htmlFotoCerveja = this.template({nomeFoto: arguments[0].response.nome});
//	    		this.containerFotoCerveja.append(htmlFotoCerveja);
//	    		
//	    		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
//	        	
//	        },

	        loadStart: function (e) {
	            console.log('loadStart', arguments);

	            bar.removeAttribute('hidden');
	            bar.max = e.total;
	            bar.value = e.loaded;
	        },

	        progress: function (e) {
	            console.log('progress', arguments);

	            bar.max = e.total;
	            bar.value = e.loaded;
	        },

	        loadEnd: function (e) {
	            console.log('loadEnd', arguments);

	            bar.max = e.total;
	            bar.value = e.loaded;
	        },

	        completeAll: function () {
	            console.log('completeAll', arguments);

	            setTimeout(function () {
	                bar.setAttribute('hidden', 'hidden');
	            }, 1000);

	           // alert('Upload Completed');
	        },
	        
	        complete: onUploadCompleto.bind(this),
	        
	        beforeSend:	function (environment) {	        	
	        	console.log('beforeSend', arguments);
	        	adcionarCsrfToken();
	        	
	           	var token = $('input[name=_csrf]').val();
		        var header = $('input[name=_csrf_header]').val();
		        environment.xhr.setRequestHeader(header, token); 
	            
	            

	            // The environment object can still be modified here. 
	            // var {data, method, headers, xhr, responseType} = environment;

	        },

	    });

	    
	    if (this.inputNomeFoto[0].defaultValue) {
			onUploadCompleto.call(this, { nome:  this.inputNomeFoto[0].defaultValue, contentType: this.inputContentType[0].defaultValue});
		}    
		
	}	
	

	function onUploadCompleto(resposta) {
			
		if(resposta.response)
			this.inputNomeFoto.val(resposta.response.nome);
		else {
			this.inputNomeFoto.val(resposta.nome);
		}
		
		if(resposta.response) {
			this.inputContentType.val(resposta.response.contentType);
		} else {
			this.inputContentType.val(resposta.contentType);
		}
		
		
				
		this.uploadDrop.addClass('hidden');
		if(resposta.response){
			var htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome});
		} else {
			var htmlFotoCerveja = this.template({nomeFoto: resposta.nome});
		}
		//var htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
		
	function onRemoverFoto() {
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
	}
	
	//Funcao usada para pegar o NOME e a chave CSRF na pagina Layout padrao 
	function adcionarCsrfToken(jqXHR) {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		jqXHR.setRequestHeader(header, token); // Adicona o Token na requisicao
	}

	
	return UploadFoto;
	
})();

$(function() {
	var uploadFoto = new Cervejaria.UploadFoto();
	uploadFoto.iniciar();
});
