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

	        beforeSend: function (environment) {
	            console.log('beforeSend', arguments);

	            // The environment object can still be modified here. 
	            // var {data, method, headers, xhr, responseType} = environment;

	        },
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
	        
	        complete: onUploadCompleto.bind(this)

	    });

	    
	    if (this.inputNomeFoto.val()) {
	    	alert('>>> this.inputNomeFoto.val() - INCIO' );
			onUploadCompleto.call(this, { nome:  this.inputNomeFoto.val(), contentType: this.inputContentType.val()});
			alert('>>> this.inputNomeFoto.val() - FIM' );
		}    
		
	}	
	

	function onUploadCompleto(resposta) {
		
		
		
		this.inputNomeFoto.val(resposta.response.nome);
		this.inputContentType.val(resposta.response.contentType);
		
		console.log(this.inputNomeFoto.val());
		console.log(this.inputContentType.val());
		
		console.log('>>> onUploadCompleto(resposta) - MEIO' );
		
		this.uploadDrop.addClass('hidden');
		var htmlFotoCerveja = this.template({nomeFoto: resposta.response.nome});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));
	}
		
	function onRemoverFoto() {
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
	}
	
	return UploadFoto;
	
})();

$(function() {
	var uploadFoto = new Cervejaria.UploadFoto();
	uploadFoto.iniciar();
});