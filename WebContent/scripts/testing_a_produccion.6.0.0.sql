update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/Envio' where id_parametro = 'URL_RNCENTRAL_ENVIO_ACUSES';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/ConfRecepcionResp' where id_parametro = 'URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/Error' where id_parametro = 'URL_RNCENTRAL_ENVIO_ERRORES';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/Envio' where id_parametro = 'URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/Envio' where id_parametro = 'URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Interfase.cgi/cgi-bin/Receptor.cgi/EnvioInforme' where id_parametro = 'URL_RNCENTRAL_MONITOREO';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarRespuestas' where id_parametro = 'URL_RNCENTRAL_RECEPCION_ACUSES';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/ConfRecepcionRech' where id_parametro = 'URL_RNCENTRAL_RECEPCION_CONF_RECHAZOS_DGI';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarEmpresas' where id_parametro = 'URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarRechazosDGI' where id_parametro = 'URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarSobres' where id_parametro = 'URL_RNCENTRAL_RECEPCION_SOBRES';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarUsuarios' where id_parametro = 'URL_RNCENTRAL_RECEPCION_USUARIOS';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/BajarValoresUI' where id_parametro = 'URL_RNCENTRAL_VALORES_UI';
update parametros set valor_string = 'https://cfe.rondanet.com/cgi-bin/Receptor.cgi/EnvioInforme' where id_parametro = 'URL_RNCENTRAL_ENVIO_STATUS';
update parametros set valor_string = 'https://efactura.dgi.gub.uy/efactura/ws_efactura' where id_parametro = 'URL_WS_DGI';
update parametros set valor_string = 'https://efactura.dgi.gub.uy:6440/efactura/ws_consultas' where id_parametro = 'URL_WS_CONSULTA_DGI';

update parametros set valor_integer = 864000 where id_parametro = 'TIEMPO_DEPURACION_ACK_INTERNOS';
update parametros set valor_integer = 2592000 where id_parametro = 'TIEMPO_DEPURACION_ALARMAS';
update parametros set valor_integer = 2592000 where id_parametro = 'TIEMPO_DEPURACION_CFE_ENVIADOS';
update parametros set valor_integer = 2592000 where id_parametro = 'TIEMPO_DEPURACION_CFE_RECIBIDOS';
update parametros set valor_integer = 2592000 where id_parametro = 'TIEMPO_DEPURACION_LOGS';
update parametros set valor_integer = 0 where id_parametro = 'TIEMPO_DEPURACION_LOGS_USUARIO';
update parametros set valor_integer = 0 where id_parametro = 'TIEMPO_DEPURACION_NO_ENVIADOS';
update parametros set valor_integer = 30 where id_parametro = 'TIEMPO_ENSOBRADO_CFE_HP';

update parametros set valor_bool = false where id_parametro = 'MODO_CERTIFICACION';

update parametros set valor_string = '214844360018' where id_parametro = 'RUT_DGI';


