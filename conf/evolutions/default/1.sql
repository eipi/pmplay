# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Activities (
  id                        bigint not null,
  type                      varchar(255),
  location                  varchar(255),
  distance                  double,
  start_time                timestamp,
  constraint pk_Activities primary key (id))
;

create table Locations (
  id                        bigint not null,
  latitude                  double,
  longitude                 double,
  constraint pk_Locations primary key (id))
;

create table Users (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_Users primary key (id))
;

create sequence Activities_seq;

create sequence Locations_seq;

create sequence Users_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists Activities;

drop table if exists Locations;

drop table if exists Users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists Activities_seq;

drop sequence if exists Locations_seq;

drop sequence if exists Users_seq;

