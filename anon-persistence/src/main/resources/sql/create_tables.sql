CREATE TABLE SecurityUser (
  ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  USERNAME VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  NAME VARCHAR(255),
  SURNAME VARCHAR(255),
  ENABLED VARCHAR(1) NOT NULL,
  ENCRYPTED VARCHAR(1) DEFAULT NULL,
  CONSTRAINT primary_key_SecurityUser PRIMARY KEY (ID)
)
go

CREATE TABLE SecurityUserRoles (
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	USER_ID INT NOT NULL,
	ROLE VARCHAR(255) NOT NULL,
	CONSTRAINT primary_key_SecurityUserRoles PRIMARY KEY (ID),
	CONSTRAINT foreign_key_SecurityUser FOREIGN KEY (USER_ID) REFERENCES SecurityUser (ID)
)
go

CREATE TABLE DatabaseConfig (	
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	URL VARCHAR(255) NOT NULL,
  	DEFAULTSCHEMA VARCHAR(255) NOT NULL,
  	LOGIN VARCHAR(255) NOT NULL,
  	PASSWORD VARCHAR(255) NOT NULL,
  	VENDOR VARCHAR(255) NOT NULL,
  	VERSION VARCHAR(255),
    GUINAME VARCHAR(255) not null unique,
  	CONSTRAINT primary_key_DATABASECONFIG PRIMARY KEY (ID)
)
go

create table AnonymisationMethodData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    DATABASECONFIG_ID INT not null,
    anonymizationType VARCHAR(30) NOT NULL,
    anonMethodClass VARCHAR(255) NOT NULL,
  	CONSTRAINT primary_key_AnonymisationMethodData PRIMARY KEY (ID),
    CONSTRAINT fk_AnonymisationMethodData_DatabaseConfig FOREIGN KEY (DATABASECONFIG_ID) REFERENCES DatabaseConfig (ID)
)
go

create table AnonymisedColumnData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    COLUMNNAME VARCHAR(255) NOT NULL,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
    COLUMNTYPE VARCHAR(255) NOT NULL,
    AnonymisationMethodData_ID int,
  	CONSTRAINT primary_key_AnonymisedColumnData PRIMARY KEY (ID),
    CONSTRAINT fk_AnonymisedColumnData_AnonymisationMethodData FOREIGN KEY (AnonymisationMethodData_ID) REFERENCES AnonymisationMethodData (ID)

)
go

ALTER TABLE AnonymisedColumnData
	ADD CONSTRAINT unique_col_anonymisation
	 UNIQUE (COLUMNNAME, TABLENAME, SCHEMANAME, AnonymisationMethodData_ID) 
go


create table ExecutionData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    description VARCHAR(1000),
	userName VARCHAR(50) NOT NULL,
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    status varchar(20) not null
)
go

create table ExecutionMethodData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    RESULTTEXT VARCHAR(1000),
    EXECUTION_ID INT NOT NULL,
    METHOD_ID INT NOT NULL,
    RUNTIMESEC int not null,
    status varchar(20) not null,
    CONSTRAINT fk_ExecutionMethod_Execution FOREIGN KEY (EXECUTION_ID) REFERENCES ExecutionData (ID),
    CONSTRAINT fk_ExecutionMethod_Method FOREIGN KEY (METHOD_ID) REFERENCES AnonymisationMethodData (ID)
)
go

create table ExecutionColumnData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    RESULTTEXT VARCHAR(1000),
    ExecutionMethod_ID INT NOT NULL,
    COLUMN_ID INT NOT NULL,
    RUNTIMESEC int not null,
    status varchar(20) not null,
    CONSTRAINT fk_ExecCol_ExecMethod FOREIGN KEY (ExecutionMethod_ID) REFERENCES ExecutionMethodData (ID),
    CONSTRAINT fk_ExecCol_Col FOREIGN KEY (COLUMN_ID) REFERENCES AnonymisedColumnData (ID)
)
go

create table ExecutionMessageData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    MessageText VARCHAR(1000) not null,
    ExecutionColumn_ID int not null,
    CONSTRAINT fk_ExecutionMessage_ExecutionColumn FOREIGN KEY (ExecutionColumn_ID) REFERENCES ExecutionColumnData (ID)
)
go

INSERT INTO APP.SECURITYUSER(USERNAME, PASSWORD, NAME, SURNAME, ENABLED, ENCRYPTED) 
	VALUES('admin', '123456', 'Admin', 'Fellow', 'Y', 'N')
go

INSERT INTO APP.SECURITYUSERROLES(USER_ID, ROLE) 
	VALUES( 1, 'ROLE_ADMIN')
go
