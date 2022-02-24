-- Se debe modificar el nombre de la base de datos por la que fue creada para el cliente
-- GRANT ALL ON DATABASE "rnlocal" TO rnlocal;
-- Se debe eliminar las lineas hasta la superior del grant

CREATE TABLE COMPROBANTES (ID_COMPROBANTE  BIGSERIAL NOT NULL, XML OID, PDF BYTEA, EMITIDO_RECIBIDO VARCHAR(1) NOT NULL, ARCHIVO_ORIGINAL OID, SERIE varchar(2) NOT NULL, NRO_DOCUMENTO int8 NOT NULL, FECHA timestamp NOT NULL, RAZON_SOCIAL_EMISOR varchar(255) NOT NULL, RUT_EMISOR varchar(15) NOT NULL, RAZON_SOCIAL_RECEPTOR varchar(255), RUT_RECEPTOR varchar(20), TS_FIRMA timestamp, DIGEST_FIRMA varchar(255) NOT NULL, ORDINAL_SOBRE int4, FECHA_PROCESADO timestamp NOT NULL, SUCURSAL int4 NOT NULL, MONEDA_TRANSACCION varchar(3) , TIPO_CAMBIO numeric(7, 3), TOTAL_MNT_NO_GRAVADO numeric(16, 2), TOTAL_MNT_EXPORTACION numeric(16, 2), TOTAL_MNT_NO_FACTURABLE numeric(16,2), TOTAL_MNT_PERCIBIDO numeric(16, 2), TOTAL_MNT_A_PAGAR numeric(16,2), TOTAL_MNT_IVA_SUSPENSO numeric(16, 2), TOTAL_MNT_IVA_BAS numeric(16, 2), TOTAL_MNT_IVA_MIN numeric(16, 2), TOTAL_MNT_IVA_OTRA numeric(16, 2), MNT_IVA_MIN numeric(16, 2), MNT_IVA_BAS numeric(16, 2), MNT_IVA_OTRA numeric(16, 2), IVA_TASA_MIN numeric(6, 3), IVA_TASA_BAS numeric(6, 3), TOTAL_MNT_RETENIDO numeric(16, 2), TOTAL_MNT_CRED_FISCAL numeric(16, 2), TOTAL_MNT_TOTAL numeric(16, 2) NOT NULL, CANT_LIN_DETALLE integer, ENVIAR_DGI bool, ENVIAR_DC bool, ENVIAR_RE bool, NRO_INTERNO int8, SERIE_INTERNO varchar(2), SERIAL_CERTIFICADO varchar(255), ID_TIPO_COMPROBANTE int4 NOT NULL, ID_SOBRE_DGI int8, ID_SOBRE_RE int8, ID_CAE_ASIGNADO int8, ID_REPORTE_DIARIO int8, ID_DETALLE_ACUSE_RE int8, ID_DETALLE_ACUSE_DGI int8, PRIMARY KEY (ID_COMPROBANTE), ID_LOTE BIGINT, ORDEN_EN_LOTE BIGINT, data_entry bool, forma_pago smallint, marcado bool, persistir_corresp boolean DEFAULT false, transporte bigint, estado_impresion_recibo integer NOT NULL DEFAULT 0, id_cliente character varying(30), lugar_dest_ent character varying(100), compra_id character varying(50), publicar boolean);
CREATE TABLE TIPOS_COMPROBANTE (ID_TIPO_COMPROBANTE int4 NOT NULL, DESCRIPCION varchar(100) NOT NULL, STOCK_MINIMO_COMPROBANTES int8, REPOSICION_COMPROBANTES int8, TEMPLATE_IMPRESION varchar(255), id_impresora_emitidos int8, id_impresora_recibidos int8, NUMERAR bool NOT NULL, FIRMAR bool NOT NULL, CLASS_IMPRESION character varying(255), copias_impresion_emitidos int8, copias_impresion_recibidos int8, enviar_siempre boolean NOT NULL, PRIMARY KEY (ID_TIPO_COMPROBANTE));
CREATE TABLE IMPRESORAS (ID_IMPRESORA  BIGSERIAL NOT NULL, NOMBRE varchar(200) NOT NULL, DESCRIPCION varchar(200) NOT NULL, RUTA varchar(255) NOT NULL, PRIMARY KEY (ID_IMPRESORA));
CREATE TABLE CAES (ID_CAE  BIGSERIAL NOT NULL, SERIE varchar(2) NOT NULL, DESDE int8 NOT NULL, HASTA int8 NOT NULL, VENCIMIENTO date NOT NULL, FECHA_INGRESO date NOT NULL, FECHA_CIERRE date, MOTIVO_ANULACION varchar(255), TIPO_COMPROBANTE int4, fecha_alarma_vencimiento date, PRIMARY KEY (ID_CAE));
CREATE TABLE RECEPTORES_CAE (ID_RECEPTOR_CAE serial NOT NULL, DESCRIPCION varchar(255) NOT NULL, numera_rondanet boolean, alias VARCHAR(50), PRIMARY KEY (ID_RECEPTOR_CAE));
CREATE TABLE CAES_ASIGNADOS (ID_CAE_ASIGNADO  BIGSERIAL NOT NULL, ID_CAE int8 NOT NULL, ID_RECEPTOR_CAE int4 NOT NULL, DESDE int4 NOT NULL, HASTA int4 NOT NULL, ASIGNADO date NOT NULL, ID_SOLICITUD varchar(255), XML_ASIGNACION OID, XML_SOLICITUD OID, ID_USUARIO int4, PRIMARY KEY (ID_CAE_ASIGNADO));
CREATE TABLE PARAMETROS (ID_PARAMETRO varchar(80) NOT NULL, DESCRIPCION varchar(255), VALOR_STRING varchar(255), VALOR_INTEGER int4, VALOR_BOOL bool, VALOR_DATE timestamp, VALOR_PASSWORD varchar(255), VALOR_NUMERICO numeric, PRIMARY KEY (ID_PARAMETRO));
CREATE TABLE SOBRES (ID_SOBRE BIGSERIAL NOT NULL, EMITIDO_RECIBIDO VARCHAR(1) NOT NULL, XML OID, NOMBRE_ARCHIVO varchar(255), RUT_RECEPTOR varchar(15) NOT NULL, RUT_EMISOR varchar(15) NOT NULL, FECHA_CREACION timestamp NOT NULL, ID_EMISOR int8 NOT NULL, FECHA_TRANSMISION timestamp, TOKEN_CONSULTA varchar(255), TIEMPO_CONSULTA_RESPUESTA int8, FECHA_CONSULTA_RESPUESTA timestamp, ID_DETALLE_ACUSE int8, ULTIMO_INTENTO_ENVIO DATE, INTENTOS_ENVIO INT8, PRIMARY KEY (ID_SOBRE));
CREATE TABLE ACUSES (ID_ACUSE BIGSERIAL NOT NULL, XML OID, RUT_EMISOR varchar(15) NOT NULL, RUT_RECEPTOR varchar(15) NOT NULL, ID_RESPUESTA int8, ID_EMISOR int8, ID_RECEPTOR int8, TS_FIRMA timestamp, FECHA_TRANSMISION timestamp, PRIMARY KEY (ID_ACUSE));

CREATE TABLE USUARIOS (id_usuario serial NOT NULL,usuario character varying(30) NOT NULL,nombre_completo character varying(255),
  password character varying(60) NOT NULL,tipo character varying(1) NOT NULL,usuario_cfe boolean NOT NULL,usuario_cae boolean NOT NULL,
  usuario_export boolean NOT NULL,usuario_reportes boolean NOT NULL, password_mail character varying(50),servidor_smtp character varying(50),
  servidor_pop character varying(50), puerto_smtp integer,  puerto_pop integer,  requiere_autenticacion boolean,  usuario_mail character varying(50),
  alarma_cfe boolean NOT NULL DEFAULT false,  alarma_cae boolean NOT NULL DEFAULT false,  alarma_admin boolean NOT NULL DEFAULT false,
  alarma_reportes boolean NOT NULL DEFAULT false, activo BOOLEAN NOT NULL, acceso_server BOOLEAN NOT NULL, acceso_pdv BOOLEAN NOT NULL, hashed_password character varying(300), salt character varying(100),
  ver_emitidos_facturasytickets BOOLEAN DEFAULT TRUE, ver_emitidos_remitos BOOLEAN DEFAULT TRUE, ver_emitidos_resguardos BOOLEAN DEFAULT TRUE,
  ver_emitidos_exportaciones BOOLEAN DEFAULT TRUE, ver_recibidos_facturasytickets BOOLEAN DEFAULT TRUE, ver_recibidos_remitos BOOLEAN DEFAULT TRUE, 
  ver_recibidos_resguardos BOOLEAN DEFAULT TRUE, ver_recibidos_exportaciones BOOLEAN DEFAULT TRUE, emitir_facturasytickets BOOLEAN DEFAULT TRUE,
  emitir_remitos BOOLEAN DEFAULT TRUE, emitir_resguardos BOOLEAN DEFAULT TRUE, emitir_exportaciones BOOLEAN DEFAULT TRUE, ver_sucursales VARCHAR(255),
  login_fallidos INTEGER DEFAULT 0, ultimo_login TIMESTAMP, CONSTRAINT usuarios_pkey PRIMARY KEY (id_usuario ), CONSTRAINT usuarios_usuario_key UNIQUE (usuario ));

