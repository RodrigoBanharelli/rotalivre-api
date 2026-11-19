--Rotalivre - GS 2026/2 CitizenTech - FIAP 1TDSPW
--Mapa colaborativo de acessibilidade urbana.
--Criação das tabelas, sequences, relacionamentos e carga de 60 registros no Oracle.
--Dados de exemplo ficticios.

--Descomente o bloco abaixo para recriar o schema do zero.
--drop table tb_relato;
--drop table tb_local_recurso;
--drop table tb_local_acessivel;
--drop table tb_recurso_acessibilidade;
--drop table tb_tipo_local;
--drop table tb_usuario;
--drop sequence sq_tb_relato;
--drop sequence sq_tb_local_acessivel;
--drop sequence sq_tb_recurso_acessibilidade;
--drop sequence sq_tb_tipo_local;
--drop sequence sq_tb_usuario;

--Usuario: cidadao que relata. Sem senha (sem login na entrega).
create table tb_usuario (
        cd_usuario           number(9,0) primary key,
        nm_usuario           varchar2(100) not null,
        ds_email             varchar2(120) not null,
        ds_perfil_mobilidade varchar2(30),
        dt_cadastro          date default sysdate not null,
        constraint uk_tb_usuario_email unique (ds_email),
        constraint ck_tb_usuario_perfil check (ds_perfil_mobilidade in
                ('CADEIRANTE', 'MOBILIDADE_REDUZIDA', 'DEFICIENCIA_VISUAL', 'IDOSO', 'OUTRO'))
);

create sequence sq_tb_usuario start with 1 increment by 1 nocache;

--Tipo de local: lista fixa.
create table tb_tipo_local (
        cd_tipo_local number(9,0) primary key,
        nm_tipo_local varchar2(30) not null,
        ds_tipo_local varchar2(200),
        constraint uk_tb_tipo_local_nome unique (nm_tipo_local),
        constraint ck_tb_tipo_local_nome check (nm_tipo_local in
                ('CALCADA', 'RAMPA', 'ELEVADOR', 'ESTABELECIMENTO', 'OBSTACULO'))
);

create sequence sq_tb_tipo_local start with 1 increment by 1 nocache;

--Recurso de acessibilidade: rampa, piso tatil, vaga PcD...
create table tb_recurso_acessibilidade (
        cd_recurso number(9,0) primary key,
        nm_recurso varchar2(60) not null,
        ds_recurso varchar2(200),
        constraint uk_tb_recurso_nome unique (nm_recurso)
);

create sequence sq_tb_recurso_acessibilidade start with 1 increment by 1 nocache;

--Local acessivel: calcada, rampa, elevador, estabelecimento ou obstaculo.
create table tb_local_acessivel (
        cd_local          number(9,0) primary key,
        nm_local          varchar2(120) not null,
        ds_endereco       varchar2(200) not null,
        vl_latitude       number(9,6) not null,
        vl_longitude      number(9,6) not null,
        vl_inclinacao_pct number(5,2) not null,
        vl_largura_cm     number(5,0) not null,
        st_ativo          char(1) default 'S' not null,
        dt_cadastro       date default sysdate not null,
        constraint ck_tb_local_inclinacao check (vl_inclinacao_pct between 0 and 100),
        constraint ck_tb_local_largura check (vl_largura_cm > 0),
        constraint ck_tb_local_ativo check (st_ativo in ('S', 'N'))
);

create sequence sq_tb_local_acessivel start with 1 increment by 1 nocache;

--Relato: o que o cidadao viu no local.
create table tb_relato (
        cd_relato     number(9,0) primary key,
        ds_situacao   varchar2(12) not null,
        ds_observacao varchar2(400),
        dt_relato     date default sysdate not null,
        constraint ck_tb_relato_situacao check (ds_situacao in ('ACESSIVEL', 'PARCIAL', 'INACESSIVEL'))
);

create sequence sq_tb_relato start with 1 increment by 1 nocache;

--Associativa do N:N local <-> recurso (chave composta, sem sequence).
create table tb_local_recurso (
        cd_local       number(9,0) not null,
        cd_recurso     number(9,0) not null,
        st_disponivel  char(1) not null,
        dt_verificacao date not null,
        constraint pk_tb_local_recurso primary key (cd_local, cd_recurso),
        constraint ck_tb_local_recurso_disp check (st_disponivel in ('S', 'N'))
);

--1:N tipo -> local
alter table tb_local_acessivel add cd_tipo_local number(9,0) not null;
alter table tb_local_acessivel add constraint fk_tb_local_tipo
        foreign key (cd_tipo_local) references tb_tipo_local (cd_tipo_local);

