CONNECT 'jdbc:derby://localhost:1527/anon;user=app;password=anon;create=true';
SET SCHEMA APP;

CREATE TABLE SecurityUser (
  ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  USERNAME VARCHAR(255) NOT NULL,
  PASSWORD VARCHAR(255) NOT NULL,
  NAME VARCHAR(255),
  SURNAME VARCHAR(255),
  ENABLED VARCHAR(1) NOT NULL,
  ENCRYPTED VARCHAR(1) DEFAULT NULL,
  CONSTRAINT primary_key_SecurityUser PRIMARY KEY (ID)
);

CREATE TABLE SecurityUserRoles (
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	USER_ID INT NOT NULL,
	ROLE VARCHAR(255) NOT NULL,
	CONSTRAINT primary_key_SecurityUserRoles PRIMARY KEY (ID),
	CONSTRAINT foreign_key_SecurityUser FOREIGN KEY (USER_ID) REFERENCES SecurityUser (ID)
);

CREATE TABLE DatabaseConnection (	
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	URL VARCHAR(255) NOT NULL,
  	LOGIN VARCHAR(255) NOT NULL,
  	PASSWORD VARCHAR(255) NOT NULL,
  	VENDOR VARCHAR(255) NOT NULL,
  	VERSION VARCHAR(255),
    GUINAME VARCHAR(255) not null,
   	SecurityUser_ID INT NOT NULL,
	CONSTRAINT fk_SecurityUser_pk FOREIGN KEY (SecurityUser_ID) REFERENCES SecurityUser (ID),
  	CONSTRAINT primary_key_DatabaseConnection PRIMARY KEY (ID),
  	CONSTRAINT DatabaseConnection_UC UNIQUE (GUINAME, SecurityUser_ID)
);

CREATE TABLE DatabaseConfig (	
	ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  	DEFAULTSCHEMA VARCHAR(255) NOT NULL,
    CONFIGURATIONNAME VARCHAR(255) not null,
    DatabaseConnection_ID INT NOT NULL,
   	SecurityUser_ID INT NOT NULL,
	CONSTRAINT fk_SecurityUser_pk2 FOREIGN KEY (SecurityUser_ID) REFERENCES SecurityUser (ID),
  	CONSTRAINT primary_key_DATABASECONFIG PRIMARY KEY (ID),
    CONSTRAINT fk_DatabaseConfig_DatabaseConnection FOREIGN KEY (DatabaseConnection_ID) REFERENCES DatabaseConnection (ID),
  	CONSTRAINT DatabaseConfig_UC UNIQUE (CONFIGURATIONNAME, SecurityUser_ID)
);

create table AnonymisationMethodData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    DATABASECONFIG_ID INT not null,
    PASSWORD VARCHAR(255),
    anonymizationType VARCHAR(30) NOT NULL,
    anonMethodClass VARCHAR(255) NOT NULL,
    DTYPE VARCHAR(255),
  	CONSTRAINT primary_key_AnonymisationMethodData PRIMARY KEY (ID),
    CONSTRAINT fk_AnonymisationMethodData_DatabaseConfig FOREIGN KEY (DATABASECONFIG_ID) REFERENCES DatabaseConfig (ID)
);

create table AnonymisedColumnData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    COLUMNNAME VARCHAR(255) NOT NULL,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
    COLUMNTYPE VARCHAR(255) NOT NULL,
    WHERECONDITION VARCHAR(2000),
    GUIFIELDWHERECONDITION VARCHAR(2000),
    GUIFIELDAPPLICABILITY VARCHAR(20),    
    AnonymisationMethodData_ID int,
  	CONSTRAINT primary_key_AnonymisedColumnData PRIMARY KEY (ID),
    CONSTRAINT fk_AnonymisedColumnData_AnonymisationMethodData FOREIGN KEY (AnonymisationMethodData_ID) REFERENCES AnonymisationMethodData (ID) ON DELETE CASCADE

);

ALTER TABLE AnonymisedColumnData
	ADD CONSTRAINT unique_col_anonymisation
	 UNIQUE (COLUMNNAME, TABLENAME, SCHEMANAME, AnonymisationMethodData_ID);

