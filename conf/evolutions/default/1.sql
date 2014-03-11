# Tags schema

# --- !Ups

CREATE SEQUENCE projects_id_seq;
CREATE TABLE projects (
    id integer NOT NULL DEFAULT nextval('projects_id_seq'),
    code varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    logoURL varchar(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE stories_id_seq;
CREATE TABLE stories (
    id integer NOT NULL DEFAULT nextval('stories_id_seq'),
    title varchar(255) NOT NULL,
    phase varchar(255),
    PRIMARY KEY (id)
);


# --- !Downs
DROP TABLE IF EXISTS stories;
DROP SEQUENCE IF EXISTS stories_id_seq;
DROP TABLE IF EXISTS projects;
DROP SEQUENCE IF EXISTS projects_id_seq;



