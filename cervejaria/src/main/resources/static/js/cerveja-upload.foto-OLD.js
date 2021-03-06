

		
			 var bar = document.getElementById('js-progressbar');

			    UIkit.upload('.js-upload', {
			        
			        type :'json',
			        method : 'POST',
			        allow: '*.(jpg|jpeg|png)',
			        multiple: false,
			        url: '/cervejaria/fotos',

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
			        complete: function () {
			            console.log('complete', arguments);
			            alert(arguments[0].response.nome);
			            alert(arguments[0].response.contentType);
			            $('input[name=foto]').val(arguments[0].response.nome);
						$('input[name=contentType]').val(arguments[0].response.contentType);
			        },

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

			            alert('Upload Completed');
			        }

			    });
			    

		