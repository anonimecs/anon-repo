/*DROP PROCEDURE IF EXISTS an_meth_enc_proc; */

CREATE PROCEDURE an_meth_enc_proc(
  colName        			TEXT,
  tblName               	TEXT,
  shift                  	DOUBLE
)
BEGIN
	
  DECLARE dynsql TEXT;
  
  SET dynsql = CONCAT('update ', tblName);
  SET dynsql = CONCAT(dynsql, ' set ', colName, ' = an_meth_enc_func(', colName, ', ', shift,')');

  EXECUTE dynsql;
END;

