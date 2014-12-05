CREATE FUNCTION dbo.an_meth_enc_func(
  @inputStr                     varchar(50),
  @shift                        int
) 
returns varchar(50)
As
  Begin
  
   DECLARE @inputStrLen INT
   DECLARE @position INT
   DECLARE @outputStr varchar(50)
   DECLARE @index INT

   SET  @inputStrLen = len(@inputStr)
   SET @outputStr = null
   SET @position = 1
  
   if @shift > 11
       SET @shift = @shift % 9 + 2

   while @position <= @inputStrLen
    begin
        SET @index = ascii(substring(@inputStr, @position, 1)) + @shift
        IF  @index > 122  
            SET @index = @index - @shift
        
        SET @outputStr = @outputStr + char(@index)
        SET @position = @position + 1
    end
  
  return (@outputStr)
 END




