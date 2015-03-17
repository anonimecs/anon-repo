CONNECT 'jdbc:derby://localhost:1527/anon;user=app;password=anon;';
SET SCHEMA APP;

ALTER TABLE AnonymisedColumnData DROP CONSTRAINT unique_col_anonymisation;
ALTER TABLE SecurityUserRoles DROP CONSTRAINT foreign_key_SecurityUser;
ALTER TABLE AnonymisationMethodData DROP CONSTRAINT fk_AnonymisationMethodData_DatabaseConfig;
ALTER TABLE AnonymisedColumnData DROP CONSTRAINT fk_AnonymisedColumnData_AnonymisationMethodData;
ALTER TABLE MappingRuleData DROP CONSTRAINT fk_MappingRuleData_AnonymisationMethodData;
ALTER TABLE MappingDefaultData DROP CONSTRAINT fk_MappingDefaultData_AnonymisationMethodData;
ALTER TABLE ExecutionMethodData DROP CONSTRAINT fk_ExecutionMethod_Execution;
ALTER TABLE ExecutionMethodData DROP CONSTRAINT fk_ExecutionMethod_Method;
ALTER TABLE ExecutionColumnData DROP CONSTRAINT fk_ExecCol_ExecMethod;
ALTER TABLE ExecutionColumnData DROP CONSTRAINT fk_ExecCol_Col;
ALTER TABLE ExecutionMessageData DROP CONSTRAINT fk_ExecutionMessage_ExecutionColumn;
ALTER TABLE User_Database DROP CONSTRAINT fk_DatabaseConfig_pk;
ALTER TABLE User_Database DROP CONSTRAINT fk_SecurityUser_pk;

drop table User_Database;
drop table ExecutionMessageData;
drop table ExecutionColumnData;
drop table ExecutionMethodData;
drop table ExecutionData;
drop table SecurityUserRoles;
drop table SecurityUser;
drop table AnonymisedColumnData;
drop table MappingRuleData;
drop table MappingDefaultData;
drop table AnonymisationMethodData; 
drop table DatabaseConfig;