CREATE TABLE REPORTES_DIARIOS (ID_REPORTE_DIARIO  BIGSERIAL NOT NULL, XML OID, FECHA timestamp NOT NULL, ID_SECUENCIA int4 NOT NULL, FECHA_TRANSMISION timestamp, FECHA_VALIDACION timestamp, ID_USUARIO int4, ID_DETALLE_ACUSE int8, REQUIERE_REGENERAR timestamp, MOTIVO_REGENERACION varchar(255), ULTIMO_INTENTO_ENVIO DATE, INTENTOS_ENVIO INT8, PRIMARY KEY (ID_REPORTE_DIARIO));
CREATE TABLE RANGOS_COMPROBANTES_ANULADOS (ID_RANGO_COMPROBANTES_ANULADOS  BIGSERIAL NOT NULL, DESDE int8 NOT NULL, HASTA int8 NOT NULL, FECHA timestamp, MOTIVO varchar(255), ID_CAE_ASIGNADO int8 NOT NULL, PRIMARY KEY (ID_RANGO_COMPROBANTES_ANULADOS));
CREATE TABLE MENSAJES (ID_MENSAJE  BIGSERIAL NOT NULL, ASUNTO varchar(255) NOT NULL, MENSAJE text NOT NULL, FECHA_CREACION timestamp NOT NULL, FECHA_ENVIO timestamp, ENVIADO bool NOT NULL, SERVER_STATUS varchar(255), REINTENTO_ENVIO int4 NOT NULL, PRIMARY KEY (ID_MENSAJE));
CREATE TABLE NUMEROS_ASIGNADOS (ID_NUMERO_ASIGNADO  BIGSERIAL NOT NULL, NUMERO int4 NOT NULL, ID_CAE_ASIGNADO int8 NOT NULL, ID_COMPROBANTE int8, PRIMARY KEY (ID_NUMERO_ASIGNADO));
CREATE TABLE RECEPTORES_ELECTRONICOS (RUT_RECEPTOR  VARCHAR(15) NOT NULL, NOMBRE VARCHAR(255) NOT NULL, FECHA_DESDE date, FECHA_HASTA date, MAIL_ENVIO varchar(255), MAIL_CONTACTO varchar(255), PRIMARY KEY (RUT_RECEPTOR));
CREATE TABLE DETALLES_ACUSES (ID_DETALLE_ACUSE  BIGSERIAL NOT NULL, ID_ACUSE int8 NOT NULL, ESTADO varchar(255) NOT NULL, CODIGO varchar(255), MOTIVO varchar(255), detalle character varying(2000), PRIMARY KEY (ID_DETALLE_ACUSE));
CREATE TABLE LOTES(ID_LOTE BIGINT NOT NULL, TOTAL_EN_LOTE BIGINT, CONTADOR_EN_LOTE BIGINT, LOTE_COMPLETO BOOLEAN NOT NULL DEFAULT FALSE, LOTE_IMPRESO BOOLEAN NOT NULL DEFAULT FALSE, CONSTRAINT LOTES_PK PRIMARY KEY (ID_LOTE)) WITH (OIDS=FALSE);
CREATE TABLE ALARMAS(id bigserial NOT NULL,codigo character varying(255), descripcion character varying(2000), fecha_alarma timestamp without time zone, fecha_envio timestamp without time zone, id_especifico character varying(300), email_receptores character varying(500), id_receptor_cae BIGINT, CONSTRAINT pk PRIMARY KEY (id ));
CREATE TABLE reportes_diarios_pedidos(fecha timestamp without time zone NOT NULL,fecha_completado timestamp without time zone,id_usuario integer,id_pedido serial NOT NULL,CONSTRAINT pedidos_pk PRIMARY KEY (id_pedido ),CONSTRAINT usuarios_fk FOREIGN KEY (id_usuario)REFERENCES usuarios (id_usuario) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);
CREATE TABLE logs(id_log serial NOT NULL,submodulo character varying(200),  id_usuario integer,  fecha timestamp without time zone,  descripcion character varying(2000),  valor_anterior character varying(200),valor_actual character varying(200),CONSTRAINT lok_pk PRIMARY KEY (id_log ),CONSTRAINT usuarios_fk FOREIGN KEY (id_usuario)REFERENCES usuarios (id_usuario) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);
CREATE TABLE reportes_diarios_envio_pedidos(id_pedido serial, fecha timestamp without time zone, secuencia integer, id_usuario character varying, CONSTRAINT pk_pedido_envio PRIMARY KEY (id_pedido )); 
CREATE TABLE emisores_ctrl_rd(id_emisor serial, emisor character varying(255), CONSTRAINT pk_emisor PRIMARY KEY (id_emisor )); 
CREATE TABLE percepciones_retenciones(frm int4, cod int4, descripcion VARCHAR(250), fecha_baja timestamp without time zone, CONSTRAINT pk_per_ret PRIMARY KEY (frm, cod));
CREATE TABLE tipo_alarmas(id_codigo character varying(100) ,habilitado boolean not null, CONSTRAINT pk_tipo_alarma PRIMARY KEY (id_codigo));
CREATE TABLE VALORES_UI(ANIO INT NOT NULL, CANTIDAD INT NOT NULL, VALOR NUMERIC(8,5) NOT NULL, PRIMARY KEY (ANIO));
CREATE TABLE CLIENTES (IDENTIFICADOR  VARCHAR(20) NOT NULL, DOCUMENTO  VARCHAR(20), NOMBRE VARCHAR(255), TIPO_DOCUMENTO INTEGER, CODIGO_PAIS_DOCUMENTO VARCHAR(2), DIRECCION VARCHAR(70), CIUDAD VARCHAR(30), DEPARTAMENTO VARCHAR(30), CODIGO_POSTAL VARCHAR(10), MAIL VARCHAR(255), ACTIVO boolean, ID_CONDICION_PAGO BIGINT, ID_CONDICION_ENVIO BIGINT, ID_LISTA_PRECIOS BIGINT, PRIMARY KEY (IDENTIFICADOR));
CREATE TABLE SUCURSALES (CODIGO int4, NOMBRE VARCHAR(70), DOMICILIO_FISCAL VARCHAR(70) NOT NULL, FECHA_ALTA date, FECHA_BAJA date,  PRINCIPAL boolean, SUCURSAL_DATAENTRY boolean, ciudad character varying(30), departamento character varying(30), PRIMARY KEY (CODIGO));
CREATE TABLE productos_servicios(id_producto bigserial NOT NULL, codigo_interno character varying(50), codigo_interno2 character varying(50), codigo_ean bigint, nombre character varying(80) NOT NULL, descripcion_adicional character varying(1000), unidad_medida character varying(10) NOT NULL, tipo_iva integer NOT NULL, fecha_baja date, CONSTRAINT productos_servicios_pkey PRIMARY KEY (id_producto));
CREATE TABLE listas_precios(id_lista bigserial, nombre character varying(100), valida_desde date, valida_hasta date, fecha_baja date, PRIMARY KEY (id_lista));
CREATE TABLE precios_productos(id_precio bigserial NOT NULL,id_producto bigint, id_lista bigint, valor numeric, CONSTRAINT precio_producto_pkey PRIMARY KEY (id_precio), CONSTRAINT precio_producto_id_lista_fkey FOREIGN KEY (id_lista) REFERENCES listas_precios (id_lista) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT precio_producto_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES productos_servicios (id_producto) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);
CREATE TABLE condiciones_envio(id_condicion_envio bigserial NOT NULL,codigo character varying(50),descripcion character varying(400) NOT NULL,fecha_baja date,CONSTRAINT condiciones_envio_pkey PRIMARY KEY (id_condicion_envio));
CREATE TABLE condiciones_pago(id_condicion_pago bigserial NOT NULL,codigo character varying(50),descripcion character varying(400) NOT NULL,fecha_baja date,CONSTRAINT condiciones_pago_pkey PRIMARY KEY (id_condicion_pago));
CREATE TABLE paquetes_mensuales(id_paquete bigserial,fecha timestamp without time zone, cant_descargas_por_tiempo integer, fecha_primer_descarga timestamp without time zone,fecha_ultima_descarga timestamp without time zone,fecha_ultimo_cfe timestamp without time zone,fecha_ultima_generacion timestamp without time zone,fecha_eliminacion timestamp without time zone,CONSTRAINT paquetes_mensuales_pkey PRIMARY KEY (id_paquete));
CREATE TABLE stocks_minimos_receptor(id_receptor_cae integer, id_tipo_comprobante integer, stock_minimo integer, PRIMARY KEY (id_receptor_cae, id_tipo_comprobante));

ALTER TABLE stocks_minimos_receptor ADD FOREIGN KEY (id_tipo_comprobante) REFERENCES tipos_comprobante (id_tipo_comprobante) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE stocks_minimos_receptor ADD FOREIGN KEY (id_receptor_cae) REFERENCES receptores_cae (id_receptor_cae) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE CLIENTES ADD FOREIGN KEY (ID_CONDICION_PAGO) REFERENCES CONDICIONES_PAGO(ID_CONDICION_PAGO);
ALTER TABLE CLIENTES ADD FOREIGN KEY (ID_CONDICION_ENVIO) REFERENCES CONDICIONES_ENVIO(ID_CONDICION_ENVIO);
ALTER TABLE CLIENTES ADD FOREIGN KEY (ID_LISTA_PRECIOS) REFERENCES LISTAS_PRECIOS(ID_LISTA);
ALTER TABLE tipos_comprobante ADD FOREIGN KEY (id_impresora_recibidos) REFERENCES impresoras (id_impresora);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT556554 FOREIGN KEY (ID_TIPO_COMPROBANTE) REFERENCES TIPOS_COMPROBANTE (ID_TIPO_COMPROBANTE);
ALTER TABLE CAES_ASIGNADOS ADD CONSTRAINT FKCAES_ASIGN159792 FOREIGN KEY (ID_RECEPTOR_CAE) REFERENCES RECEPTORES_CAE (ID_RECEPTOR_CAE);
ALTER TABLE CAES_ASIGNADOS ADD CONSTRAINT FKCAES_ASIGN9707 FOREIGN KEY (ID_CAE) REFERENCES CAES (ID_CAE);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT805441 FOREIGN KEY (ID_SOBRE_RE) REFERENCES SOBRES (ID_SOBRE);
ALTER TABLE CAES_ASIGNADOS ADD CONSTRAINT FKCAES_ASIGN944501 FOREIGN KEY (ID_USUARIO) REFERENCES USUARIOS (ID_USUARIO);
ALTER TABLE SOBRES ADD CONSTRAINT FKSOBRES390741 FOREIGN KEY (ID_DETALLE_ACUSE) REFERENCES DETALLES_ACUSES (ID_DETALLE_ACUSE);
ALTER TABLE TIPOS_COMPROBANTE ADD CONSTRAINT FKTIPOS_COMP769672 FOREIGN KEY (id_impresora_emitidos) REFERENCES IMPRESORAS (ID_IMPRESORA);
ALTER TABLE CAES ADD CONSTRAINT FKCAES504300 FOREIGN KEY (TIPO_COMPROBANTE) REFERENCES TIPOS_COMPROBANTE (ID_TIPO_COMPROBANTE);
ALTER TABLE RANGOS_COMPROBANTES_ANULADOS ADD CONSTRAINT FKRANGOS_COM988471 FOREIGN KEY (ID_CAE_ASIGNADO) REFERENCES CAES_ASIGNADOS (ID_CAE_ASIGNADO);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT808002 FOREIGN KEY (ID_CAE_ASIGNADO) REFERENCES CAES_ASIGNADOS (ID_CAE_ASIGNADO);
ALTER TABLE NUMEROS_ASIGNADOS ADD CONSTRAINT FKNUMEROS_AS769882 FOREIGN KEY (ID_CAE_ASIGNADO) REFERENCES CAES_ASIGNADOS (ID_CAE_ASIGNADO);
ALTER TABLE NUMEROS_ASIGNADOS ADD CONSTRAINT FKNUMEROS_AS106864 FOREIGN KEY (ID_COMPROBANTE) REFERENCES COMPROBANTES (ID_COMPROBANTE);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT700449 FOREIGN KEY (ID_SOBRE_DGI) REFERENCES SOBRES (ID_SOBRE);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT957367 FOREIGN KEY (ID_REPORTE_DIARIO) REFERENCES REPORTES_DIARIOS (ID_REPORTE_DIARIO);
ALTER TABLE DETALLES_ACUSES ADD CONSTRAINT FKDETALLES_A203902 FOREIGN KEY (ID_ACUSE) REFERENCES ACUSES (ID_ACUSE);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT613143 FOREIGN KEY (ID_DETALLE_ACUSE_RE) REFERENCES DETALLES_ACUSES (ID_DETALLE_ACUSE);
ALTER TABLE COMPROBANTES ADD CONSTRAINT FKCOMPROBANT235105 FOREIGN KEY (ID_DETALLE_ACUSE_DGI) REFERENCES DETALLES_ACUSES (ID_DETALLE_ACUSE);
ALTER TABLE REPORTES_DIARIOS ADD CONSTRAINT FKREPORTES_D259478 FOREIGN KEY (ID_DETALLE_ACUSE) REFERENCES DETALLES_ACUSES (ID_DETALLE_ACUSE);
ALTER TABLE REPORTES_DIARIOS ADD CONSTRAINT FKREPORTES_D98688 FOREIGN KEY (ID_USUARIO) REFERENCES USUARIOS (ID_USUARIO);
ALTER TABLE alarmas ADD FOREIGN KEY(id_receptor_cae) REFERENCES receptores_cae;

