select * from ADB_IMPORT_STATUS
go

sp_help ADB_IMPORT_STATUS
go


-- create the LOOKUP TABLE
select ROWID=identity(10), SSY_SourceSystem
into TMP_SSYS
from (select distinct SSY_SourceSystem
from POSITION
union 
select distinct SSY_SourceSystem
from ADB_IMPORT_STATUS
) AAA
order by 2
go


select * from TMP_SSYS
go

drop table TMP_SSYS
go

-- crate the test table
select * 
into TMP_POSITION
from POSITION
go

select * from TMP_POSITION
go

drop table  TMP_POSITION
go



-- anonymise the TEST TABLE
update TMP_POSITION 
set SSY_SourceSystem = (
select s2.SSY_SourceSystem + '   ' + x.SSY_SourceSystem
from 
TMP_SSYS s2,
TMP_SSYS s1
where s1.SSY_SourceSystem = x.SSY_SourceSystem
and s2.ROWID= (s1.ROWID + 20) % (select count(*) from TMP_SSYS) + 1
)
from TMP_POSITION x
go

------------------------------
-- create the LOOKUP TABLE
select ROWID=identity(10), SSY_SourceSystem as orig, SSY_SourceSystem as reshuffled
into TMP_SSYS
from (select distinct SSY_SourceSystem
from POSITION
union 
select distinct SSY_SourceSystem
from ADB_IMPORT_STATUS
) AAA
order by 2
go
-- reshuffle the lookup table
update TMP_SSYS
set reshuffled = (select orig from TMP_SSYS a where a.ROWID = (x.ROWID + 20) % (select count(*) from TMP_SSYS) + 1)
from TMP_SSYS x
go

select * from TMP_SSYS
go
drop table TMP_SSYS
go

-- anonymise the target table
update TMP_POSITION 
set SSY_SourceSystem = (
select s.reshuffled
from 
TMP_SSYS s
where s.orig = x.SSY_SourceSystem
)
from TMP_POSITION x
go

select distinct SSY_SourceSystem
from TMP_POSITION


