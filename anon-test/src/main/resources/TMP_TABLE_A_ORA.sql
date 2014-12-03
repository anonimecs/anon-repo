--drop table TMP_TABLE_A
;
CREATE TABLE TMP_TABLE_A  ( 
	ID        	NUMBER(13) NOT NULL,
	COMSIID   	VARCHAR2(50) NOT NULL,
	LASTNAME  	VARCHAR2(50) NOT NULL,
	FIRSTNAME 	VARCHAR2(50) NOT NULL,
	EMAIL     	VARCHAR2(100) NOT NULL,
	VALIDTO   	DATE NULL,
	MODIFIEDBY	VARCHAR2(50) NULL,
	MODIFIEDAT	DATE NULL,
	ROLE_ID   	NUMBER(13) NULL 
	);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(59581, 'cb2jape', 'Japec', 'Marsel', 'marsel.japec@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3695);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(68491, 'cb2br7x', 'Bretschneider', 'Andreas', 'andreas.bretschneider@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3695);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(68111, 'cb2habk', 'Habke', 'Julia', 'julia.habke@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(68492, 'cb3barl', 'Barleben', 'Thomas', 'thomas.barleben@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57430, 'cb2javo', 'Javorovic', 'Sonja', 'sonja.javorovic@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3653);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57431, 'cb2kryz', 'Kryzhanyuk', 'Viktoria', 'viktoria.kryzhanyuk@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57432, 'cb2raoi', 'Radoi', 'Mihaela Cristina', 'michaelachristina.radoi@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3655);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57433, 'cb2xxhp', 'Moser', 'Dorothea', 'dorothea.moser@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3655);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(68490, 'cb2wehs', 'Wehnes', 'Dirk', 'dirk.wehnes@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3695);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57435, 'cb3flor', 'Florin', 'Dorit', 'dorit.florin@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57436, 'cb3muea', 'Krupp', 'Monika', 'monika.krupp@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57437, 'cb3roeh', 'Röhm', 'Stefan', 'stefan.roehm@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3655);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57438, 'cb3tres', 'Tresch', 'Ralf', 'ralf.tresch@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57440, 'eh2kul1', 'Kumar', 'Amit', 'amit.kumar2@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57441, 'eh2mesz', 'Meszaros', 'Csaba', 'csaba.meszaros@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57449, 'cb1b717', 'Ölschläger', 'Carmen', 'carmen.oelschlaeger@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3651);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57450, 'cb2clah', 'Classen', 'Hans Christoph', 'hans-christoph.classen@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3655);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57451, 'cb2klg6', 'Klein', 'Alexander', 'alexander.klein@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57452, 'cb2muu2', 'Murr', 'Franz-Josef', 'franz-josef.murr@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3695);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57428, 'cb14877', 'Hoehne', 'Stephan', 'stephan.hoehne@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3651);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57429, 'cb1s500', 'Frank', 'Axel', 'axel.frank@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:08','YYYY-MM-DD HH24:MI:SS'), 3695);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57453, 'cb2wi8v', 'Winands', 'Michael', 'michael.winands@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57455, 'cb3bayt', 'Bernard', 'Özlem', 'oezlem.bernard@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57456, 'cb3kras', 'Kruse', 'Andreas', 'andreas.kruse@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57457, 'cb3musc', 'Muschiol', 'Andrea', 'andrea.muschiol@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57458, 'cb3thok', 'Thomas', 'Dirk', 'dirk.thomas@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3655);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57459, 'cb3wilr', 'Wilhelm', 'Günter', 'guenter.wilhelm@commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57461, 'eh2gpal', 'Gaur', 'Sukhpal', 'sukhpal.gaur@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57462, 'eh2luwm', 'Luca', 'Mihail Sergin', 'mihailsergin.luca@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57463, 'eh2oost', 'Oostendorp', 'Volker', 'volker.oostendorp@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
INSERT INTO TMP_TABLE_A(ID, COMSIID, LASTNAME, FIRSTNAME, EMAIL, VALIDTO, MODIFIEDBY, MODIFIEDAT, ROLE_ID)
  VALUES(57465, 'eh2sal2', 'Saini', 'Rahul', 'rahul.saini@partner.commerzbank.com', NULL, 'replicator', TO_DATE('2013-07-05 13:46:09','YYYY-MM-DD HH24:MI:SS'), 3692);
