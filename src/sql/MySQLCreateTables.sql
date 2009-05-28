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
	privileges VARCHAR(40) NOT NULL,
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
	urlshot VARCHAR(200) NOT NULL,
	original VARCHAR(200) NOT NULL,
	urlflv VARCHAR(200) NOT NULL,
	url3gp VARCHAR(200) NOT NULL,
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
insert into userprofile values(1, 'admin','UDn64bBABog2A', 'admin', 'none', 'admin@gmail.com', 'ADMIN', 0);
-- Password for admin = admin
insert into userprofile values(2, 'adrian xxxxxxxx','YZsowua7bk/lU', 'adrian', 'carballal', 'adriancarballal@gmail.com', 'COMPETITOR', 0);
-- Password for user adrian = adrian
insert into userprofile values(3, 'voter','LMcPpWzd49C7g', 'voter', 'voter', 'voter@gmail.com', 'VOTER', 0);
-- Password for user voter = voter
--------------------------
--        Videos        --
--------------------------
insert into video values (1,1,"Trailer Agora", "Pelicula dirigida por Alejandro Amenabar y protagonizada por Rachel Weisz. Proximamente en cines.","http://adriancarballal.dynalias.com/container/hSGzJYRq/hSGzJYRq.jpg", "http://adriancarballal.dynalias.com/container/hSGzJYRq/Agora-Trailer.flv", "http://adriancarballal.dynalias.com/container/hSGzJYRq/hSGzJYRq.flv","http://adriancarballal.dynalias.com/container/hSGzJYRq/hSGzJYRq.3gp",CURRENT_TIMESTAMP);
insert into video values (2,1,"Niño Loco Aleman","Jugar a juegos de guerra a cierta edad provoca transtornos, este es un claro ejemplo","http://adriancarballal.dynalias.com/container/LiZgQwLa/LiZgQwLa.jpg","http://adriancarballal.dynalias.com/container/LiZgQwLa/Aleman_Zumbao.flv","http://adriancarballal.dynalias.com/container/LiZgQwLa/LiZgQwLa.flv","http://adriancarballal.dynalias.com/container/LiZgQwLa/LiZgQwLa.3gp",CURRENT_TIMESTAMP);
insert into video values (3,1,"Contigo no, bicho 2º parte","Carlos se tuerce un tobillo intentando imprecionar a una señorita. Este chico nunca aprende.","http://adriancarballal.dynalias.com/container/FjlYFVOH/FjlYFVOH.jpg","http://adriancarballal.dynalias.com/container/FjlYFVOH/Contigo_no__bicho_II_parte.flv","http://adriancarballal.dynalias.com/container/FjlYFVOH/FjlYFVOH.flv","http://adriancarballal.dynalias.com/container/FjlYFVOH/FjlYFVOH.3gp",CURRENT_TIMESTAMP);
insert into video values (4,1,"Contigo no, bicho","A todos alguna vez nos han dado calabazas, pero hay formas y formas de hacerlo. El pobre me da pena, la verdad","http://adriancarballal.dynalias.com/container/rvdprHNo/rvdprHNo.jpg","http://adriancarballal.dynalias.com/container/rvdprHNo/CONTIGO_NO_BICHO.flv","http://adriancarballal.dynalias.com/container/rvdprHNo/rvdprHNo.flv","http://adriancarballal.dynalias.com/container/rvdprHNo/rvdprHNo.3gp",CURRENT_TIMESTAMP);
insert into video values (5,1,"Etoo y Keita","Dos de los jugadores del Barcelona nos demuestran sus habilidades con el balon.", "http://adriancarballal.dynalias.com/container/iRjdKBtl/iRjdKBtl.jpg","http://adriancarballal.dynalias.com/container/iRjdKBtl/Etoo-Keita-Practice.flv","http://adriancarballal.dynalias.com/container/iRjdKBtl/iRjdKBtl.flv","http://adriancarballal.dynalias.com/container/iRjdKBtl/iRjdKBtl.3gp",CURRENT_TIMESTAMP);
insert into video values (6,1,"Gears of War 2 Trailer 1","El juego insignia de XBOX 360 llega con un nuevo titulo. No te pierdas estas imagenes","http://adriancarballal.dynalias.com/container/tXpZEaGk/tXpZEaGk.jpg","http://adriancarballal.dynalias.com/container/tXpZEaGk/Gears_Of_War_2.flv","http://adriancarballal.dynalias.com/container/tXpZEaGk/tXpZEaGk.flv","http://adriancarballal.dynalias.com/container/tXpZEaGk/tXpZEaGk.3gp",CURRENT_TIMESTAMP);
insert into video values (7,1,"Gears of War 2 Parte 2","El juego insignia de XBOX 360 llega con un nuevo titulo. No te pierdas estas imagenes. 2º parte","http://adriancarballal.dynalias.com/container/ZDEdDESo/ZDEdDESo.jpg","http://adriancarballal.dynalias.com/container/ZDEdDESo/Gears_of_War_2_Trailer.flv","http://adriancarballal.dynalias.com/container/ZDEdDESo/ZDEdDESo.flv","http://adriancarballal.dynalias.com/container/ZDEdDESo/ZDEdDESo.3gp",CURRENT_TIMESTAMP);
insert into video values (8,1,"Bebe sonriendo","Que monos son los crios cuando quieren, este no hace mas que reirse.","http://adriancarballal.dynalias.com/container/xyJPsyfi/xyJPsyfi.jpg","http://adriancarballal.dynalias.com/container/xyJPsyfi/Hahaha.flv","http://adriancarballal.dynalias.com/container/xyJPsyfi/xyJPsyfi.flv","http://adriancarballal.dynalias.com/container/xyJPsyfi/xyJPsyfi.3gp",CURRENT_TIMESTAMP);
insert into video values (9,1,"Push Tailer","Trailer de esta pelicula de accion que se estrena en Junio de 2009.", "http://adriancarballal.dynalias.com/container/ItrEYxjy/ItrEYxjy.jpg","http://adriancarballal.dynalias.com/container/ItrEYxjy/Push-TRAILER.flv","http://adriancarballal.dynalias.com/container/ItrEYxjy/ItrEYxjy.flv","http://adriancarballal.dynalias.com/container/ItrEYxjy/ItrEYxjy.3gp",CURRENT_TIMESTAMP);
insert into video values (10,1,"Terminator 4 Trailer","Terminator: Salvation, la nueva entrega de la saga Terminator vuelve, esta vez sin Arnold Swa..., bueno, el CHUACHE", "http://adriancarballal.dynalias.com/container/snhQUDHX/snhQUDHX.jpg","http://adriancarballal.dynalias.com/container/snhQUDHX/TERMINATOR-4-trailer.flv","http://adriancarballal.dynalias.com/container/snhQUDHX/snhQUDHX.flv","http://adriancarballal.dynalias.com/container/snhQUDHX/snhQUDHX.3gp",CURRENT_TIMESTAMP);
insert into video values (11,1,"Transformers 2 Trailer","LaBeuf y Fox regresan con los Transformers para intertan detener a los Decepticons. Proximamente en cines.","http://adriancarballal.dynalias.com/container/EsUsfPZn/EsUsfPZn.jpg","http://adriancarballal.dynalias.com/container/EsUsfPZn/Transformers2-trailer.flv","http://adriancarballal.dynalias.com/container/EsUsfPZn/EsUsfPZn.flv","http://adriancarballal.dynalias.com/container/EsUsfPZn/EsUsfPZn.3gp",CURRENT_TIMESTAMP);
insert into video values (12,2,"650 millones en 1 min","Mira como llegamos a la actualidad mirando la historia de nuestro planeta.","http://adriancarballal.dynalias.com/container/bgGwxKKy/bgGwxKKy.jpg","http://adriancarballal.dynalias.com/container/bgGwxKKy/650_millones_de_a_os_en_1_min.flv","http://adriancarballal.dynalias.com/container/bgGwxKKy/bgGwxKKy.flv","http://adriancarballal.dynalias.com/container/bgGwxKKy/bgGwxKKy.3gp",CURRENT_TIMESTAMP);
insert into video values (13,2,"Homer Evolution","Todo el mundo le conoce, le adora, pero nadie sabe como llego a ser lo que es,nosotros te lo enseñamos","http://adriancarballal.dynalias.com/container/nXUveLKL/nXUveLKL.jpg","http://adriancarballal.dynalias.com/container/nXUveLKL/Homer-Evolution.flv","http://adriancarballal.dynalias.com/container/nXUveLKL/nXUveLKL.flv","http://adriancarballal.dynalias.com/container/nXUveLKL/nXUveLKL.3gp",CURRENT_TIMESTAMP);
insert into video values (14,2,"Messi y Zidane","Los dos mejores jugadores de esta decada nos enseñan sus mejores toques.","http://adriancarballal.dynalias.com/container/lmBtogQy/lmBtogQy.jpg","http://adriancarballal.dynalias.com/container/lmBtogQy/Messi_y_Zidane_-_ADIDAS.flv","http://adriancarballal.dynalias.com/container/lmBtogQy/lmBtogQy.flv","http://adriancarballal.dynalias.com/container/lmBtogQy/lmBtogQy.3gp",CURRENT_TIMESTAMP);
--------------------------
--       Message        --
--------------------------
insert into message values (1,1,1,"Trailer Agora: Video subido con exito");
insert into message values (2,1,1,"Niño Loco Aleman: Video subido con exito");
insert into message values (3,1,1,"Contigo no, bicho 2º parte: Video subido con exito");
insert into message values (4,1,1,"Contigo no, bicho: Video subido con exito");
insert into message values (5,1,1,"Etoo y Keita: Video subido con exito");
insert into message values (6,1,1,"Gears of War 2 Trailer 1: Video subido con exito");
insert into message values (7,1,1,"Bebe sonriendo: Video subido con exito");
insert into message values (8,1,1,"Push Tailer: Video subido con exito");
insert into message values (9,1,1,"Terminator 4 Trailer: Video subido con exito");
insert into message values (10,1,1,"Transformers 2 Trailer: Video subido con exito");
insert into message values (11,2,1,"650 millones en 1 min: Video subido con exito");
insert into message values (12,2,1,"Homer Evolution: Video subido con exito");
insert into message values (13,2,1,"Messi y Zidane: Video subido con exito");
--------------------------
--         vote         --
--------------------------
insert into vote values(1,12,3,5,CURRENT_TIMESTAMP);
insert into vote values(2,12,1,4,CURRENT_TIMESTAMP);
insert into vote values(3,6,3,4,CURRENT_TIMESTAMP);
insert into vote values(4,6,2,3,CURRENT_TIMESTAMP);
insert into vote values(5,11,3,3,CURRENT_TIMESTAMP);
insert into vote values(6,11,1,2,CURRENT_TIMESTAMP);
insert into vote values(7,1,3,2,CURRENT_TIMESTAMP);
insert into vote values(8,1,2,1,CURRENT_TIMESTAMP);
