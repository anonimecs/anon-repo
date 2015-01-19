create table TMP_TABLE_A(
col1 varchar(50) not null,
col2 varchar(50) not null,
primary key(col1, col2)
);

insert into TMP_TABLE_A values ('AAA', 'AAA');
insert into TMP_TABLE_A values ('BBB', 'BBB');
insert into TMP_TABLE_A values ('CCC', 'CCC');
