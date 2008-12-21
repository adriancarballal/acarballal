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
	original VARCHAR(50) NOT NULL,
	urlflv VARCHAR(50) NOT NULL,
	urlmp4 VARCHAR(50) NOT NULL,
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

--------------------------
-- Data for experiments --
--------------------------

--------------------------
--         Users        --
--------------------------
insert into userprofile values(1, 'admin','UDn64bBABog2A', 'admin', 'none', 'admin@gmail.com', 0, 0);
-- Password for admin = admin
insert into userprofile values(2, 'adrian','YZsowua7bk/lU', 'adrian', 'carballal', 'adriancarballal@gmail.com', 1, 0);
-- Password for user adrian = adrian

--------------------------
--        Videos        --
--------------------------
insert into video values (1,2,"Episodio Chuck 01", "Parte 1", "C:\\elmas_data\\snapshots\\small\\1.jpg", "", "C:\\elmas_data\\flvs\1.flv", "", CURRENT_TIMESTAMP);
insert into video values (2,2,"Episodio Chuck 01", "Parte 2", "C:\\elmas_data\\snapshots\\small\\2.jpg", "", "C:\\elmas_data\\flvs\2.flv", "", CURRENT_TIMESTAMP);
insert into video values (3,2,"Episodio Chuck 01", "Parte 3", "C:\\elmas_data\\snapshots\\small\\3.jpg", "", "C:\\elmas_data\\flvs\3.flv", "", CURRENT_TIMESTAMP);
insert into video values (4,2,"Episodio Chuck 01", "Parte 4", "C:\\elmas_data\\snapshots\\small\\4.jpg", "", "C:\\elmas_data\\flvs\4.flv", "", CURRENT_TIMESTAMP);
insert into video values (5,2,"Episodio Chuck 01", "Parte 5", "C:\\elmas_data\\snapshots\\small\\5.jpg", "", "C:\\elmas_data\\flvs\5.flv", "", CURRENT_TIMESTAMP);
insert into video values (6,2,"Episodio Chuck 01", "Parte 6", "C:\\elmas_data\\snapshots\\small\\6.jpg", "", "C:\\elmas_data\\flvs\6.flv", "", CURRENT_TIMESTAMP);
insert into video values (7,2,"Episodio Chuck 01", "Parte 7", "C:\\elmas_data\\snapshots\\small\\7.jpg", "", "C:\\elmas_data\\flvs\7.flv", "", CURRENT_TIMESTAMP);
insert into video values (8,2,"Episodio Chuck 01", "Parte 8", "C:\\elmas_data\\snapshots\\small\\8.jpg", "", "C:\\elmas_data\\flvs\8.flv", "", CURRENT_TIMESTAMP);
insert into video values (9,2,"Episodio Chuck 01", "Parte 9", "C:\\elmas_data\\snapshots\\small\\9.jpg", "", "C:\\elmas_data\\flvs\9.flv", "", CURRENT_TIMESTAMP);
insert into video values (10,2,"Episodio Chuck 01", "Parte 10", "C:\\elmas_data\\snapshots\\small\\10.jpg", "", "C:\\elmas_data\\flvs\10.flv", "", CURRENT_TIMESTAMP);
insert into video values (11,2,"Episodio Chuck 01", "Parte 11", "C:\\elmas_data\\snapshots\\small\\11.jpg", "", "C:\\elmas_data\\flvs\11.flv", "", CURRENT_TIMESTAMP);
insert into video values (12,2,"Episodio Chuck 01", "Parte 12", "C:\\elmas_data\\snapshots\\small\\12.jpg", "", "C:\\elmas_data\\flvs\12.flv", "", CURRENT_TIMESTAMP);
insert into video values (13,2,"Episodio Chuck 01", "Parte 13", "C:\\elmas_data\\snapshots\\small\\13.jpg", "", "C:\\elmas_data\\flvs\13.flv", "", CURRENT_TIMESTAMP);
insert into video values (14,2,"Episodio Chuck 01", "Parte 14", "C:\\elmas_data\\snapshots\\small\\14.jpg", "", "C:\\elmas_data\\flvs\14.flv", "", CURRENT_TIMESTAMP);
insert into video values (15,2,"Episodio Chuck 01", "Parte 15", "C:\\elmas_data\\snapshots\\small\\15.jpg", "", "C:\\elmas_data\\flvs\15.flv", "", CURRENT_TIMESTAMP);
insert into video values (16,2,"Episodio Chuck 01", "Parte 16", "C:\\elmas_data\\snapshots\\small\\16.jpg", "", "C:\\elmas_data\\flvs\16.flv", "", CURRENT_TIMESTAMP);
insert into video values (17,2,"Episodio Chuck 01", "Parte 17", "C:\\elmas_data\\snapshots\\small\\17.jpg", "", "C:\\elmas_data\\flvs\17.flv", "", CURRENT_TIMESTAMP);
insert into video values (18,2,"Episodio Chuck 01", "Parte 18", "C:\\elmas_data\\snapshots\\small\\18.jpg", "", "C:\\elmas_data\\flvs\18.flv", "", CURRENT_TIMESTAMP);
insert into video values (19,2,"Episodio Chuck 01", "Parte 19", "C:\\elmas_data\\snapshots\\small\\19.jpg", "", "C:\\elmas_data\\flvs\19.flv", "", CURRENT_TIMESTAMP);
insert into video values (20,2,"Episodio Chuck 01", "Parte 20", "C:\\elmas_data\\snapshots\\small\\20.jpg", "", "C:\\elmas_data\\flvs\20.flv", "", CURRENT_TIMESTAMP);

--------------------------
--     UserComments     --
--------------------------
insert into usercomment values (1,2,1,"comment 1", CURRENT_TIMESTAMP);
insert into usercomment values (2,2,1,"comment 2", CURRENT_TIMESTAMP);
insert into usercomment values (3,2,1,"comment 3", CURRENT_TIMESTAMP);
insert into usercomment values (4,2,1,"comment 4", CURRENT_TIMESTAMP);
insert into usercomment values (5,2,1,"comment 5", CURRENT_TIMESTAMP);
insert into usercomment values (6,2,1,"comment 6", CURRENT_TIMESTAMP);
insert into usercomment values (7,2,1,"comment 7", CURRENT_TIMESTAMP);
insert into usercomment values (8,2,1,"comment 8", CURRENT_TIMESTAMP);
insert into usercomment values (9,2,1,"comment 9", CURRENT_TIMESTAMP);
insert into usercomment values (10,2,1,"comment 10", CURRENT_TIMESTAMP);
insert into usercomment values (11,2,1,"comment 11", CURRENT_TIMESTAMP);
insert into usercomment values (12,2,1,"comment 12", CURRENT_TIMESTAMP);
insert into usercomment values (13,2,1,"comment 13", CURRENT_TIMESTAMP);
insert into usercomment values (14,2,1,"comment 14", CURRENT_TIMESTAMP);
insert into usercomment values (15,2,1,"comment 15", CURRENT_TIMESTAMP);
insert into usercomment values (16,2,1,"comment 16", CURRENT_TIMESTAMP);
insert into usercomment values (17,2,1,"comment 17", CURRENT_TIMESTAMP);
insert into usercomment values (18,2,1,"comment 18", CURRENT_TIMESTAMP);
insert into usercomment values (19,2,1,"comment 19", CURRENT_TIMESTAMP);
insert into usercomment values (20,2,1,"comment 20", CURRENT_TIMESTAMP);