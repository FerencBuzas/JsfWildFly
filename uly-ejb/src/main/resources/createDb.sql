-- Create database, tables and some test data. --

DROP DATABASE IF EXISTS uly;

CREATE DATABASE uly;

CREATE USER IF NOT EXISTS feri IDENTIFIED BY 'feri';
CREATE USER IF NOT EXISTS bea IDENTIFIED BY 'bea';
CREATE USER IF NOT EXISTS marci IDENTIFIED BY 'marci';

USE uly;

GRANT ALL ON uly TO feri;
GRANT ALL ON uly TO bea;
GRANT ALL ON uly TO marci;
