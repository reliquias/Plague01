use cprg_empresa;

/*
CREATE TABLE PRG_EMPRESA
(
ID int NOT NULL,
CNPJ varchar(19) NOT NULL,
CONFIG_FILE_NAME varchar(50),
SQL_DIALECT text,
URL_CONEXAO text,
DRIVER_CLASS text,
USER_NAME text,
PASSWORD text,
PRIMARY KEY (ID)
)
*/

/*
select 
cnpj
,CAST(url_conexao  AS CHAR(10000)) 
,CAST(CONFIG_FILE_NAME  AS CHAR(10000)) 
,CAST(SQL_DIALECT  AS CHAR(10000)) 
,CAST(USER_NAME  AS CHAR(10000)) 
,CAST(PASSWORD  AS CHAR(10000)) 
,CAST(DRIVER_CLASS AS CHAR(10000)) 
from prg_empresa
*/

/*
insert into prg_empresa (id,CNPJ      ,CONFIG_FILE_NAME         ,SQL_DIALECT                         ,URL_CONEXAO                             ,DRIVER_CLASS           ,USER_NAME,PASSWORD)
                   VALUES (1,'server01','hibernate.cfg.Default.xml','org.hibernate.dialect.MySQLDialect','jdbc:mysql://localhost/BD_PLG_SERVER01','com.mysql.jdbc.Driver','root'   ,'appbd')                                   
*/


use bd_plg_server01;

insert into usuario (nome, login, senha, ativo, cpf, matricula) 
values ('Master do Universo', 'master','master',1,'12345X', '112233'); 

