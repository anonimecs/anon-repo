create table TMP_TABLE_A(
COL1 varchar(50) not null,
COL2 varchar(50) not null,
primary key(COL1, COL2)
);

insert into TMP_TABLE_A values ('AAA', 'AAA');
insert into TMP_TABLE_A values ('BBB', 'BBB');
insert into TMP_TABLE_A values ('CCC', 'CCC');

create table TMP_TABLE_B(
COL1_REF varchar(50) not null,
COL2_REF varchar(50) not null,
CONSTRAINT my_fk    FOREIGN KEY (COL1_REF, COL2_REF)    REFERENCES TMP_TABLE_A (COL1, COL2) 
);

insert into TMP_TABLE_B values ('AAA', 'AAA');
insert into TMP_TABLE_B values ('BBB', 'BBB');
insert into TMP_TABLE_B values ('CCC', 'CCC');

create table TMP_TABLE_C(
COL1 varchar(50) not null,
primary key(COL1)
);

create table TMP_TABLE_D(
COL1_REF varchar(50) not null,
CONSTRAINT my_fk2    FOREIGN KEY (COL1_REF)    REFERENCES TMP_TABLE_C (COL1) 
);

create table TMP_TABLE_E(
COL1 varchar(50) not null,
primary key(COL1)
);
