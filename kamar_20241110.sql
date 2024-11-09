CREATE TABLE public.kamar (
	id bigserial NOT NULL,
	tipekamar varchar NULL,
	status varchar NULL,
	tamu varchar NULL,
	CONSTRAINT kamar_pk PRIMARY KEY (id)
);

INSERT INTO public.kamar (id,tipekamar,status,tamu) VALUES
	 (3,'Kamar Single','Tersedia',NULL),
	 (4,'Kamar Single','Tersedia',NULL),
	 (6,'Kamar Standard','Tersedia',NULL),
	 (7,'Kamar Standard','Tersedia',NULL),
	 (8,'Kamar Standard','Tersedia',NULL),
	 (9,'Kamar Deluxe','Tersedia',NULL),
	 (10,'Kamar Deluxe','Tersedia',NULL),
	 (11,'Kamar Deluxe','Tersedia',NULL),
	 (12,'Kamar Luxury','Tersedia',NULL),
	 (2,'Kamar Single','Tersedia','');
INSERT INTO public.kamar (id,tipekamar,status,tamu) VALUES
	 (1,'Kamar Single','Terpesan','pandji'),
	 (5,'Kamar Standard','Terpesan','jiji'),
	 (13,'Kamar Luxury','Terpesan','jiji');
