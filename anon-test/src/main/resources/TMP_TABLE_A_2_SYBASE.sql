create table TMP_TABLE_A(
col1 varchar(50) not null,
col2 varchar(50) not null,
primary key(col1, col2)
)
GO

insert into TMP_TABLE_A values ('AAA', 'AAA')
GO
insert into TMP_TABLE_A values ('BBB', 'BBB')
GO
insert into TMP_TABLE_A values ('CCC', 'CCC')
GO
--drop table TMP_TABLE_A
