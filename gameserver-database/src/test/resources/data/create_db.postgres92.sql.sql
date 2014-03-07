create user game_server ENCRYPTED PASSWORD 'game_server';
create database gameserver;
grant ALL on DATABASE gameserver to game_server;

create sequence hibernate_seq start 32;
create table MATCHES ( ID bigint not null,
                     NAME text not null, 
                     IS_OPEN boolean default false, 
                     IS_STARTED boolean default false, 
                     MAX_PLAYERS int default 2, 
                     primary key (ID));

create table PLAYERS ( ID bigint not null,
                       NAME text not null, 
                       primary key (ID));

create table MATCH_PLAYERS( MATCH_ID bigint not null,
                           PLAYER_ID bigint not null,
                           CONSTRAINT match_fk FOREIGN KEY (MATCH_ID) REFERENCES MATCHES (ID),
                           CONSTRAINT player_fk FOREIGN KEY (PLAYER_ID) REFERENCES PLAYERS (ID));
 