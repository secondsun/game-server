create sequence hibernate_seq AS bigint start with 32 increment by 7 no maxvalue;
create table MATCHES ( ID bigint not null,
                     NAME clob not null, 
                     IS_OPEN boolean default false, 
                     IS_STARTED boolean default false, 
                     MAX_PLAYERS int default 2, 
                     primary key (ID));

create table PLAYERS ( ID bigint not null,
                       NAME clob not null, 
                       primary key (ID));

create table MATCH_PLAYERS( MATCH_ID bigint not null,
                           PLAYER_ID bigint not null,
                           CONSTRAINT match_fk FOREIGN KEY (MATCH_ID) REFERENCES MATCHES (ID),
                           CONSTRAINT player_fk FOREIGN KEY (PLAYER_ID) REFERENCES PLAYERS (ID));
 