--1:N local -> relato e 1:N usuario -> relato
alter table tb_relato add cd_local number(9,0) not null;
alter table tb_relato add cd_usuario number(9,0) not null;
alter table tb_relato add constraint fk_tb_relato_local
        foreign key (cd_local) references tb_local_acessivel (cd_local);
alter table tb_relato add constraint fk_tb_relato_usuario
        foreign key (cd_usuario) references tb_usuario (cd_usuario);

--N:N local <-> recurso
alter table tb_local_recurso add constraint fk_tb_local_recurso_local
        foreign key (cd_local) references tb_local_acessivel (cd_local);
alter table tb_local_recurso add constraint fk_tb_local_recurso_recurso
        foreign key (cd_recurso) references tb_recurso_acessibilidade (cd_recurso);

--Carga: usuarios (8)
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Ana Souza', 'ana.souza@exemplo.com', 'CADEIRANTE');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Bruno Lima', 'bruno.lima@exemplo.com', 'MOBILIDADE_REDUZIDA');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Carla Mendes', 'carla.mendes@exemplo.com', 'DEFICIENCIA_VISUAL');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Diego Rocha', 'diego.rocha@exemplo.com', 'IDOSO');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Elisa Prado', 'elisa.prado@exemplo.com', 'CADEIRANTE');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Fabio Nunes', 'fabio.nunes@exemplo.com', 'OUTRO');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Gabriela Torres', 'gabriela.torres@exemplo.com', 'MOBILIDADE_REDUZIDA');
insert into tb_usuario (cd_usuario, nm_usuario, ds_email, ds_perfil_mobilidade)
values (sq_tb_usuario.nextval, 'Henrique Alves', 'henrique.alves@exemplo.com', null);

--Carga: tipos de local (5)
insert into tb_tipo_local (cd_tipo_local, nm_tipo_local, ds_tipo_local)
values (sq_tb_tipo_local.nextval, 'CALCADA', 'Trecho de calcada para pedestres');
insert into tb_tipo_local (cd_tipo_local, nm_tipo_local, ds_tipo_local)
values (sq_tb_tipo_local.nextval, 'RAMPA', 'Rampa de acesso entre niveis');
insert into tb_tipo_local (cd_tipo_local, nm_tipo_local, ds_tipo_local)
values (sq_tb_tipo_local.nextval, 'ELEVADOR', 'Elevador publico ou de estacao');
insert into tb_tipo_local (cd_tipo_local, nm_tipo_local, ds_tipo_local)
values (sq_tb_tipo_local.nextval, 'ESTABELECIMENTO', 'Comercio ou servico aberto ao publico');
insert into tb_tipo_local (cd_tipo_local, nm_tipo_local, ds_tipo_local)
values (sq_tb_tipo_local.nextval, 'OBSTACULO', 'Barreira na via: buraco, degrau, poste');

--Carga: recursos de acessibilidade (8)
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Rampa de acesso', 'Rampa dentro da norma');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Piso tatil', 'Piso de alerta e direcional');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Banheiro adaptado', 'Banheiro com barras de apoio');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Vaga PcD', 'Vaga reservada para pessoa com deficiencia');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Elevador', 'Elevador com botoes em braile');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Corrimao', 'Corrimao dos dois lados');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Sinal sonoro', 'Aviso sonoro para deficiencia visual');
insert into tb_recurso_acessibilidade (cd_recurso, nm_recurso, ds_recurso)
values (sq_tb_recurso_acessibilidade.nextval, 'Porta larga', 'Porta com 80 cm ou mais');

