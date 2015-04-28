CREATE OR REPLACE PROCEDURE an_meth_enc_proc(
  colName                     varchar2,
  tblName                     varchar2,
  shift                        number,
  whereClause				  varchar2
) 
is
  dynsql varchar2(150);

Begin

  dynsql := 'update ' || tblName;
  dynsql := dynsql || ' set ' || colName || ' = an_meth_enc_func(' || colName || ', ' || to_char(shift) || ') ' || whereClause;

  --DBMS_OUTPUT.PUT_LINE(dynsql);

  EXECUTE IMMEDIATE  dynsql;
END;

