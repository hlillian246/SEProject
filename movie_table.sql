Create table movies(
	id varchar(10) not null, 
	title varchar(100) not null, 
	year int not null,
 	director varchar(100) Not null, 
 	revenue int default 0 null,
	rating float not null, 
	numVotes int not null,
 	overview varchar(8192) null,
 	PRIMARY KEY(id)
 );
