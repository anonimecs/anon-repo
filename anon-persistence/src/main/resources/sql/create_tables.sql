drop table DatabaseConfig
go
--drop table DatabaseTableData
go
--drop table DatabaseColumnData
go
drop table AnonymisationMethodData 
go
drop table AnonymisedColumnData
go


CREATE TABLE DatabaseConfig (	
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	URL VARCHAR(255) NOT NULL,
  	LOGIN VARCHAR(255) NOT NULL,
  	PASSWORD VARCHAR(255) NOT NULL,
  	VENDOR VARCHAR(255) NOT NULL,
  	VERSION VARCHAR(255),
    GUINAME VARCHAR(255) not null,
  	CONSTRAINT primary_key_DATABASECONFIG PRIMARY KEY (ID)
)
go

/*
create table DatabaseTableData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    DATABASECONFIG_ID INT not null,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
  	CONSTRAINT primary_key_DATABASETABLEINFO PRIMARY KEY (ID)
)
*/
go
/*
create table DatabaseColumnData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    COLUMNNAME VARCHAR(255) NOT NULL,
    COLUMNTYPE VARCHAR(255) NOT NULL,
    DatabaseTableData_ID int,
  	CONSTRAINT primary_key_DATABASECOLUMNINFO PRIMARY KEY (ID)
)*/
go

create table AnonymisationMethodData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    DATABASECONFIG_ID INT not null,
    anonymizationType VARCHAR(30) NOT NULL,
    anonMethodClass VARCHAR(255) NOT NULL,
  	CONSTRAINT primary_key_AnonymisationMethodData PRIMARY KEY (ID)
)
go

create table AnonymisedColumnData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    COLUMNNAME VARCHAR(255) NOT NULL,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
    COLUMNTYPE VARCHAR(255) NOT NULL,
    AnonymisationMethodData_ID int,
  	CONSTRAINT primary_key_AnonymisedColumnData PRIMARY KEY (ID)
)
go
ALTER TABLE AnonymisedColumnData
	ADD CONSTRAINT unique_col_anonymisation
	 UNIQUE (COLUMNNAME, TABLENAME, SCHEMANAME, AnonymisationMethodData_ID) 
go