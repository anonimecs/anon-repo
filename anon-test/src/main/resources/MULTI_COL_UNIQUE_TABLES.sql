create table TMP_TABLE_A(
COL1 varchar(50) not null,
COL2 varchar(50) not null,
constraint someUniqueConst unique(COL1, COL2)
);

create table TMP_TABLE_B(
COL1 varchar(50) not null unique
);

create table TMP_TABLE_C(
COL1 varchar(50) not null
);

create table TMP_TABLE_D(
COL1 varchar(50) not null,
COL2 varchar(50) not null,
PRIMARY KEY(COL1, COL2)
);

create table TMP_TABLE_E(
COL1 varchar(50) not null primary key
);

create table TMP_TABLE_F(
COL1 varchar(50)
);

