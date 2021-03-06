/* DROP PROCEDURE IF EXISTS an_meth_enc_proc; */

CREATE PROCEDURE an_meth_enc_proc(
  colName        			VARCHAR(255),
  tblName               	VARCHAR(255),
  shift                  	DOUBLE,
  whereClause				VARCHAR(1000)
)
BEGIN
	
  DECLARE dynsql VARCHAR(2000);
  SET @dynsql = CONCAT("update ", tblName, " set ", colName," = an_meth_enc_func( ", colName, ",", shift, ")", whereClause );
  
  PREPARE query from @dynsql;
  EXECUTE query;
  DEALLOCATE PREPARE query;
END;
