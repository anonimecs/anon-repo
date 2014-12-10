/*DROP FUNCTION IF EXISTS an_meth_enc_func;*/

CREATE FUNCTION an_meth_enc_func(
	inputStr TEXT, 
	shift_param DOUBLE
) 
RETURNS TEXT DETERMINISTIC
BEGIN
 DECLARE myposition DOUBLE DEFAULT 1;
 DECLARE outputStr TEXT DEFAULT '';
 DECLARE myindex DOUBLE DEFAULT 0;
 DECLARE shift DOUBLE DEFAULT shift_param;
 
 IF shift > 11 THEN SET shift = MOD(shift, 9) + 2;
 END IF;
 
 WHILE myposition <= LENGTH(inputStr) DO
    SET myindex = ASCII(SUBSTR(inputStr, myposition, 1)) + shift;
    
    IF myindex > 122 THEN SET myindex = myindex - shift;
    END IF;
    
    SET outputStr = CONCAT(outputStr, CHAR(myindex));
    SET myposition = myposition + 1;
 END WHILE;

 RETURN outputStr;

END;