/*/etc/mysql/my.cnf

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
init_connect='SET collation_connection = utf8_unicode_ci'
character-set-server = utf8*/

drop database if exists super_optimum;
create database super_optimum;