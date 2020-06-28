insert into venda (data_criacao, valor_total, status, codigo_cliente, codigo_usuario) 
  values (
    values (from_unixtime(ROUND(RAND()*(1593302400 - 1546300800) + 1546300800)
  , round(rand() * 10000, 2)
  , 'EMITIDA'
  , round(rand() * 7) + 1
  , round(rand() * 2) + 1)
