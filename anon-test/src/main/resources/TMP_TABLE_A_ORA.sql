CREATE TABLE TMP_TABLE_A  ( 
	ID         	NUMBER(13) NOT NULL primary key,
	ROLENAME   	VARCHAR2(50) NOT NULL,
	DESCRIPTION	VARCHAR2(50) NULL,
	MODIFIEDBY 	VARCHAR2(50) NOT NULL,
	MODIFIEDAT 	DATE NOT NULL
	);
INSERT INTO TMP_TABLE_A(ID, ROLENAME, DESCRIPTION, MODIFIEDBY, MODIFIEDAT)
  VALUES(3695, 'DB_M-LOGO-OP_C-VK_A-VK_E-VK_M-VK_M-WI_M-WI_N', NULL, 'n/a', TO_DATE('2013-06-17 15:26:16','YYYY-MM-DD HH24:MI:SS'));
INSERT INTO TMP_TABLE_A(ID, ROLENAME, DESCRIPTION, MODIFIEDBY, MODIFIEDAT)
  VALUES(3651, 'DB_M-RI_M', NULL, 'n/a', TO_DATE('2012-10-30 17:42:05','YYYY-MM-DD HH24:MI:SS'));
INSERT INTO TMP_TABLE_A(ID, ROLENAME, DESCRIPTION, MODIFIEDBY, MODIFIEDAT)
  VALUES(3653, 'OP_C-OP_U-OR_A-PORT', NULL, 'n/a', TO_DATE('2012-10-30 17:42:05','YYYY-MM-DD HH24:MI:SS'));
INSERT INTO TMP_TABLE_A(ID, ROLENAME, DESCRIPTION, MODIFIEDBY, MODIFIEDAT)
  VALUES(3655, 'AUDI-DB_M-LOGO-OP_C-PORT-REP_-REP_-RI_M-SCEN-SC...', NULL, 'n/a', TO_DATE('2012-10-30 17:42:05','YYYY-MM-DD HH24:MI:SS'));
INSERT INTO TMP_TABLE_A(ID, ROLENAME, DESCRIPTION, MODIFIEDBY, MODIFIEDAT)
  VALUES(3692, 'AUDI-BR_C-DB_M-LOGO-MR_U-OP_C-OP_R-OP_U-OR_A-PA...', NULL, 'n/a', TO_DATE('2013-05-28 19:35:21','YYYY-MM-DD HH24:MI:SS'));
