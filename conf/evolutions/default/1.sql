# Tags schema

# --- !Ups

CREATE SEQUENCE projects_id_seq;
CREATE TABLE projects (
    id integer NOT NULL DEFAULT nextval('projects_id_seq'),
    code varchar(255) NOT NULL UNIQUE,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    logoURL varchar(255),
    PRIMARY KEY (id)
);

CREATE SEQUENCE phases_id_seq;
CREATE TABLE phases (
    id integer NOT NULL DEFAULT nextval('phases_id_seq'),
    parent integer DEFAULT NULL,
    name varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (id),
    FOREIGN KEY (parent) REFERENCES phases(id) ON DELETE CASCADE
);

CREATE TABLE project_phases (
    project_id integer NOT NULL,
    phase_id integer NOT NULL,
    capacity integer NOT NULL DEFAULT -1,
    position integer NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (phase_id) REFERENCES phases(id) ON DELETE CASCADE,
    UNIQUE(project_id, phase_id)
);

CREATE SEQUENCE stories_id_seq;
CREATE TABLE stories (
    id integer NOT NULL DEFAULT nextval('stories_id_seq'),
    project_id integer NOT NULL,
    title varchar(255) NOT NULL,
    storyType varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    phase_id integer NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (phase_id) REFERENCES phases(id) ON DELETE CASCADE
);

CREATE TABLE story_phases (
    story_id integer NOT NULL,
    phase_id integer NOT NULL,
    FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    FOREIGN KEY (phase_id) REFERENCES phases(id) ON DELETE CASCADE,
    UNIQUE(story_id, phase_id)
);

insert into phases(name) values ('backlog');
insert into phases(name) values ('definition');
insert into phases(name) values ('development');
insert into phases(name) values ('QA');
insert into phases(name) values ('deployment');
insert into phases(name) values ('released');

insert into projects(code, name, description, logoURL) values('T-1','Test Project','A test project to see the functionality of the site','http://www.gravatar.com/avatar/b0c453d8de3950e1c5097f75ea6c5502?r=PG&s=256&default=identicon');
insert into project_phases(project_id, phase_id, capacity, position) values(1,1,-1,0);
insert into project_phases(project_id, phase_id, capacity, position) values(1,2,-1,1);
insert into project_phases(project_id, phase_id, capacity, position) values(1,3,-1,2);
insert into project_phases(project_id, phase_id, capacity, position) values(1,4,-1,3);
insert into project_phases(project_id, phase_id, capacity, position) values(1,5,-1,4);
insert into project_phases(project_id, phase_id, capacity, position) values(1,6,-1,5);
insert into stories(project_id, title, storyType, description) values(1, 'First Bug', 'bug','There is always a first bug');
insert into stories(project_id, title, storyType, description, phase_id) values(1, 'First Improvement', 'improvement','And something to improve', 2);
insert into stories(project_id, title, storyType, description, phase_id) values(1, 'Another Improvement', 'improvement','And something to improve', 1);
insert into stories(project_id, title, storyType, description, phase_id) values(1, 'Social network integration', 'feature','And of course now they want this',3);
insert into stories(project_id, title, storyType, description, phase_id) values(1, 'A task', 'task','The food of our minds',5);

# --- !Downs
DROP TABLE IF EXISTS story_phases;
DROP TABLE IF EXISTS project_phases;
DROP TABLE IF EXISTS phases;
DROP SEQUENCE IF EXISTS phases_id_seq;
DROP TABLE IF EXISTS stories;
DROP SEQUENCE IF EXISTS stories_id_seq;
DROP TABLE IF EXISTS projects;
DROP SEQUENCE IF EXISTS projects_id_seq;




