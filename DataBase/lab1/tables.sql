CREATE TYPE status AS ENUM (
    'открыта',
    'закрыта'
);

CREATE TYPE assessments AS ENUM (
    'катастрофическая',
    'идеальная',
    'хорошая',
    'удовлетворительная'
);

CREATE TYPE sizes AS ENUM (
    'огромная',
    'большая',
    'средняя',
    'маленькая'
);

CREATE TABLE Room (
    room_id SERIAL PRIMARY KEY,
    room_name VARCHAR(24) NOT NULL,
    room_status status
);


CREATE TABLE Device (
    device_id SERIAL PRIMARY KEY,
    device_name VARCHAR(24) NOT NULL,
    device_status BOOLEAN,
    room_id INTEGER REFERENCES Room(room_id)
);

CREATE TABLE Planet (
    planet_id SERIAL PRIMARY KEY,
    planet_name VARCHAR(24) NOT NULL,
    color VARCHAR(32) NOT NULL,
    planet_size sizes
);

CREATE TABLE Interaction (
    interaction_id SERIAL PRIMARY KEY,
    interaction_name VARCHAR(64) NOT NULL,
    interaction_description TEXT NOT NULL
);

CREATE TABLE Behaviour (
    behaviour_id SERIAL PRIMARY KEY,
    behaviour_name VARCHAR(32) NOT NULL,
    behaviour_description TEXT NOT NULL
);

CREATE TABLE Cosmic_event (
    cosmic_event_id SERIAL PRIMARY KEY,
    cosmic_event_description TEXT NOT NULL,
    cosmic_event_assessment assessments NOT NULL,
    planet_id INTEGER REFERENCES Planet(planet_id)
);

CREATE TABLE Observation (
    observation_id SERIAL PRIMARY KEY,
    cosmic_event_id INTEGER REFERENCES Cosmic_event(cosmic_event_id),
    observation_description TEXT NOT NULL
);


CREATE TABLE Nation (
    nation_id SERIAL PRIMARY KEY,
    country VARCHAR(64) NOT NULL,
    capital VARCHAR(64) NOT NULL,
    population_size INTEGER NOT NULL
);

CREATE TABLE People (
    people_id SERIAL PRIMARY KEY,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    age INTEGER NOT NULL,
    profession VARCHAR(32),
    interaction_id INTEGER REFERENCES Interaction(interaction_id),
    room_id INTEGER REFERENCES Room(room_id),
    behaviour_id INTEGER REFERENCES Behaviour(behaviour_id),
    nation_id INTEGER REFERENCES Nation(nation_id),
    observation_id INTEGER REFERENCES Observation(observation_id)
);




















