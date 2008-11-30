-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));


DROP TABLE Video;
DROP TABLE UserProfile;


-- ------------------------------ UserProfile ----------------------------------

CREATE TABLE UserProfile (
	usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, email VARCHAR(60) NOT NULL,
	privileges TINYINT NOT NULL,
    version BIGINT,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
	CONSTRAINT LoginNameUniqueKey UNIQUE (loginName)) 
    TYPE = InnoDB;

CREATE INDEX UserProfileIndexByUsrId ON UserProfile (usrId);
CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

-- --------------------------------- Video -------------------------------------

CREATE TABLE Video (
	vidId BIGINT NOT NULL AUTO_INCREMENT,
	usrId BIGINT NOT NULL,
	title VARCHAR(50) NOT NULL,
	cmmnt VARCHAR(300) NOT NULL,
	urlshot VARCHAR(50) NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT Video_PK PRIMARY KEY (vidId),
	CONSTRAINT Video_FK FOREIGN KEY (usrId)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
    TYPE = InnoDB;

CREATE INDEX VideoIndexByVidId ON Video (vidId);
CREATE INDEX VideoIndexByUsrId ON Video (usrId);
-- Necesitariamos un indice para las busquedas por titulo y por fecha???