--Carga: locais (14). Tipos: 1 CALCADA, 2 RAMPA, 3 ELEVADOR, 4 ESTABELECIMENTO, 5 OBSTACULO
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 1, 'Calcada Av. Paulista 1100', 'Av. Paulista, 1100', -23.563600, -46.654400, 2, 300, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 2, 'Rampa Praca do Ciclista', 'Praca do Ciclista', -23.555400, -46.662300, 8.33, 120, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 3, 'Elevador Estacao Exemplo', 'Av. Paulista, 2200', -23.557600, -46.660400, 0, 110, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 4, 'Restaurante Exemplo Augusta', 'Rua Augusta, 1500', -23.556000, -46.658000, 0, 90, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 5, 'Buraco na calcada Haddock Lobo', 'Rua Haddock Lobo, 400', -23.559000, -46.665000, 0, 60, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 1, 'Calcada Rua da Consolacao', 'Rua da Consolacao, 2000', -23.554500, -46.660000, 5, 150, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 2, 'Rampa Parque Trianon', 'Rua Peixoto Gomide, 949', -23.561700, -46.657500, 10, 110, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 3, 'Elevador Shopping Exemplo', 'Av. Paulista, 800', -23.564000, -46.652000, 0, 140, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 4, 'Farmacia Exemplo Alameda Santos', 'Alameda Santos, 1000', -23.566000, -46.651000, 3, 100, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 5, 'Poste no meio da calcada', 'Rua Frei Caneca, 300', -23.553000, -46.653000, 0, 70, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 1, 'Calcada Rua Pamplona', 'Rua Pamplona, 1200', -23.567000, -46.656000, 12, 80, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 2, 'Rampa Biblioteca Exemplo', 'Rua Treze de Maio, 500', -23.560000, -46.649000, 6, 130, 'S');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 4, 'Mercado Exemplo Bela Vista', 'Rua Rui Barbosa, 200', -23.561000, -46.645000, 0, 95, 'N');
insert into tb_local_acessivel (cd_local, cd_tipo_local, nm_local, ds_endereco, vl_latitude, vl_longitude, vl_inclinacao_pct, vl_largura_cm, st_ativo)
values (sq_tb_local_acessivel.nextval, 5, 'Degrau sem rampa Bela Cintra', 'Rua Bela Cintra, 700', -23.558000, -46.661000, 0, 50, 'S');

--Carga: recursos de cada local, N:N (15)
--Recursos: 1 Rampa, 2 Piso tatil, 3 Banheiro, 4 Vaga PcD, 5 Elevador, 6 Corrimao, 7 Sinal sonoro, 8 Porta larga
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (2, 1, 'S', sysdate - 3);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (2, 6, 'S', sysdate - 3);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (3, 5, 'S', sysdate - 5);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (3, 7, 'N', sysdate - 5);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (4, 3, 'S', sysdate - 10);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (4, 4, 'N', sysdate - 10);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (4, 8, 'S', sysdate - 10);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (7, 1, 'S', sysdate - 7);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (7, 6, 'N', sysdate - 7);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (8, 5, 'S', sysdate - 2);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (8, 3, 'S', sysdate - 2);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (9, 1, 'S', sysdate - 12);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (9, 8, 'S', sysdate - 12);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (12, 1, 'S', sysdate - 4);
insert into tb_local_recurso (cd_local, cd_recurso, st_disponivel, dt_verificacao) values (12, 2, 'S', sysdate - 4);

--Carga: relatos (10). Datas com sysdate - n para a regra dos 30 dias funcionar em qualquer dia.
--Local 2: 3 usuarios diferentes -> nivel 68,4 (PARCIAL) e CONFIRMADO
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 2, 1, 'ACESSIVEL', 'Rampa em bom estado', sysdate - 2);
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 2, 2, 'PARCIAL', 'Inclinacao forte no final', sysdate - 10);
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 2, 3, 'INACESSIVEL', 'Rampa bloqueada por obra', sysdate - 20);
--Local 5: tudo INACESSIVEL -> local mais critico
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 5, 4, 'INACESSIVEL', 'Buraco grande e sem sinalizacao', sysdate - 1);
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 5, 5, 'INACESSIVEL', 'Cadeira de rodas nao passa', sysdate - 3);
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 5, 6, 'INACESSIVEL', 'Continua sem reparo', sysdate - 6);
--Local 3: so 2 usuarios -> NAO_CONFIRMADO
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 3, 1, 'ACESSIVEL', 'Elevador funcionando', sysdate - 4);
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 3, 7, 'PARCIAL', 'Sinal sonoro quebrado', sysdate - 9);
--Local 4: 1 relato
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 4, 8, 'PARCIAL', 'Banheiro sem barra de apoio', sysdate - 15);
--Local 11: relato com 45 dias -> fica fora da janela de 30 dias
insert into tb_relato (cd_relato, cd_local, cd_usuario, ds_situacao, ds_observacao, dt_relato)
values (sq_tb_relato.nextval, 11, 2, 'INACESSIVEL', 'Calcada estreita e inclinada', sysdate - 45);

commit;

--Conferencia: a soma deve dar 60.
select 'tb_usuario' tabela, count(*) qtd from tb_usuario
union all select 'tb_tipo_local', count(*) from tb_tipo_local
union all select 'tb_recurso_acessibilidade', count(*) from tb_recurso_acessibilidade
union all select 'tb_local_acessivel', count(*) from tb_local_acessivel
union all select 'tb_local_recurso', count(*) from tb_local_recurso
union all select 'tb_relato', count(*) from tb_relato;
