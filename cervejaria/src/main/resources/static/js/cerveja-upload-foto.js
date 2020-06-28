var Cervejaria = Cervejaria || {};

Cervejaria.UploadFoto = (function() {
	
	function UploadFoto() {
		this.inputNomeFoto = $('input[name=foto]');
		this.inputContentType = $('input[name=contentType]');
		this.novaFoto = $('input[name=novaFoto]');
		this.inputURLFoto = $('input[name=urlFoto]');
		
		this.htmlFotoCervejaTemplate = $('#foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoCervejaTemplate);
		
		this.containerFotoCerveja = $('.js-container-foto-cerveja');
		
		this.uploadDrop = $('.js-upload');
		this.imgLoading = $('.js-img-loading');
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
	        beforeSend:	function (environment) {	        	
	        	console.log('beforeSend', arguments);
	        	var token = $('input[name=_csrf]').val();
	    		var header = $('input[name=_csrf_header]').val();
	    		environment.headers[header] = token;
	
	    		//console.log(environment.headers);
	    		//environment.headers(header, token); // Adicona o Token na requisicao
	            

	            // The environment object can still be modified here. 
	            // var {data, method, headers, xhr, responseType} = environment;

	        },
	        
	        load: function () {
	            console.log('load', arguments);
	           
	        },
	        
	        error: function () {
	            console.log('error', arguments);
	        },
	        

	        loadStart: function (e) {
	            console.log('loadStart', arguments);
	            
	            $('.js-img-loading').removeClass('hidden');
	            
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
	        
	        complete: onUploadCompleto.bind(this)
	        
	        

	    });

	    
	    if (this.inputNomeFoto[0].defaultValue) {
			//onUploadCompleto.call(this, { nome:  this.inputNomeFoto[0].defaultValue, contentType: this.inputContentType[0].defaultValue});
	    	renderizarFoto.call(this, { nome:  this.inputNomeFoto[0].defaultValue, 
	    							    contentType: this.inputContentType[0].defaultValue, 
	    							    url : this.inputURLFoto[0].defaultValue
	    							   }
	    						);
		}    
		
	}	
	function uploadLoadingImage(resposta){
		this.imgLoading.removeClass('hidden');
	}

	function onUploadCompleto(resposta) {
			
		this.novaFoto.val('true');
		this.inputURLFoto.val(resposta.response.url);
		
		renderizarFoto.call(this, resposta);
		
		 $('.js-img-loading').addClass('hidden');
		/*
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
		
		var foto='';
		if(fotoNova){
			foto='/temp';
		}
				
		this.uploadDrop.addClass('hidden');
		var htmlFotoCerveja
		if(resposta.response){
			htmlFotoCerveja = this.template({foto: resposta.response.nome});
		} else {
			htmlFotoCerveja = this.template({foto: resposta.nome});
		}
		//var htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
		*/
	}
	
	function renderizarFoto(resposta){
		
		if(resposta.response){
			this.inputNomeFoto.val(resposta.response.nome);
			this.inputContentType.val(resposta.response.contentType);
			this.inputURLFoto.val(resposta.response.url);
		} else {
			this.inputContentType.val(resposta.contentType);
			this.inputNomeFoto.val(resposta.nome);
			this.inputURLFoto.val(resposta.url);
		}
		
//		if(resposta.response) {
//			this.inputContentType.val(resposta.response.contentType);
//		} else {
//			this.inputContentType.val(resposta.contentType);
//		}
		
		this.uploadDrop.addClass('hidden');
		
		var foto='';
		
		/*
		if (this.novaFoto.val() == 'true') {
			foto = 'temp/';
		} */
		
		var htmlFotoCerveja;
		
		
		if(resposta.response){
			foto += resposta.response.url;
			//htmlFotoCerveja = this.template({foto: foto});			
		} else {
			foto += resposta.url;
			//htmlFotoCerveja = this.template({foto: foto});
		} 
		htmlFotoCerveja = this.template({url: foto});
		//var htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
		
	function onRemoverFoto() {
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
		this.novaFoto.val('false');
	}
	
	//Funcao usada para pegar o NOME e a chave CSRF na pagina Layout padrao 
	function adcionarCsrfToken() {
		var token = $('input[name=_csrf]').val();
		var header = $('input[name=_csrf_header]').val();
		enviroment.headers(header, token); // Adicona o Token na requisicao
	}

	
	return UploadFoto;
	
})();

$(function() {
	var uploadFoto = new Cervejaria.UploadFoto();
	uploadFoto.iniciar();
});
