CREATE TABLE public."user" (
	id bigserial NOT NULL,
	username varchar NOT NULL,
	"password" varchar NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_un UNIQUE (username)
);

INSERT INTO public."user" (id,username,"password") VALUES
	 (1,'admin','admin'),
	 (3,'pandji','pandji'),
	 (5,'jiji','jiji');
