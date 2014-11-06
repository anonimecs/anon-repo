
CREATE PROCEDURE cs_meth_enc(
  @inputStr                     varchar(50),
  @shift                        int
) 
As
  Begin
  
   DECLARE @inputStrLen INT
   DECLARE @position INT
   DECLARE @outputStr varchar(50)

   SET  @inputStrLen = len(@inputStr)
   SET @outputStr = ''
   SET @position = 1
  
   while @position < @inputStrLen
    begin

        SET @outputStr = @outputStr + char(ascii(substring(@inputStr, @position, 1)) + @shift)
        SET @position = @position + 1
    end
  
 END
go

exec cs_meth_enc 'csaba', 5
go

drop procedure cs_meth_enc
go



