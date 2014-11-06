
CREATE PROCEDURE cs_meth_enc(
  @inputStr                     varchar(50),
  @outputStr                    varchar(20) Output,
  @shift                        int
) 
As
  Begin
  
   DECLARE @inputStrLen INT
   DECLARE @position INT

   SET  @inputStrLen = len(@inputStr)
   SET @outputStr = ''
   SET @position = 1
  
   while @position < @inputStrLen
    begin

        SET @outputStr = @outputStr + char(ascii(substring(@inputStr, @position, 1)) + @shift)
        SET @position = @position + 1
    end
  

    RETURN  0
 END
go

declare @res varchar(50)
exec cs_meth_enc 'csaba', @res output, 5
select @res
go

drop procedure cs_meth_enc
go



