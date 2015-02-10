
create table TMP_TABLE_B(
col1_ref varchar(50) not null,
col2_ref varchar(50) not null,
CONSTRAINT my_fk    FOREIGN KEY (col1_ref, col2_ref)    REFERENCES TMP_TABLE_A (col1, col2) 
)
GO

insert into TMP_TABLE_B values ('AAA', 'AAA')
GO
insert into TMP_TABLE_B values ('BBB', 'BBB')
GO
insert into TMP_TABLE_B values ('CCC', 'CCC')
GO
--drop table TMP_TABLE_B