CREATE VIEW cfe_consulta AS 
 SELECT c.xml, c.serie, c.rut_emisor, c.emitido_recibido, c.nro_documento, c.serie_interno, c.nro_interno, c.id_tipo_comprobante, cs.id_cae, c.pdf, cs.desde AS cae_desde, cs.hasta AS cae_hasta, cs.vencimiento AS cae_vto, da1.estado AS estado_dgi, da1.motivo AS motivo_dgi, da2.estado AS estado_re, da2.motivo AS motivo_re, c.ts_firma, c.digest_firma
   FROM comprobantes c
   LEFT JOIN detalles_acuses da1 ON c.id_detalle_acuse_dgi = da1.id_detalle_acuse
   LEFT JOIN detalles_acuses da2 ON c.id_detalle_acuse_re = da2.id_detalle_acuse
   LEFT JOIN acuses a ON da1.id_acuse = a.id_acuse
   LEFT JOIN caes_asignados ca ON ca.id_cae_asignado = c.id_cae_asignado
   LEFT JOIN caes cs ON cs.id_cae = ca.id_cae;

GRANT ALL ON TABLE cfe_consulta TO postgres;
GRANT ALL ON TABLE cfe_consulta TO cfe_consulta;

GRANT ALL ON ALL TABLES IN SCHEMA public TO RNLOCAL;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO RNLOCAL;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO rn_backup;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA public TO rn_backup;

GRANT ALL ON PUBLIC.CFE_CONSULTA TO CFE_CONSULTA;

