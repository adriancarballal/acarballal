-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

DROP TABLE Message;
DROP TABLE Favourite;
DROP TABLE UserCommentComplaint;
DROP TABLE VideoCommentComplaint;
DROP TABLE VideoComplaint;
DROP TABLE Vote;
DROP TABLE VideoComment;
DROP TABLE UserComment;
DROP TABLE Video;
DROP TABLE UserProfile;


-------------------------------- UserProfile ----------------------------------

CREATE TABLE UserProfile (
	usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(15) COLLATE latin1_bin NOT NULL,
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
	title VARCHAR(30) NOT NULL,
	cmmnt VARCHAR(200) NOT NULL,
	urlshot VARCHAR(100) NOT NULL,
	original VARCHAR(100) NOT NULL,
	urlflv VARCHAR(100) NOT NULL,
	url3gp VARCHAR(100) NOT NULL,
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
	cmmnt VARCHAR(400) NOT NULL,
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

------------------------------- VideoComplaint-----------------------------------

CREATE TABLE VideoComplaint (
	id BIGINT NOT NULL AUTO_INCREMENT,
	complainer BIGINT NOT NULL,
	reference BIGINT NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT VideoComplaint_PK PRIMARY KEY (id),
	CONSTRAINT ComplaintedVideo_FK FOREIGN KEY (reference)
		REFERENCES Video(vidId) ON DELETE CASCADE,
	CONSTRAINT Complainer_FK FOREIGN KEY (complainer)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
	TYPE = InnoDB;


---------------------------- VideoCommentComplaint--------------------------------

CREATE TABLE VideoCommentComplaint (
	id BIGINT NOT NULL AUTO_INCREMENT,
	complainer BIGINT NOT NULL,
	reference BIGINT NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT VideoCommentComplaint_PK PRIMARY KEY (id),
	CONSTRAINT ComplaintedVideoComment_FK FOREIGN KEY (reference)
		REFERENCES VideoComment(cmmtId) ON DELETE CASCADE,
	CONSTRAINT ComplainerVideoComment_FK FOREIGN KEY (complainer)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
	TYPE = InnoDB;


----------------------------- UserCommentComplaint--------------------------------

CREATE TABLE UserCommentComplaint (
	id BIGINT NOT NULL AUTO_INCREMENT,
	complainer BIGINT NOT NULL,
	reference BIGINT NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT UserCommentComplaint_PK PRIMARY KEY (id),
	CONSTRAINT ComplaintedUserComment_FK FOREIGN KEY (reference)
		REFERENCES UserComment(cmmtId) ON DELETE CASCADE,
	CONSTRAINT ComplainerUserComment_FK FOREIGN KEY (complainer)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
	TYPE = InnoDB;

CREATE TABLE Favourite (
	favouriteId BIGINT NOT NULL AUTO_INCREMENT,
	user BIGINT NOT NULL,
	favourite BIGINT NOT NULL,
	CONSTRAINT UserFavourite_PK PRIMARY KEY (favouriteId),
	CONSTRAINT FavouriteVideo_FK FOREIGN KEY (favourite)
		REFERENCES Video(vidId) ON DELETE CASCADE,
	CONSTRAINT UserFavourite_FK FOREIGN KEY (user)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE)
	TYPE = InnoDB;

CREATE TABLE Message (
	id BIGINT NOT NULL AUTO_INCREMENT,
    receiver BIGINT NOT NULL,
	sender BIGINT NOT NULL,
	text VARCHAR(300) NOT NULL,
	CONSTRAINT Message_PK PRIMARY KEY (id),
	CONSTRAINT ReceiverMessage_FK FOREIGN KEY (receiver)
		REFERENCES UserProfile(usrId) ON DELETE CASCADE,
	CONSTRAINT SenderMessage_FK FOREIGN KEY (sender)
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
insert into userprofile values(2, 'adrian xxxxxxxx','YZsowua7bk/lU', 'adrian', 'carballal', 'adriancarballal@gmail.com', 1, 0);
-- Password for user adrian = adrian
--insert into userprofile values(3, 'voter','LMcPpWzd49C7g', 'voter', 'voter', 'voter@gmail.com', 2, 0);
-- Password for user voter = voter

--	admin=0
--	competitor=1
--	none=2
--	voter=3
--------------------------
--        Videos        --
--------------------------
insert into video values (1,2,"Episodio Chuck 01 xxxxxxxxxxxx", "Parte 1 x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x y z", "http://adriancarballal.dynalias.com/imagenes/small/1.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/1.flv", "", CURRENT_TIMESTAMP);
insert into video values (2,2,"Episodio Chuck 01", "Parte 2", "http://adriancarballal.dynalias.com/imagenes/small/2.jpg", "", "http://192.168.0.11/videos_webService/2.flv", "", CURRENT_TIMESTAMP);
insert into video values (3,2,"Episodio Chuck 01", "Parte 3", "http://adriancarballal.dynalias.com/imagenes/small/3.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/3.flv", "", CURRENT_TIMESTAMP);
insert into video values (4,2,"Episodio Chuck 01", "Parte 4", "http://adriancarballal.dynalias.com/imagenes/small/4.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/4.flv", "", CURRENT_TIMESTAMP);
insert into video values (5,2,"Episodio Chuck 01", "Parte 5", "http://adriancarballal.dynalias.com/imagenes/small/5.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/5.flv", "", CURRENT_TIMESTAMP);
insert into video values (6,2,"Episodio Chuck 01", "Parte 6", "http://adriancarballal.dynalias.com/imagenes/small/6.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/6.flv", "", CURRENT_TIMESTAMP);
insert into video values (7,2,"Episodio Chuck 01", "Parte 7", "http://adriancarballal.dynalias.com/imagenes/small/7.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/7.flv", "", CURRENT_TIMESTAMP);
insert into video values (8,2,"Episodio Chuck 01", "Parte 8", "http://adriancarballal.dynalias.com/imagenes/small/8.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/8.flv", "", CURRENT_TIMESTAMP);
insert into video values (9,2,"Episodio Chuck 01", "Parte 9", "http://adriancarballal.dynalias.com/imagenes/small/9.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/9.flv", "", CURRENT_TIMESTAMP);
insert into video values (10,2,"Episodio Chuck 01", "Parte 10", "http://adriancarballal.dynalias.com/imagenes/small/10.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/10.flv", "", CURRENT_TIMESTAMP);
insert into video values (11,2,"Episodio Chuck 01", "Parte 11", "http://adriancarballal.dynalias.com/imagenes/small/11.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/11.flv", "", CURRENT_TIMESTAMP);
insert into video values (12,2,"Episodio Chuck 01", "Parte 12", "http://adriancarballal.dynalias.com/imagenes/small/12.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/12.flv", "", CURRENT_TIMESTAMP);
insert into video values (13,2,"Episodio Chuck 01", "Parte 13", "http://adriancarballal.dynalias.com/imagenes/small/13.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/13.flv", "", CURRENT_TIMESTAMP);
insert into video values (14,2,"Episodio Chuck 01", "Parte 14", "http://adriancarballal.dynalias.com/imagenes/small/14.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/14.flv", "", CURRENT_TIMESTAMP);
insert into video values (15,2,"Episodio Chuck 01", "Parte 15", "http://adriancarballal.dynalias.com/imagenes/small/15.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/15.flv", "", CURRENT_TIMESTAMP);
insert into video values (16,2,"Episodio Chuck 01", "Parte 16", "http://adriancarballal.dynalias.com/imagenes/small/16.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/16.flv", "", CURRENT_TIMESTAMP);
insert into video values (17,2,"Episodio Chuck 01", "Parte 17", "http://adriancarballal.dynalias.com/imagenes/small/17.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/17.flv", "", CURRENT_TIMESTAMP);
insert into video values (18,2,"Episodio Chuck 01", "Parte 18", "http://adriancarballal.dynalias.com/imagenes/small/18.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/18.flv", "", CURRENT_TIMESTAMP);
insert into video values (19,2,"Episodio Chuck 01", "Parte 19", "http://adriancarballal.dynalias.com/imagenes/small/19.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/19.flv", "", CURRENT_TIMESTAMP);
insert into video values (20,2,"Episodio Chuck 01", "Parte 20", "http://adriancarballal.dynalias.com/imagenes/small/20.jpg", "", "http://adriancarballal.dynalias.com/videos_webService/20.flv", "", CURRENT_TIMESTAMP);

--------------------------
--     UserComments     --
--------------------------
 
--insert into usercomment values (1,2,1,"x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x ", CURRENT_TIMESTAMP);
--insert into usercomment values (2,2,1,"comment 2", CURRENT_TIMESTAMP);
--insert into usercomment values (3,2,1,"x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x ", CURRENT_TIMESTAMP);
--insert into usercomment values (4,2,1,"x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x ", CURRENT_TIMESTAMP);
--insert into usercomment values (5,2,1,"x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x ", CURRENT_TIMESTAMP);
--insert into usercomment values (6,2,1,"comment 6", CURRENT_TIMESTAMP);
--insert into usercomment values (7,2,1,"x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x x ", CURRENT_TIMESTAMP);
--insert into usercomment values (8,2,1,"comment 8", CURRENT_TIMESTAMP);
--insert into usercomment values (9,2,1,"comment 9", CURRENT_TIMESTAMP);
--insert into usercomment values (10,2,1,"comment 10", CURRENT_TIMESTAMP);
--insert into usercomment values (11,2,1,"comment 11", CURRENT_TIMESTAMP);
--insert into usercomment values (12,2,1,"comment 12", CURRENT_TIMESTAMP);
--insert into usercomment values (13,2,1,"comment 13", CURRENT_TIMESTAMP);
--insert into usercomment values (14,2,1,"comment 14", CURRENT_TIMESTAMP);
--insert into usercomment values (15,2,1,"comment 15", CURRENT_TIMESTAMP);
--insert into usercomment values (16,1,2,"comment 16", CURRENT_TIMESTAMP);
--insert into usercomment values (17,1,2,"comment 17", CURRENT_TIMESTAMP);
--insert into usercomment values (18,1,2,"comment 18", CURRENT_TIMESTAMP);
--insert into usercomment values (19,1,2,"comment 19", CURRENT_TIMESTAMP);
--insert into usercomment values (20,2,2,"comment 20", CURRENT_TIMESTAMP);