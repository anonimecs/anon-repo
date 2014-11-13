CREATE OR REPLACE FUNCTION an_meth_enc_func(
  inputStr                     varchar2,
  shift_param                  number
) 
return varchar2
is
  
   inputStrLen number;
   myposition number;
   outputStr varchar2(50);
   myindex number;
   shift number;

  
begin
   outputStr := null;
   myposition := 1;
   inputStrLen := length(inputStr);
   shift := shift_param;

   if shift > 11 then
       shift := mod(shift, 9) + 2;
   end if;

   while myposition <= inputStrLen
   loop
        myindex := ascii(SUBSTR(inputStr, myposition, 1)) + shift;
        if  myindex > 122  then
             myindex := myindex - shift;
        end if;

        outputStr := outputStr || chr(myindex);
        myposition := myposition + 1;
    end loop;
  
  return (outputStr);
 END;
 






