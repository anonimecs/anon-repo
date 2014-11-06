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
