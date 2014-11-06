/*
CREATE FUNCTION cs_meth_enc_func(
  @inputStr                     varchar(50),
  @shift                        int
) 
returns varchar(50)
As
  Begin
  
   DECLARE @inputStrLen INT
   DECLARE @position INT
   DECLARE @outputStr varchar(50)

   SET  @inputStrLen = len(@inputStr)
   SET @outputStr = null
   SET @position = 1
  
   while @position <= @inputStrLen
    begin

        SET @outputStr = @outputStr + char(ascii(substring(@inputStr, @position, 1)) + @shift)
        SET @position = @position + 1
    end
  
  return (@outputStr)
 END
go


CREATE PROCEDURE cs_meth_enc_tbl(
  @colName                     varchar(50),
  @tblName                     varchar(50),
  @shift                        int
) 
As
  Begin
  declare @dynsql varchar (150)

  set @dynsql = 'update ' + @tblName
  set @dynsql = @dynsql + ' set ' + @colName + ' = dbo.cs_meth_enc_func(' + @colName + ', '+ str(@shift) +')'
  exec (@dynsql)
 END
go
*/
/*
create table cs_test(username varchar(10))
go
insert into cs_test values ('holger')
insert into cs_test values ('dirk')
go

exec cs_meth_enc_tbl 'username', 'cs_test', 5
select * from cs_test
go
drop table cs_test
go
*/

select * 
into TMP_CS_POSITION
from POSITION
go

/*
exec cs_meth_enc_tbl 'SSY_SourceSystem', 'TMP_CS_POSITION', -5

*/

select distinct(SSY_SourceSystem) from TMP_CS_POSITION
go

drop table TMP_CS_POSITION
go

drop procedure cs_meth_enc_tbl
go
drop function cs_meth_enc_func
go



