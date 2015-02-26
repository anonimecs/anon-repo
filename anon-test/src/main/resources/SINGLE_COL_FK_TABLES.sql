create table TMP_TABLE_A3(
COL1 varchar(50) not null,
COL2 varchar(50) not null,
COL3 int not null,
primary key(COL1)
);

insert into TMP_TABLE_A3 values ('AAA', 'AAA', 1);
insert into TMP_TABLE_A3 values ('BBB', 'BBB', 2);
insert into TMP_TABLE_A3 values ('CCC', 'CCC', 3);

create table TMP_TABLE_B3(
COL1_REF varchar(50) not null,
COL2_REF varchar(50) not null,
COL3 int not null,
CONSTRAINT my_fk_3    FOREIGN KEY (COL1_REF)    REFERENCES TMP_TABLE_A3 (COL1) 
);

insert into TMP_TABLE_B3 values ('AAA', 'AAA', 1);
insert into TMP_TABLE_B3 values ('BBB', 'BBB', 2);
insert into TMP_TABLE_B3 values ('CCC', 'CCC', 3);
