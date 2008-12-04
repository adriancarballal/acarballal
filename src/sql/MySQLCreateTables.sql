-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

DROP TABLE Vote;
DROP TABLE VideoComment;
DROP TABLE UserComment;
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

--------------------------------- UserComment------------------------------------
CREATE TABLE UserComment (
	cmmtId BIGINT NOT NULL AUTO_INCREMENT,
	commented BIGINT NOT NULL,
	commentator BIGINT NOT NULL,
	cmmnt VARCHAR(300) NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT UserComment_PK PRIMARY KEY (cmmtId),
	CONSTRAINT UsrCommented_FK FOREIGN KEY (commented)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE,
	CONSTRAINT UsrCommentator_FK FOREIGN KEY (commentator)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
    TYPE = InnoDB;

CREATE INDEX UserCommentByUserCommented ON UserComment(commented);

--------------------------------- VideoComment------------------------------------
CREATE TABLE VideoComment (
	cmmtId BIGINT NOT NULL AUTO_INCREMENT,
	vidId BIGINT NOT NULL,
	commentator BIGINT NOT NULL,
	cmmnt VARCHAR(300) NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT VideoComment_PK PRIMARY KEY (cmmtId),
	CONSTRAINT VidCommented_FK FOREIGN KEY (vidId)
		REFERENCES Video(vidId) ON DELETE CASCADE,
	CONSTRAINT VidCommentator_FK FOREIGN KEY (commentator)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
    TYPE = InnoDB;
-- Faltan los indices para las busquedas.

------------------------------------ Vote ---------------------------------------
CREATE TABLE Vote (
	voteId BIGINT NOT NULL AUTO_INCREMENT,
	vidId BIGINT NOT NULL,
	voter BIGINT NOT NULL,
	vote TINYINT NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT Vote_PK PRIMARY KEY (voteId),
	CONSTRAINT VideoVoted_FK FOREIGN KEY (vidId)
		REFERENCES Video(vidId) ON DELETE CASCADE,
	CONSTRAINT Voter_FK FOREIGN KEY (voter)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
    TYPE = InnoDB;
