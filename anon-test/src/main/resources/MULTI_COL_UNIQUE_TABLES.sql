create table TMP_TABLE_A(
COL1 varchar(50) not null,
COL2 varchar(50) not null,
constraint someUniqueConst unique(COL1, COL2)
);

insert into TMP_TABLE_A values ('AAA', 'AAA');
insert into TMP_TABLE_A values ('BBB', 'BBB');
insert into TMP_TABLE_A values ('CCC', 'CCC');

create table TMP_TABLE_B(
COL1_REF varchar(50) not null unique
);