CREATE TABLE MappingRuleData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    MAPPINGRULETYPE VARCHAR(30) NOT NULL,
    BOUNDARY VARCHAR(100),
    MAPPEDVALUE VARCHAR(100),
    AnonymisationMethodData_ID int,
  	CONSTRAINT primary_key_MappingRuleData PRIMARY KEY (ID),
    CONSTRAINT fk_MappingRuleData_AnonymisationMethodData FOREIGN KEY (AnonymisationMethodData_ID) REFERENCES AnonymisationMethodData (ID) ON DELETE CASCADE
    
);

CREATE TABLE MappingDefaultData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    AnonymisationMethodData_ID int,
    DEFAULTVALUE VARCHAR(100),
  	CONSTRAINT primary_key_MappingDefaultData PRIMARY KEY (ID),
    CONSTRAINT fk_MappingDefaultData_AnonymisationMethodData FOREIGN KEY (AnonymisationMethodData_ID) REFERENCES AnonymisationMethodData (ID) ON DELETE CASCADE
);





create table ExecutionData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    DATABASECONFIG_ID INT not null,
    description VARCHAR(1000),
	userName VARCHAR(50) NOT NULL,
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    status varchar(20) not null,
    CONSTRAINT fk_ExecutionData_DatabaseConfig FOREIGN KEY (DATABASECONFIG_ID) REFERENCES DatabaseConfig (ID)
);

create table ExecutionMethodData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    RESULTTEXT VARCHAR(1000),
    EXECUTION_ID INT NOT NULL,
    METHOD_ID INT NOT NULL,
    RUNTIMESEC int not null,
    status varchar(20) not null,
    CONSTRAINT fk_ExecutionMethod_Execution FOREIGN KEY (EXECUTION_ID) REFERENCES ExecutionData (ID),
    CONSTRAINT fk_ExecutionMethod_Method FOREIGN KEY (METHOD_ID) REFERENCES AnonymisationMethodData (ID)
);

create table ExecutionColumnData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    RESULTTEXT VARCHAR(1000),
    ExecutionMethod_ID INT NOT NULL,
    COLUMN_ID INT NOT NULL,
    RUNTIMESEC int not null,
    status varchar(20) not null,
    CONSTRAINT fk_ExecCol_ExecMethod FOREIGN KEY (ExecutionMethod_ID) REFERENCES ExecutionMethodData (ID),
    CONSTRAINT fk_ExecCol_Col FOREIGN KEY (COLUMN_ID) REFERENCES AnonymisedColumnData (ID)
);

create table ExecutionMessageData(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY primary key,
    MessageText VARCHAR(1000) not null,
    ExecutionColumn_ID int not null,
    CONSTRAINT fk_ExecutionMessage_ExecutionColumn FOREIGN KEY (ExecutionColumn_ID) REFERENCES ExecutionColumnData (ID)
);


create table ReductionMethodData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    DATABASECONFIG_ID INT not null,
    ReductionType VARCHAR(30) NOT NULL,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
    WHERECONDITION VARCHAR(2000),
    CONSTRAINT primary_key_ReductionMethodData PRIMARY KEY (ID),
    CONSTRAINT fk_ReductionMethodData_DatabaseConfig FOREIGN KEY (DATABASECONFIG_ID) REFERENCES DatabaseConfig (ID)
);

create table ReductionMethodReferencingTableData (
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    ReductionMethodData_ID INT not null,
    ReductionType VARCHAR(30) NOT NULL,
    TABLENAME VARCHAR(255) NOT NULL,
    SCHEMANAME VARCHAR(255) NOT NULL,
    WHERECONDITION VARCHAR(2000),
  	CONSTRAINT primary_key_ReductionMethodReferencingTableData PRIMARY KEY (ID),
    CONSTRAINT fk_ReductionMethodReferencingTableData_ReductionMethodData FOREIGN KEY (ReductionMethodData_ID) REFERENCES ReductionMethodData (ID)
);

INSERT INTO APP.SECURITYUSER(USERNAME, PASSWORD, NAME, SURNAME, ENABLED, ENCRYPTED) 
	VALUES('admin', '$2a$15$PJDiuHrRUEogTwSfNHYjnudDkSgc.WqkGrowOMND3NVgrJTRAIg1e', 'BuiltIn', 'User', 'Y', 'Y');

INSERT INTO APP.SECURITYUSERROLES(USER_ID, ROLE) 
	VALUES( 1, 'ROLE_ADMIN');