INSERT INTO TIPOS_COMPROBANTE VALUES(101,'e-Ticket',50000,0,'Ticket.jasper', NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintTicket',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(102,'NC e-Ticket',5000,0,'Ticket.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintTicket',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(103,'ND e-Ticket',5000,0,'Ticket.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintTicket',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(111,'e-Factura',50000,0,'Bill.jasper',NULL, NULL, FALSE, TRUE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(112,'NC e-Factura',5000,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(113,'ND e-Factura',5000,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(181,'e-Remito',0,0,'',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintGuard',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(182,'e-Resguardo',0,0,'Guard.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintGuard',1,1,false);
	 
INSERT INTO TIPOS_COMPROBANTE VALUES(201,'e-Ticket CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(202,'NC e-Ticket CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(203,'ND e-Ticket CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(211,'e-Factura CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(212,'NC e-Factura CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(213,'ND e-Factura CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(281,'e-Remito CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(282,'e-Resguardo CONTINGENCIA',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);

INSERT INTO TIPOS_COMPROBANTE VALUES(121,'e-Factura Exportación',0,0,'Bill.jasper',NULL, NULL, FALSE, TRUE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(122,'NC e-Factura Exportación',0,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(123,'ND e-Factura Exportación',0,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintBill',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(124,'e-Remito Exportación',0,0,'',NULL, NULL, FALSE, FALSE, 'org.gs1uy.rondanetweb.print.services.PrintGuard',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(221,'e-Factura Exportación Contingencia',0,0,'Bill.jasper',NULL, NULL, FALSE, TRUE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(222,'NC e-Factura Exportación Contingencia',0,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(223,'ND e-Factura Exportación Contingencia',0,0,'Bill.jasper',NULL, NULL, FALSE, FALSE, '',1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(224,'e-Remito Exportación Contingencia',0,0,'',NULL, NULL, FALSE, FALSE, '',1,1,false);

INSERT INTO TIPOS_COMPROBANTE VALUES(131,'e-Ticket Venta por Cuenta Ajena',0,0,NULL, NULL, NULL, FALSE, FALSE, NULL,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(132,'Nota de Crédito de e-Ticket Venta por Cuenta Ajena',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(133,'Nota de Débito de e-Ticket Venta por Cuenta Ajena',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(141,'e-Factura Venta por Cuenta Ajena',0,0,NULL,NULL, NULL, FALSE, TRUE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(142,'Nota de Crédito de e-Factura Venta por Cuenta Ajena',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(143,'Nota de Débito de e-Factura Venta por Cuenta Ajena',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);

INSERT INTO TIPOS_COMPROBANTE VALUES(231,'e-Ticket Venta por Cuenta Ajena Contingencia',0,0,NULL, NULL, NULL, FALSE, FALSE, NULL,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(232,'Nota de Crédito de e-Ticket Venta por Cuenta Ajena Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(233,'Nota de Débito de e-Ticket Venta por Cuenta Ajena Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(241,'e-Factura Venta por Cuenta Ajena Contingencia',0,0,NULL,NULL, NULL, FALSE, TRUE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(242,'Nota de Crédito de e-Factura Venta por Cuenta Ajena Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(243,'Nota de Débito de e-Factura Venta por Cuenta Ajena Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE(
            id_tipo_comprobante, descripcion, stock_minimo_comprobantes, 
            reposicion_comprobantes, template_impresion, id_impresora_emitidos, 
            id_impresora_recibidos, numerar, firmar, class_impresion, copias_impresion_emitidos, 
            copias_impresion_recibidos, enviar_siempre)
    VALUES (900, 'Recibo', 0, 
            0, '', null, 
            null, false, false, '', 1, 
            0, false);

INSERT INTO TIPOS_COMPROBANTE VALUES(151,'e-Boleta de entrada',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(152,'Nota de Crédito de e-Boleta de entrada',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(153,'Nota de Débito de e-Boleta de entrada',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(251,'e-Boleta de entrada Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(252,'Nota de Crédito de e-Boleta de entrada Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
INSERT INTO TIPOS_COMPROBANTE VALUES(253,'Nota de Débito de e-Boleta de entrada Contingencia',0,0,NULL,NULL, NULL, FALSE, FALSE, NULL ,1,1,false);
            

-- clave, descripci�n, string, integer, bool, date, pass
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_ENVIO_SOBRES_CFE_LP','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/Envio',NULL,NULL,NULL, NULL, NULL);	
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_ENVIO_SOBRES_CFE_HP','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/Envio',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_ENVIO_ACUSES','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/Envio',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_SOBRES','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarSobres',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_ACUSES','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarRespuestas',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_ENVIO_CONFIRMACION_ACUSES','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/ConfRecepcionResp',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_USUARIOS','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarUsuarios',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_RECHAZOS_DGI','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarRechazosDGI',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_CONF_RECHAZOS_DGI','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/ConfRecepcionRech',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_RECEPCION_RECEPTORES_ELECTRONICOS','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarEmpresas',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_ENVIO_ERRORES',NULL,'https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/Error',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('URL_RNCENTRAL_VALORES_UI','URL para descarga de valores de UI','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/BajarValoresUI',NULL, NULL,NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES('URL_RNCENTRAL_MONITOREO','','https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/EnvioInforme',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('URL_WS_DGI','','https://efactura.dgi.gub.uy:6443/ePrueba/ws_eprueba',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('LOW_PRIORITY_FOLDER','Carpeta para tickets de baja prioridad','AEnviarTKMenores',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('HIGH_PRIORITY_FOLDER','Carpeta para documentos de alta prioridad','AEnviar',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('NOT_SENT_FOLDER','Carpeta de archivos no enviados','NoEnviados',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('SENT_FOLDER','Carpeta de archivos enviados','Enviados',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('RECEIVED_FOLDER','Carpeta de archivos recibidos','Recibidos',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('SOLICITUD_RANGO_CAE','Carpeta de solicitudes de Rango de CAE','RangoCAEPedidos',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('SOLICITUD_RANGO_NO_ASIGNADO','Carpeta de solicitudes de Rango de CAE no Asignados','RangoCAENoAsignados',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('ASIGNACION_RANGO_CAE','Carpeta de asignaciones de Rango de CAE','RangoCAEEntregados',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('PRINT_OUTPUT_FOLDER','Carpeta de CFE impresos','Impresion',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('PRINT_TEMPLATES_FOLDER','Carpeta de Templates de impresion','PrintResources',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('LOG_FOLDER','Carpeta de Logs del sistema','Logs',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TEMP_FOLDER','Carpeta de archivos temporales','Temp',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('BACKUP_FOLDER','Carpeta de backup','Respaldos',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('CONTROL_FOLDER','Carpeta de control','Control',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('CANTIDAD_CFES_SOBRE','Cantidad de CFE por sobre',NULL,200,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('MAX_SIZE_SOBRE_DGI',NULL,NULL,1887436,NULL,NULL,NULL,NULL);

INSERT INTO PARAMETROS VALUES('RUT_DGI','RUT de la DGI','219999830019',NULL,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('TIEMPO_PROCESAMIENTO_CFE_HP','Tiempo de procesamiento de comprobantes HP',NULL,1,NULL,NULL, NULL,NULL); 
INSERT INTO PARAMETROS VALUES('TIEMPO_PROCESAMIENTO_CFE_LP','Tiempo de procesamiento de comprobantes LP',NULL,5,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENSOBRADO_CFE_HP','Tiempo de ensobrado de comprobantes HP',NULL,30,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENSOBRADO_CFE_LP','Tiempo de ensobrado de comprobantes LP',NULL,60,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENVIO_CFE_HP','',NULL,30,NULL,NULL, NULL,NULL); 
INSERT INTO PARAMETROS VALUES('TIEMPO_ENVIO_CFE_LP','',NULL,60,NULL,NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES('TIEMPO_RECEPCION_CFE','',NULL,180,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENVIO_ACUSES','',NULL,900,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_RECEPCION_ACUSES','',NULL,180,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_SOLICITUD_RANGO_CAE','',NULL,10,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENVIO_ALARMAS','',NULL,30,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_GUARDAR_TABLA_CORRESPONDENCIA',NULL,NULL,60,NULL,NULL,NULL,NULL);

INSERT INTO PARAMETROS VALUES('TIEMPO_PROCESO_DEPURACION',NULL,NULL,43200,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_ACK_INTERNOS',NULL,NULL,864000,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_NO_ENVIADOS',NULL,NULL,0,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_ALARMAS',NULL,NULL,2592000,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_LOGS_USUARIO',NULL,NULL,186624000,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_LOGS',NULL,NULL,2592000,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_CFE_ENVIADOS','',NULL,2592000,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_DEPURACION_CFE_RECIBIDOS','',NULL,2592000,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_PROCESO_RECEPCION_MAILS', '', NULL, 180, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('TIEMPO_PROCESO_GENERAR_CORRESP','', NULL, 10, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('RAZON_SOCIAL', null, '', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('CIUDAD', null, 'Montevideo', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('DEPARTAMENTO', null, 'Montevideo', NULL, NULL, NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES ('CAEs_TO_CREATE_FOLDER', 'Carpeta para CAEs a cargar', 'CAEsACargar', NULL, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('CAEs_CREATED_FOLDER', 'Carpeta para CAEs creados', 'CAEsCargados', null, null, null, null,NULL);
INSERT INTO PARAMETROS VALUES ('CAEs_NOT_CREATED_FOLDER', 'Carpeta para CAES que no se pudieron crear', 'CAEsNoCargados', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('DAYS_SEND_ALERTS', 'Dias faltantes para el vencimiento del certificado en los que se manda alarma', '60,45,30', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('VALIDATE_CRETIFICATES', 'Validar fecha de vencimiento de certificados', NULL, NULL, TRUE, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('SEND_MAIL_TIME', 'Momento del dia en que se revisa la fecha de vencimiento de los certificados', NULL, NULL, NULL,'1970-01-01 12:00:00', NULL,NULL);

INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_USER', '', '', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_PASSWORD', '', NULL, NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_HOST', '', '', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_PUERTO', '', NULL, 25, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_AUTH', '', NULL, NULL, TRUE, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_TLS', '', NULL, NULL, FALSE, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_SSL', '', NULL, NULL, FALSE, NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_PROTOCOL', 'POP3 O IMAP', 'POP3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_INBOX', '', 'INBOX', NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_USER', '', '', NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_PASSWORD', '', NULL, NULL, NULL, NULL, '',NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_HOST', '', '', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_PUERTO', '', NULL, 110, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_AUTH', '', NULL, NULL, TRUE, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_ACCESS_SSL', '', NULL, NULL, FALSE, NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES ('SOCKET_CONNECTION_TIMEOUT', '', NULL, 30000, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('SOCKET_DATA_TIMEOUT', '', NULL, 30000 , NULL, NULL, NULL,NULL);  

INSERT INTO PARAMETROS VALUES ('TIMEOUT_WEBSERVICE_DGI', '', NULL, 15000, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('GENERATE_PDF_FILE_RECIBIDOS','',NULL,NULL, false,NULL, NULL);
INSERT INTO PARAMETROS VALUES ('WS_SEGUROS','Deshabilita los Web Services antiguos',NULL,NULL,true,NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('LICENCIA','',NULL,NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('QG2w0b2G9TkkITznj6a+MteGwbHWLrHb','',NULL,NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('TELEFONO1_EMISOR','','',NULL,NULL,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('TELEFONO2_EMISOR','','',NULL,NULL,NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('HABILITAR_RECIBOS','',NULL, NULL, FALSE, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('TOLERANCIA_TOTALES','', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO PARAMETROS VALUES ('VALIDAR_CALCULO_IVA','', NULL, NULL, TRUE, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('TEMPLATE_NOM_CFE_EMITIDO', NULL, '[RUTEMISOR]_[TIPOCFE][SERIE][NRODOC]' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('TEMPLATE_NOM_CFE_RECIBIDO', NULL, '[RUTEMISOR]_[TIPOCFE][SERIE][NRODOC]', NULL, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('VERSION','VERSION DE LA APLICACION', '6.2.1', null, null,null, null, null);

INSERT INTO PARAMETROS VALUES ('CARPETA_ACTUALIZACIONES', '', 'actualizaciones', null, null,null, null, null);

INSERT INTO PARAMETROS VALUES ('CONTROLAR_ENVIO_REPORTE', NULL, NULL , NULL, TRUE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CARPETA_AEDITAR', 'Carpeta para documentos a editar DE', 'AEditar' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CARPETA_BORRADORES', 'Carpeta para borradores DE', 'AEditar/Borradores' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CARPETA_PLANTILLAS', 'Carpeta para plantillas DE', 'AEditar/Plantillas' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('COND_DE_PAGO_DATA_ENTRY_VISIBLE', NULL, NULL , NULL, false, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('COND_DE_ENVIO_DATA_ENTRY_VISIBLE', NULL, NULL , NULL, false, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('URL_RNCENTRAL_ENVIO_STATUS', NULL, 'https://cfe.rondanet.com:5542/cgi-bin/Receptor.cgi/EnvioInforme' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('TIEMPO_ENVIO_STATUS', NULL, NULL , 86400, NULL, NULL, NULL, NULL);

INSERT INTO parametros(id_parametro, descripcion, valor_string, valor_integer, valor_bool, valor_date, valor_password, valor_numerico) VALUES ('ENVIAR_ALARMA_VENCIMIENTO_CAE', 'Habilita el envio de alarmas por vencimiento de CAE', NULL, NULL, true, NULL, NULL, NULL);
INSERT INTO parametros(id_parametro, descripcion, valor_string, valor_integer, valor_bool, valor_date, valor_password, valor_numerico) VALUES ('DIAS_ALERTA_VENCIMIENTO_CAE', 'Lista de 3 dias separados por comas en los cuales se enviará alarma en si un CAE le quedan esa cantidad de dias para vencer.', '60,30,15', NULL, NULL, NULL, NULL, NULL);
INSERT INTO parametros(id_parametro, descripcion, valor_string, valor_integer, valor_bool, valor_date, valor_password, valor_numerico) VALUES ('HORA_ALARMA_CAES', 'Hora de envio de alarmas por vencimiento de CAE', NULL, NULL, NULL, '2017-01-01 12:00:00', NULL, NULL);
INSERT INTO parametros(id_parametro, descripcion, valor_string, valor_integer, valor_bool, valor_date, valor_password, valor_numerico) VALUES ('CTRL_REM_RES_REFERENCIAS', '', NULL, NULL, true, NULL, NULL, NULL);
INSERT INTO parametros(id_parametro, descripcion, valor_string, valor_integer, valor_bool, valor_date, valor_password, valor_numerico) VALUES ('ACCESO_ADMIN_ACTUALIZADOR', '', NULL, NULL, false, NULL, NULL, NULL);

-- RNLocal como receptor de CAE
INSERT INTO RECEPTORES_CAE VALUES(0,'RNLOCAL',true, '');

CREATE VIEW COMPROBANTES_VIEW AS SELECT id_comprobante, xml, pdf, serie, nro_documento, fecha, razon_social_emisor, rut_emisor, razon_social_receptor, 
rut_receptor, ts_firma, ordinal_sobre, fecha_procesado, sucursal, moneda_transaccion, tipo_cambio, total_mnt_no_gravado, 
total_mnt_exportacion, total_mnt_percibido, total_mnt_iva_suspenso, total_mnt_iva_bas, total_mnt_iva_min, total_mnt_iva_otra, 
mnt_iva_min, mnt_iva_bas, mnt_iva_otra, iva_tasa_min, iva_tasa_bas, total_mnt_retenido, total_mnt_total, total_mnt_no_facturable, 
total_mnt_a_pagar, enviar_dgi, enviar_dc, enviar_re, nro_interno, serie_interno, serial_certificado, id_tipo_comprobante, id_sobre_dgi, 
id_sobre_re, id_cae_asignado, id_detalle_acuse_re, id_detalle_acuse_dgi, cant_lin_detalle, emitido_recibido, digest_firma FROM COMPROBANTES;


CREATE VIEW CAES_VIEW AS SELECT id_cae, serie, desde, hasta, vencimiento, fecha_ingreso, fecha_cierre, motivo_anulacion, tipo_comprobante FROM CAES;
 
CREATE VIEW RECEPTORES_CAE_VIEW AS SELECT id_receptor_cae, descripcion FROM RECEPTORES_CAE;

CREATE VIEW CAES_ASIGNADOS_VIEW AS SELECT id_cae_asignado, id_cae, id_receptor_cae, desde, hasta, asignado FROM CAES_ASIGNADOS;
  
CREATE VIEW SOBRES_VIEW AS SELECT id_sobre, xml, nombre_archivo, rut_receptor, rut_emisor, fecha_creacion, id_emisor, fecha_transmision, tiempo_consulta_respuesta, fecha_consulta_respuesta, id_detalle_acuse, emitido_recibido FROM SOBRES;
  
CREATE VIEW ACUSES_VIEW AS SELECT id_acuse, xml, rut_emisor, rut_receptor, id_respuesta, id_emisor, id_receptor, ts_firma, fecha_transmision FROM ACUSES;

CREATE VIEW REPORTES_DIARIOS_VIEW AS SELECT id_reporte_diario, xml, fecha, id_secuencia, fecha_transmision, fecha_validacion, id_detalle_acuse FROM REPORTES_DIARIOS;

CREATE VIEW RECEPTORES_ELECTRONICOS_VIEW AS SELECT rut_receptor, nombre, fecha_desde, fecha_hasta, mail_envio, mail_contacto FROM RECEPTORES_ELECTRONICOS;

CREATE VIEW DETALLES_ACUSES_VIEW AS SELECT id_detalle_acuse, id_acuse, estado, motivo, codigo FROM DETALLES_ACUSES;

CREATE VIEW LOGS_VIEW AS SELECT id_log, submodulo, id_usuario, fecha, descripcion, valor_anterior, valor_actual FROM LOGS;

CREATE VIEW USUARIOS_VIEW AS SELECT id_usuario, usuario, nombre_completo FROM USUARIOS;

CREATE VIEW CLIENTES_VIEW AS SELECT identificador, documento, nombre, activo  FROM CLIENTES;

GRANT ALL ON TABLE CAES_VIEW TO rnlocal;
GRANT ALL ON TABLE CAES_VIEW TO cfe_consulta;

GRANT ALL ON TABLE cfe_consulta TO rnlocal;
GRANT ALL ON TABLE cfe_consulta TO cfe_consulta;

GRANT ALL ON TABLE COMPROBANTES_VIEW TO rnlocal;
GRANT ALL ON TABLE COMPROBANTES_VIEW TO cfe_consulta;

GRANT ALL ON TABLE RECEPTORES_CAE_VIEW TO rnlocal;
GRANT ALL ON TABLE RECEPTORES_CAE_VIEW TO cfe_consulta;

GRANT ALL ON TABLE CAES_ASIGNADOS_VIEW TO rnlocal;
GRANT ALL ON TABLE CAES_ASIGNADOS_VIEW TO cfe_consulta;

GRANT ALL ON TABLE SOBRES_VIEW TO rnlocal;
GRANT ALL ON TABLE SOBRES_VIEW TO cfe_consulta;

GRANT ALL ON TABLE ACUSES_VIEW TO rnlocal;
GRANT ALL ON TABLE ACUSES_VIEW TO cfe_consulta;

GRANT ALL ON TABLE REPORTES_DIARIOS_VIEW TO rnlocal;
GRANT ALL ON TABLE REPORTES_DIARIOS_VIEW TO cfe_consulta;

GRANT ALL ON TABLE RECEPTORES_ELECTRONICOS_VIEW TO rnlocal;
GRANT ALL ON TABLE RECEPTORES_ELECTRONICOS_VIEW TO cfe_consulta;

GRANT ALL ON TABLE DETALLES_ACUSES_VIEW TO rnlocal;
GRANT ALL ON TABLE DETALLES_ACUSES_VIEW TO cfe_consulta;

GRANT ALL ON TABLE LOGS_VIEW TO rnlocal;
GRANT ALL ON TABLE LOGS_VIEW TO cfe_consulta;

GRANT ALL ON TABLE USUARIOS_VIEW TO rnlocal;
GRANT ALL ON TABLE USUARIOS_VIEW TO cfe_consulta;

GRANT ALL ON TABLE CLIENTES_VIEW TO rnlocal;
GRANT ALL ON TABLE CLIENTES_VIEW TO cfe_consulta;

---------------------------------------------------------------------------

INSERT INTO PARAMETROS VALUES('KEYSTORE_FILENAME',NULL,'rn.keystore',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('KEYSTORE_PASSWORD',NULL,NULL,NULL,NULL,NULL, 'XxdysM0Kel1Y6g3VSIGPqldRVWW6X5oC',NULL);
INSERT INTO PARAMETROS VALUES('KEY_NAME',NULL,'myKey',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('KEY_PASSWORD',NULL,NULL,NULL,NULL,NULL, 'XxdysM0Kel1Y6g3VSIGPqldRVWW6X5oC',NULL);
INSERT INTO PARAMETROS VALUES('CRYPTOGRAPHIC_CONFIGURATION',NULL,NULL,NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('CRYPTO_KEYSTORE_PROVIDER',NULL,NULL,NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('CRYPTO_KEYSTORE_TYPE',NULL,'JKS',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('NOMBRE_EMPRESA','Nombre de la empresa','Empresa',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('RUT_EMISOR','Rut del cliente',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('PASSWORD_RNC','Password del cliente para autenticar con RN Central',NULL,NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('LOGO','Logo RN Local','logo.jpg',NULL,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('GENERATE_PDF_BASE', NULL, NULL, NULL, false, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('GENERATE_PDF_FILE','',NULL,NULL,false,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('DIAS_ENVIO_REPORTE_DIARIO',NULL,'1,2,3,4,5,6,7',NULL,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('DIAS_ENVIO_ALARMA_REPORTE_DIARIO',NULL,'1,2,3,4,5,6,7',NULL,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('ENVIAR_ALARMA_REPORTE_DIARIO',NULL,NULL,NULL,TRUE,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('ENVIAR_REPORTE_DIARIO_AUTOMATICO',NULL,NULL,NULL,FALSE,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('HORA_ALARMA_REPORTE_DIARIO',NULL,NULL,NULL,NULL,'1970-01-01 16:00:00',NULL,NULL);
INSERT INTO PARAMETROS VALUES('HORA_ENVIO_REPORTE_DIARIO',NULL,NULL,NULL,NULL,'1970-01-01 17:00:00',NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_GENERADOR_REPORTES_DIARIOS',NULL,NULL,2,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ENVIO_REPORTES_DIARIOS',NULL,NULL,2,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES ('GUARDAR_REPORTE_DIARIO','Indica si se debe guardar una copia de los reportes generados automaticamente en un archivo de la carpeta de control',NULL,NULL,true,NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('HABILITAR_ALARMAS',NULL,NULL,NULL,true,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('HABILITAR_CFE_ENTRY',NULL,NULL,NULL,TRUE,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('VALIDAR_TOTALES',NULL,NULL,NULL,true,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('GUARDAR_ACK_INTERNOS',NULL,NULL,NULL,false,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES('MOSTRAR_NUMERACION_INTERNA',NULL,NULL,NULL,TRUE,NULL,NULL,NULL);

INSERT INTO PARAMETROS VALUES('HOME_APP_FOLDER', null, '', NULL, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES('HOME_FOLDER', null, '', NULL, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES('MODO_CERTIFICACION',NULL,NULL,NULL,FALSE,NULL,NULL,NULL);

INSERT INTO PARAMETROS VALUES('CFEADENDA_SCHEMA_VALIDATION_FILE', NULL,'xsd/1.37_3/CFEEmpresas.xsd', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('SOBRE_EMPRESAS_SCHEMA_VALIDATION_FILE','','xsd/1.37_3/EnvioCFE_entreEmpresas.xsd',NULL, NULL,NULL, NULL);


INSERT INTO PARAMETROS VALUES('MODO_IMPRESION_LOTES',NULL,'',0,NULL,NULL,NULL,NULL);
INSERT INTO PARAMETROS VALUES ('TIPO_APLICACION', 'MIXTA,INHOUSE,SAAS,HOSTEADA', 'MIXTA', NULL, NULL, NULL, NULL,NULL);  

INSERT INTO PARAMETROS VALUES ('REVISAR_STOCKS', '', NULL, NULL, TRUE, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('REVISAR_STOCKS_CRON_SCHEDULE', '', '12 00 * * *', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('GENERAR_LISTA_CORRESPONDENCIA_NUMEROS', 'Indica si se genera la lista de correspondencia de numeración', '', NULL, NULL, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('IMPRIMIR_CFE_ANTES_ENVIAR_DGI', 'Indica si los comprobanteas se imprimen antes de ser enviados a DGI', NULL, NULL, TRUE, NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES ('AGREGAR_CAE_TAG_CFC', 'Indica si se debe agregar el TAG de CAE a los comprobantes', NULL, NULL, FALSE, NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES ('AGREGAR_CAE_TAG_CFE', 'Indica si se debe agregar el TAG de CAE a los CFC', NULL, NULL, FALSE, NULL, NULL,NULL); 
INSERT INTO PARAMETROS VALUES ('CANTIDAD_RESULTADOS_BUSQUEDA', 'Indica la cantidad máxima de resultados para obtener en una búsqueda', NULL, 300, NULL, NULL, NULL,NULL);

INSERT INTO PARAMETROS VALUES ('PROXY_HOST', '', '', NULL, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('PROXY_PORT', '', NULL, 8080, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('PROXY_USER', '', '', NULL, NULL, NULL, NULL,NULL);  
INSERT INTO PARAMETROS VALUES ('PROXY_PASS', '', NULL, NULL , NULL, NULL, '',NULL);  
INSERT INTO PARAMETROS VALUES ('PROXY_DOMAIN','',NULL,NULL,NULL,NULL, NULL,NULL); 

INSERT INTO PARAMETROS VALUES ('URL_DGI_PUBLICACION_WEB', 'URL para la verificación de facturas y tickets mayores en la DGI', 'www.efactura.dgi.gub.uy', NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('URL_EMPRESA_PUBLICACION_WEB', 'URL para la verificación de tickets', 'cfe.rondanet.com', NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('ALARMAS_CANTIDAD_PROX_BASE','', NULL, 5, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('ALARMAS_PROXIMO_ENVIO_MULT_SUMA','', NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO PARAMETROS VALUES ('ALARMAS_PROXIMO_ENVIO_MULT_RESTA','', NULL, NULL, NULL, NULL, NULL, 0.9);
INSERT INTO PARAMETROS VALUES ('ALARMAS_MAX_TIEMPO_SIN_ENVIAR','', NULL, 30, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('ALARMAS_TIEMPO_DEPURACION_HASH','', NULL, 60, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('INTENTOS_LECTURA_CFE','', NULL, 10, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('HABILITAR_DEBUG_MAIL','', NULL, NULL, false, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('TIEMPO_PUBLICACION_WEB','', NULL, 0, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('MAX_CFE_PROCESADOS_HP','', NULL, 200, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('MAX_CFE_PROCESADOS_LP','', NULL, 200, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('MAIL_SMTP_FROM','', '', NULL, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('MOSTRAR_AVISO_CFE_RONDANET_EN_FORMATOS','Muestra aviso CFE Rondanet en formatos gráficos estándar (2.2 en adelante)','',NULL, true,NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('RESPUESTA_WS_STATUS_NO_CORRESPONDE_ENVIAR_DGI','Indica el valor de la respuesta del web service de consulta de estado de comprobantes para tickets menores','', NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('FREE_SHOP','Indica si la empresa es un Free Shop',NULL, NULL, FALSE, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('FECHA_PUBLICACION_XSD', NULL, NULL, NULL, NULL, '2015-10-01 00:00:00', NULL, NULL);

INSERT INTO PARAMETROS VALUES ('IMPRESION_INDIVIDUAL', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('HABILITAR_UTILES_ADMIN', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('CTRL_SUMAS_DE_ITEMS', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CTRL_RET_PER_CREDFISC', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CTRL_SUMA_NOFACTURABLES', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CTRL_PROD_SERVICIOS', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CTRL_SECUENCIAS', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('CTRL_COMP_FISCAL', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('VALIDAR_NIE', NULL, NULL , NULL, FALSE, NULL, NULL, NULL);
 
INSERT INTO PARAMETROS VALUES ('TIEMPO_GENERACION_ACUSES', NULL, NULL , 180, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('ACTUALIZACION_HOST_FTPS', NULL, 'www.gs1uy.org' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('ACTUALIZACION_USER_FTPS', NULL, 'cfeactualizador@gs1uy.org' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('ACTUALIZACION_PASS_FTPS', NULL, NULL , NULL, NULL, NULL, '1vTNudbJ5Y0Bbs01kqprUC0GvK3Po3Ll', NULL);

INSERT INTO PARAMETROS VALUES ('INFO_ADICIONAL_RECEP_DATA_ENTRY_VISIBLE', NULL, NULL , NULL, false, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('INFO_ADICIONAL_EMISOR_DATA_ENTRY_VISIBLE', NULL, NULL , NULL, false, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES ('INFO_ADICIONAL_EMISOR_DATA_ENTRY_DEFECTO', NULL, '' , NULL, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('MODO_CONTROL_RECIBIDOS', 'COMPLETO o PARCIAL', 'PARCIAL' , NULL, NULL, NULL, NULL, NULL);

INSERT INTO PARAMETROS VALUES ('URL_WS_CONSULTA_DGI', 'URL de Web services de consulta de DGI', 'https://efactura.dgi.gub.uy:6460/ePrueba/ws_consultasPrueba' , NULL, NULL, NULL, NULL, NULL);
INSERT INTO PARAMETROS VALUES('MESES_ALMACENAMIENTO_SIN_COSTO','Especifica la cantidad de meses de almacenamiento sin costo.',NULL,0,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TIEMPO_ESPERA_PROCESO_PAQUETES_MENSUALES','Delay en milisegundos entre consultas a la base',NULL,5000,NULL,NULL, NULL,NULL);
INSERT INTO PARAMETROS VALUES('TAMANO_BLOQUE_PROCESO_PAQUETES_MENSUALES','Tamaño de bloque de procesamiento ',NULL,100,NULL,NULL, NULL,NULL);

INSERT INTO parametros VALUES ('SERVICIOS_EXTERNOS_END_POINT', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO parametros VALUES ('SERVICIOS_EXTERNOS_USUARIO', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO parametros VALUES ('SERVICIOS_EXTERNOS_PASSWORD', '', NULL, NULL, NULL, NULL, '', NULL);

INSERT INTO parametros VALUES ('MAX_INTENTOS_LOGIN_FALLIDO', '', NULL, 6, NULL, NULL, NULL, NULL);
INSERT INTO parametros VALUES ('MINUTOS_BLOQUEO_LOGIN', 'Tiempo en minutos', NULL, 3, NULL, NULL, NULL, NULL);

INSERT INTO parametros VALUES ('MAIL_CLIENTE_ASUNTO', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO parametros VALUES ('MAIL_CLIENTE_CUERPO', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO parametros VALUES ('MAIL_CLIENTE_IS_ACTIVAR', '', NULL, NULL, false, NULL, NULL, NULL);

INSERT INTO parametros VALUES ('DATA_ENTRY_LAST_IS_CREDITO', 'recuerda el ultimo tipo de pago del data entry', NULL, NULL, false, NULL, NULL, NULL);


UPDATE PARAMETROS set valor_string = (SELECT valor_string FROM PARAMETROS WHERE id_parametro='MAIL_SMTP_USER') WHERE id_parametro = 'MAIL_SMTP_FROM';


INSERT INTO percepciones_retenciones VALUES (1246,56,'RET. S/ INTERESES – BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,156,'RET. S/ INTERESES – BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,168,'RET. S/DER FEDE, IMAG, SIM DE DEP.– BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,169,'RET. S/GANAN. DE CAPITAL MOBILIA.– BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,177,'RET. S/ARRENDAMIENTOS - BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,178,'RET. S/ARREND REALIZADA POR OTRO AGENTE - BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,179,'RET. S/ARRENDAMIENTOS A TURISTAS - BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,184,'RET. S/INCREMENTOS PATRIMONIALES - BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,194,'RET. S/ RENTAS DE ACT. EMPRESAR.– BONT',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,440,'IMPUTACION DE DIVIDENDOS Y UTILID. FICTOS A CONTRIB. DE IRAE',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,171,'OTT - APPS TRANSPORTE (Nº 3º RES. 1810/2017)',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,131,'RETENCION MENSUAL PF',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,132,'AJUSTE ANUAL PF',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,133,'RETENCION COMPLEMENTARIA',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,134,'AFAP- RETENCION MENSUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,135,'RETENCION CORRESP. A EJERCICIOS ANTERIORES',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,137,'RETENCION MENSUAL NF',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,138,'AJUSTE ANUAL NF',NULL);
INSERT INTO percepciones_retenciones VALUES (1144,139,'FONASA - RETENCION MENSUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1145,141,'RETENCION MENSUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1145,142,'AJUSTE ANUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1145,143,'RETENCION COMPLEMENTARIA',NULL);
INSERT INTO percepciones_retenciones VALUES (1145,145,'RETENCION CORRESP. A EJERCICIOS ANTERIORES',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,51,'RET. S/INT. DE DEPOSITOS M/N Y UI A MAS DE 1 AÑO',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,52,'RET. S/INT. DE DEPOSITOS M/N A 1 AÑO O MENOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,53,'RET. S/INT. DE OBL Y OTROS TITULOS A MAS DE 3 AÑOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,54,'RET. S/RESTANTES RENTAS FINANCIERAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,61,'RET. S/RENTAS VITALICIAS, SEGUROS Y SIMILAR',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,62,'RET. S/RESTANTES RENTAS DE CAPITAL MOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,63,'RET. S/DIVIDENDOS Y UTILIDADES PAGADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,64,'RET. S/DER. FEDERATIVOS, DE IMAGEN Y SIM DEP.',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,65,'RET. S/DERECHOS DE AUTOR DE OBRAS LIT, ART O CIENTIFICAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,66,'RET. S/DIVID. Y UTIL. ORIGEN - NUM. 2, ART.3, T.7',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,67,'RET. S/REND. CAP. MOB. PROV. DE ENTIDAD. NO RESIDEN.',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,81,'RET. POR INC. PATRIMONIALES S/BS. MUEBLES ADQ. REMATE',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,136,'RET. S/RENTAS DE TRABAJADORES INDEPENDIENTE',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,153,'RET. S/REND DE OBL Y OTROS TITULOS A MAS DE 3 AÑOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,154,'RET. S/RESTANTES RENTAS FINANCIERAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,161,'RET. S/RENTAS VITALICIAS, SEGUROS Y SIMILARES',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,162,'RET. S/RESTANTES RENTAS DE CAPITAL MOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,163,'RET. S/DIVIDENDOS Y UTILIDADES PAGADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,164,'RET. S/DER. FEDERATIVOS, DE IMAGEN Y SIM DEP.',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,165,'RET. S/DERECHOS DE AUTOR DE OBRAS LIT, ART O CIENTIFICAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,166,'RET. S/DIVID. Y UTIL. ORIGEN - NUM. 2, ART.3, T.7',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,167,'RET. S/REND. CAP. MOB. PROV. DE ENTIDAD. NO RESIDEN.',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,171,'RET. S/ARREND Y OTRAS RENTAS DE CAPITAL INMOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,172,'RET. S/ARREND REALIZADA POR OTRO AGENTE',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,175,'RET. S/ARREND. REALIZ. A LA ENTIDAD ADMINSTRADORA',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,176,'RET. S/ARRENDAMIENTOS A TURISTAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1146,181,'RET. POR INC. PATRIMONIALES S/BS. MUEBLES ADQ. REM',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,31,'RET. S/RENTAS DE TRABAJADORES DEPENDIENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,36,'RET. S/RENTAS DE TRABAJADORES INDEPENDIENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,37,'RET. S/RENTAS TRABAJ. INDEPEND.–CONV. DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,41,'RET. S/PASIVIDADES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,51,'RET. S/INT. DE DEPOSITOS M/N Y UI A MAS DE UN AÑO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,52,'RET. S/INT. DE DEPOSITOS M/N A 1 AÑO O MENOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,53,'RET. S/INT. DE OBL Y OTROS TITULOS A MAS DE 3 AÑOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,54,'RET. S/RESTANTES RENTAS FINANCIERAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,55,'RET. S/ INTERESES – CONV. DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,61,'RET. S/RENTAS VITALICIAS. SEGUROS Y SIMILARES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,62,'RET. S/RESTANTES RENTAS DE CAPITAL MOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,63,'RET. S/DIVIDENDOS Y UTILIDADES PAGADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,64,'RET. S/DER FEDERATIVOS Y SIM DE DEP.',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,65,'RET. S/DIVID. Y UTILID. PAGADAS–CONV. DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,66,'RET. S/DER FEDE, IMAG, SIM DE DEP.–CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,67,'RET. S/GANAN. DE CAPITAL MOBILIA.–CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,71,'RET. S/ARREND Y OTRAS RENTAS DE CAPITAL INMOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,72,'RET. S/ARREND REALIZADA POR OTRO AGENTE',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,76,'RET. SOBRE ARRENDAMIENTOS A TURISTAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,81,'RET. POR INC. PATRIMONIALES S/BS. MUEBLES ADQ. REMATE',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,82,'RET. POR OTROS INC. PATRIMONIALES SEGUN DTO. 155/008',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,91,'RET. S/RENTAS DE ACT. EMPRESARIALES Y ASIMILADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,92,'RET. S/RENTAS DE ACT. EMPRESARIALES INTERNACIONAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,93,'RET. S/ RENTAS DE ACT. EMPRESAR.–CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,131,'RET. S/RENTAS DE TRABAJADORES DEPENDIENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,136,'RET. S/RENTAS DE TRABAJADORES INDEPENDIENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,137,'RET. S/RENTAS TRABAJ. INDEPEND.–CONV. DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,141,'RET. S/PASIVIDADES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,153,'RET. S/INT. DE OBL Y OTROS TITULOS A MAS DE 3 AÑOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,154,'RET. S/RESTANTES RENTAS FINANCIERAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,155,'RET. S/ INTERESES – CONV. DOBLE IMPOS.',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,161,'RET. S/RENTAS VITALICIAS. SEGUROS Y SIMILARES',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,162,'RET. S/RESTANTES RENTAS DE CAPITAL MOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,163,'RET. S/DIVIDENDOS Y UTILIDADES PAGADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,164,'RET. S/DER FEDERATIVOS Y SIM DE DEP.',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,165,'RET. S/DIVID. Y UTILID. PAGADAS–CONV. DOBLE IMPOS.',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,166,'RET. S/DER FEDE, IMAG, SIM DE DEP.–CONV DOBLE IMPO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,167,'RET. S/GANAN. DE CAPITAL MOBILIA.–CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,171,'RET. S/ARREND Y OTRAS RENTAS DE CAPITAL INMOBILIARIO',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,172,'RET. S/ARREND REALIZADA POR OTRO AGENTE     ',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,175,'RET. S/ARREND. REALIZ. A LA ENTIDAD ADMINSTRADORA',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,176,'RET. S/ARRENDAMIENTOS A TURISTAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,181,'RET. POR INC. PATRIMONIALES S/BS. MUEBLES ADQ. REMATE',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,182,'RET. POR OTROS INC. PATRIMONIALES SEGUN DTO. 155/008',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,183,'RET. S/INCREMENTOS PATRIMONIALES - CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,191,'RET. S/RENTAS DE ACT. EMPRESARIALES Y ASIMILADAS',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,192,'RET. S/RENTAS DE ACT. EMPRESARIALES INTERNACIONAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1246,193,'RET. S/ RENTAS DE ACT. EMPRESAR.–CONV DOBLE IMPOS',NULL);
INSERT INTO percepciones_retenciones VALUES (1845,141,'RETENCION MENSUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1845,142,'AJUSTE ANUAL',NULL);
INSERT INTO percepciones_retenciones VALUES (1845,143,'RETENCION COMPLEMENTARIA',NULL);
INSERT INTO percepciones_retenciones VALUES (1845,145,'RETENCION CORRESP A EJERCICIOS ANTERIORES',NULL);
INSERT INTO percepciones_retenciones VALUES (1700,48,'ITP - IMPUESTO A LAS TRANSMISIONES PATRIMONOLES',NULL);
INSERT INTO percepciones_retenciones VALUES (1700,82,'IRPF - INCREMENTOS PATRIMONIALES - AG. RET.',NULL);
INSERT INTO percepciones_retenciones VALUES (1700,83,'IRNR - INCREMENTOS PATRIMONIALES - AG. RET.',NULL);
INSERT INTO percepciones_retenciones VALUES (1701,48,'ITP - IMPUESTO A LAS TRANSMISIONES PATRIMONOLES',NULL);
INSERT INTO percepciones_retenciones VALUES (1701,82,'IRPF - INCREMENTOS PATRIMONIALES - AG. RET.',NULL);
INSERT INTO percepciones_retenciones VALUES (1701,83,'IRNR - INCREMENTOS PATRIMONIALES - AG. RET.',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,11,'AG.RET-ADICIONAL MEVIR PLAZA (COD.574)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,12,'AG.RET-ADICIONAL INIA PLAZA (COD.576)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,21,'AG.RET-ADICIONAL MEVIR EXPORTACION (COD.584)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,22,'AG.RET-ADICIONAL INIA EXPORTACION (COD.586)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,101,'AG.RET-IMEBA MIGRACION',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,110,'AG.RET-IMEBA PLAZA-LANAS (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,111,'AG.RET-IMEBA PLAZA-CUEROS (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,112,'AG.RET-IMEBA PLAZA-GANADO BOVINO (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,113,'AG.RET-IMEBA PLAZA-GANADO OVINO (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,114,'AG.RET-IMEBA PLAZA-GANADO SUINO (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,115,'AG.RET-IMEBA PLAZA-CEREALES Y OLEAGIN.(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,116,'AG.RET-IMEBA PLAZA-LECHE (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,117,'AG.RET-IMEBA PLAZA-PROD.DE AVICULTURA (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,118,'AG.RET-IMEBA PLAZA-PROD.DE APICULTURA (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,119,'AG.RET-IMEBA PLAZA-PROD.DE CUNICULTURA (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,120,'AG.RET-IMEBA PLAZA-FLORES Y SEMILLAS (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,121,'AG.RET-IMEBA PLAZA-HORTICULTURA Y FRUTIC.(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,122,'AG.RET-IMEBA PLAZA-CITRUS (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,123,'AG.RET-IMEBA PLAZA-RANICULTURA (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,124,'AG.RET-IMEBA PLAZA-HELICICULTURA(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,125,'AG.RET-IMEBA PLAZA-CRIA DE NANDU(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,126,'AG.RET-IMEBA PLAZA-CRIA DE NUTRIAS(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,127,'AG.RET-IMEBA PLAZA-PRODUCTOS DE ORIGEN FORESTAL(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,128,'AG.RET-IMEBA PLAZA - RESTANTES PRODUCTOS AGROP. (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,129,'AG.RET- IMEBA PLAZA - CAÑA DE AZUCAR (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,210,'AG.RET-IMEBA EXPORTACION-LANAS (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,211,'AG.RET-IMEBA EXPORTACION-CUEROS (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,212,'AG.RET-IMEBA EXPORTACION-GANADO BOVINO (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,213,'AG.RET-IMEBA EXPORTACION-GANADO OVINO (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,214,'AG.RET-IMEBA EXPORTACION-GANADO SUINO (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,215,'AG.RET-IMEBA EXPORTACION-CEREALES Y OLEAGIN.(COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,216,'AG.RET-IMEBA EXPORTACION-LECHE (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,217,'AG.RET-IMEBA EXPORTACION-PROD.DE AVICULTURA (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,218,'AG.RET-IMEBA EXPORTACION-PROD.DE APICULTURA (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,219,'AG.RET-IMEBA EXPORTACION-PROD.DE CUNICULTURA (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,220,'AG.RET-IMEBA EXPORTACION-FLORES Y SEMILLAS (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,221,'AG.RET-IMEBA EXPORTACION-HORTICULTURA Y FRUTIC.(COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,222,'AG.RET-IMEBA EXPORTACION-CITRUS (COD.582)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,223,'AG.RET-IMEBA EXP.-RANICULTURA (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,224,'AG.RET-IMEBA EXP.-HELICICULTURA(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,225,'AG.RET-IMEBA EXP.-CRIA DE NANDU(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,226,'AG.RET-IMEBA EXP-CRIA DE NUTRIAS(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,227,'AG.RET-IMEBA EXP.-PRODUCTOS DE ORIGEN FORESTAL(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,228,'AG.RET-IMEBA EXP-RESTANTES PRODUCTOS AGROP.(COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2142,229,'AG.RET- IMEBA EXP - CAÑA DE AZUCAR (COD.572)',NULL);
INSERT INTO percepciones_retenciones VALUES (2180,48,'ITP - IMPUESTO A LAS TRASMISIONES PATRIMONIALES',NULL);
INSERT INTO percepciones_retenciones VALUES (2180,49,'ITP - ADICIONAL IMPUESTO A LAS TRASMISIONES PATRIMONIALES',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,40,'REDUCCION DEL IVA – DECRETO 203/014',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,410,'REDUCCION ALICUOTA LEY 17.934',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,411,'REDUCCION ALICUOTA ART.5 DEC. 288/012',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,412,'CRED. ARRENDAMIENTO TERMINALES ART.12 DEC. 288/012',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,413,'REDUCCION TOTAL DEL IVA A TURISTAS NO RESIDENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,414,'CREDITO ARRENDAMIENTO A TURISTAS NO RESIDENTES',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,415,'REDUCCION DEL IVA - ART.10 DEC. 203/014',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,421,'REDUCCION ALICUOTA ART.6 DEC. 288/012',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,422,'CRED. ARRENDAMIENTO TERMINALES ART.13 DEC. 288/012',NULL);
INSERT INTO percepciones_retenciones VALUES (2181,425,'REDUCCION DEL IVA - ART.13 DEC. 203/014',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,10,'IVA',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,11,'IVA BANCA DE CUBIERTA COLECTIVA',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,21,'IRIC - REGALIAS, ARREND., USO Y CESION DE USO DE EQUIP.',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,22,'IRIC - REMUNERACIONES DE SERVICIOS TECNICOS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,23,'IRIC - DIVIDENDOS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,24,'IRIC - UTILIDADES PERSONAS DEL EXTERIOR',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,25,'IRIC - DERECHOS DE AUTOR',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,30,'IMPUESTO AL PATRIMONIO',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,31,'IMPUESTO AL PATRIMONIO - CONVENIO DOBLE IMPOSICION',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,40,'IMPUESTO A LAS COMISIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,41,'IMP. A LA  COMPRA VTA  BS. MUEBLES EN REMATE PUBLICO',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,42,'IMPUESTO A LOS ACTIVOS DE EMPRESAS BANCARIAS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,43,'IMP. A LOS INGRESOS DE LOS ORGANIZADORES DE SORTEOS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,44,'IMP.A LAS RETRIB. POR SERV.AL ESTADO (ART.587 LEY 17.296)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,45,'ADIC. IMP. A LAS RETRIB. PERSONALES (ART.7 LEY 17.453)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,46,'IMPUESTO DE CONTROL DEL SISTEMA FINANCIERO',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,47,'IMPUESTO A LAS RETRIB.PERSONALES (ART.7 LEY 17.502)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,51,'IVA - FRIGORIFICOS Y MATADEROS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,52,'IVA - INAVI',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,60,'IMESI',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,70,'IMP. A LAS CESIONES DE DERECHOS SOBRE DEPORTISTAS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,110,'IVA - PRESTACIONES SERVICIOS DE SALUD (ART.8 BIS DTO. 220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,111,'IVA -CONTRATOS CONSULT. CON EL  ESTADO (ART.7 DTO. 220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,112,'IVA - CORREDORES DE SEGUROS (RES. 406/82)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,113,'IVA - CORREDORES DE BOLSA (RES.10/83)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,114,'IVA - EMP.SEGURIDAD, VIGILANCIA Y LIMPIEZA (D.194/00 Y 528/03)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,115,'IVA - EMP. TRANSPORTISTAS PROFESIONALES DE CARGA',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,116,'IVA - ENTIDADES ASEGURADORAS ( DTO. 336/03)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,117,'IVA - TRANSPORTE TERRESTRE DE PASAJEROS ( DTO. 405/03)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,118,'IVA - COMPRAS DEL ESTADO (DTOS. 528/03 Y 319/06)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,119,'IVA - OTRAS RETENCIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,121,'IRIC/IRAE- EMP. SEGURIDAD, VIGILANCIA Y LIMPIEZA (DTO.194/00)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,122,'IRIC/IRAE EMP- CONTRAT.DE OBRAS PUBLIC.VIALES (DTO.271/90)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,123,'IRIC/IRAE - OTRAS RETENCIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,124,'IRAE - INSTITUCIONES DEPORTIVAS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,131,'IMPUESTO AL PATRIMONIO - CONVENIO DOBLE IMPOSICION',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,141,'IMPUESTO AL PATRIMONIO',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,151,'IMP. COMISIONES . CORREDORES DE SEG. (ART. 8 DTO. 691/90)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,152,'IMP. COMISIONES - REMATADORES (ART.10 DTO.691/90)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,153,'IMP. COMISIONES - OTRAS RETENCIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,160,'OBLIGACIONES TRIBUT.-ADMINIST. DE CRED.CONTRIB. LIT. E',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,161,'OBLIGACIONES TRIBUT.-ADMINIST. DE CRED. TASA 5%',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,162,'OBLIGACIONES TRIBUT.-ADMINIST. DE CRED. TASA 2%',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,163,'OBLIGACIONES TRIBUT.-ADMINIST. DE CRED. LEY 17.934',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,164,'OBLIGACIONES TRIBUTARIAS DE TERCEROS - ANTEL',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,165,'OBLIGACIONES TRIBUTARIAS DE TERCEROS - DTO. 134/2009',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,166,'OBLIGACIONES TRIB.- ADM. DE TARJETAS DEC.288/012',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,167,'OBLIGACIONES TRIB.- OPERACIONES DEL ART. 1 DEC. 203/014.',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,168,'OBLIGACIONES TRIB.- OPERACIONES DEL ART. 2 DEC. 203/014',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,169,'OBLIGACIONES TRIB.- OPERACIONES DEL ART. 3 DEC. 203/014',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,170,'COFIS',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,211,'IVA - FRIGORIF. Y MATAD. CARNE FRESCA (ART.9 DTO.220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,212,'IVA - FRIGORIF. Y MATADEROS FAENA (ART.12 DTO.220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,213,'IVA - GRASAS Y LUBRICANTES (ART.14 DTO.220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,214,'IVA - VENTA DIRECTA (ART.16 DTO.220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,215,'IVA - VEHICULOS AUTOMOTORES (ART.20 DTO.220/98)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,216,'IVA - INAVI',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,217,'IVA - FABRICANTES E IMPORT. DE PREFORMAS P.E.T.',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,218,'IVA -  ESTABLECIMIENTOS DE FAENA DE AVES (DTO. 621/2006)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,219,'IVA GAS OIL',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,220,'IVA - OTRAS PERCEPCIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,221,'IVA - FUEGOS ARTIFICIALES (RES 4080/2013)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,228,'IRIC/IRAE - ESTABLECIMIENTOS DE FAENA DE AVES (DTO. 621/2006)',NULL);
INSERT INTO percepciones_retenciones VALUES (2183,229,'IRAE - OTRAS PERCEPCIONES',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,202,'SEGUROS - INCENDIO',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,205,'SEGUROS - VEHICULOS AUTOMOT. O REMOLCAD.',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,208,'SEGUROS - ROBO Y RIESGOS SIMILARES',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,211,'SEGUROS - RESPONSABILIDAD CIVIL',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,214,'SEGUROS - CAUCION',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,217,'SEGUROS - TRANSPORTE',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,220,'SEGUROS - MARITIMOS',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,223,'SEGUROS - ULT. INC. ART. 2 LEY 16426, INTERPRE. POR LEY 16851',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,226,'SEGUROS - OTROS',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,229,'SEGUROS - VIDA',NULL);
INSERT INTO percepciones_retenciones VALUES (2192,232,'SEGUROS - VIDA OTROS',NULL);

---------------------------

INSERT INTO SUCURSALES VALUES(1,'','',NULL,NULL,false,true);

------------------------------------------------------------------------------------------
-- Para Credito de la Casa
--UPDATE parametros SET valor_integer = 1 WHERE id_parametro='MODO_IMPRESION_LOTES';
--UPDATE tipos_comprobante SET template_impresion='TicketCrCa.jasper', class_impresion='org.gs1uy.rondanetweb.print.services.PrintTicketCrCa' WHERE id_tipo_comprobante= 101 AND id_tipo_comprobante= 102 AND id_tipo_comprobante= 103;
--UPDATE tipos_comprobante SET template_impresion='BillGuardCrCa.jasper', class_impresion='org.gs1uy.rondanetweb.print.services.PrintBillGuard' WHERE id_tipo_comprobante= 111 AND id_tipo_comprobante= 112 AND id_tipo_comprobante= 113;

CREATE UNIQUE INDEX COMPROBANTE_UNICO ON COMPROBANTES(RUT_EMISOR, SERIE, NRO_DOCUMENTO, EMITIDO_RECIBIDO, ID_TIPO_COMPROBANTE);
CREATE INDEX ON COMPROBANTES(ID_TIPO_COMPROBANTE, SERIE_INTERNO, NRO_INTERNO);

-- Dejar indices siempre al final.
CREATE INDEX ON COMPROBANTES(ID_TIPO_COMPROBANTE, SERIE_INTERNO, NRO_INTERNO);
CREATE INDEX ON comprobantes (fecha ASC NULLS LAST);
CREATE INDEX ON ALARMAS(FECHA_ENVIO);
CREATE INDEX ON SOBRES(FECHA_TRANSMISION);
CREATE INDEX ON SOBRES(ID_DETALLE_ACUSE);
CREATE INDEX ON COMPROBANTES(ID_SOBRE_RE);
CREATE INDEX ON COMPROBANTES(ID_SOBRE_DGI, ENVIAR_DGI);
CREATE INDEX ON CAES_ASIGNADOS(ID_CAE );
CREATE INDEX ON COMPROBANTES(FECHA_PROCESADO );
CREATE INDEX ON RANGOS_COMPROBANTES_ANULADOS(ID_CAE_ASIGNADO );
CREATE INDEX comprobantes_ts_firma_idx
  ON comprobantes
  USING btree
  (ts_firma );
CREATE INDEX 
   ON comprobantes (id_sobre_re ASC NULLS LAST, enviar_dc ASC NULLS LAST, enviar_dgi ASC NULLS LAST, enviar_re ASC NULLS LAST, emitido_recibido ASC NULLS LAST);

CREATE INDEX persistir_corresp_ix ON comprobantes (persistir_corresp) WHERE  persistir_corresp IS TRUE;
  
CREATE INDEX comprobantes_estadoImpresionRecibo_ix ON comprobantes (estado_impresion_recibo) WHERE  estado_impresion_recibo = 1;
CREATE INDEX comprobantes_transporte_ix ON comprobantes (transporte) WHERE  emitido_recibido = 'E';

CREATE INDEX comprobantes_id_cae_asignado ON comprobantes (id_cae_asignado ASC NULLS LAST);

 CREATE INDEX comprobantes_ts_firma_rut_emisor_serie_emitido_recibido_id__idx
 ON comprobantes
 USING btree (ts_firma, rut_emisor, serie, emitido_recibido, id_tipo_comprobante);

 CREATE INDEX ON ACUSES(fecha_transmision);
 
CREATE INDEX productos_servicios_codigo_ean_idx  ON productos_servicios USING btree (codigo_ean);
CREATE INDEX productos_servicios_codigo_interno_idx ON productos_servicios USING btree (codigo_interno);

CREATE INDEX numeros_asignados_numero_id_cae_asignado_idx ON numeros_asignados USING btree(id_cae_asignado ASC NULLS LAST, numero ASC NULLS LAST);
CREATE INDEX comprobantes_id_sobre_re_enviar_re_idx ON comprobantes USING btree(id_sobre_re NULLS FIRST, enviar_re);

CREATE OR REPLACE FUNCTION lo_manage()
  RETURNS "trigger" AS
'$libdir/lo', 'lo_manage'
  LANGUAGE 'c' VOLATILE;
ALTER FUNCTION lo_manage() OWNER TO postgres;

CREATE TRIGGER t_xml_comp BEFORE UPDATE OF XML OR DELETE ON COMPROBANTES
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML);
	
CREATE TRIGGER t_arch_orig BEFORE UPDATE OF ARCHIVO_ORIGINAL OR DELETE ON COMPROBANTES
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(ARCHIVO_ORIGINAL);
	
	
CREATE TRIGGER t_xml_asig BEFORE UPDATE OF XML_ASIGNACION OR DELETE ON CAES_ASIGNADOS
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML_ASIGNACION);
	
	
CREATE TRIGGER t_xml_sol BEFORE UPDATE OF XML_SOLICITUD OR DELETE ON CAES_ASIGNADOS
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML_SOLICITUD);
	
	
CREATE TRIGGER t_xml_sob BEFORE UPDATE OF XML OR DELETE ON SOBRES
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML);
	
	
CREATE TRIGGER t_xml_acu BEFORE UPDATE OF XML OR DELETE ON ACUSES
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML);
	
	
CREATE TRIGGER t_xml_rep BEFORE UPDATE OF XML OR DELETE ON REPORTES_DIARIOS
    FOR EACH ROW EXECUTE PROCEDURE lo_manage(XML);

CREATE OR REPLACE FUNCTION exec(text) returns text language plpgsql volatile
  AS $f$
    BEGIN
      EXECUTE $1;
      RETURN $1;
    END;
$f$;

SELECT exec('ALTER DATABASE ' || current_database() ||  ' OWNER TO rnlocal');

SELECT exec('ALTER TABLE ' || quote_ident(s.nspname) || '.' ||
            quote_ident(s.relname) || ' OWNER TO rnlocal')
  FROM (SELECT nspname, relname
          FROM pg_class c JOIN pg_namespace n ON (c.relnamespace = n.oid) 
         WHERE nspname NOT LIKE E'pg\\_%' AND 
               nspname <> 'information_schema' AND 
               relkind IN ('r','S','v') ORDER BY relkind = 'S') s;

