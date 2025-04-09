INSERT INTO Room (room_id, room_name, room_status) VALUES
    (1, 'игровая', 'открыта'),
    (2, 'туалет', 'закрыта'),
    (3, 'цирк', 'открыта');


INSERT INTO Device (device_id, device_name, device_status, room_id) VALUES
    (1, 'Телевизор', TRUE, 1),
    (2, 'Кондиционер', FALSE, 1),
    (3, 'Лампа', TRUE, 1);


INSERT INTO Planet (planet_id, planet_name, color, planet_size) VALUES
    (1, 'Юпитер', 'оранжевый', 'большая'),
    (2, 'Солнце', 'желтый', 'огромная'),
    (3, 'Плутон', 'белый', 'маленькая');


INSERT INTO Interaction (interaction_id, interaction_name, interaction_description) VALUES 
    (1, 'поцелуй', 'страстный'),
    (2, 'прощание', 'грустное');


INSERT INTO Behaviour (behaviour_id, behaviour_name, behaviour_description) VALUES
    (1, 'Агрессивное', 'Поведение, характеризующееся агрессией и конфликтностью.'),
    (2, 'Дружелюбное', 'Поведение, направленное на установление дружеских отношений.'),
    (3, 'Нейтральное', 'Поведение без выраженных эмоций или предпочтений.');


INSERT INTO Cosmic_event (cosmic_event_id, cosmic_event_description, cosmic_event_assessment, planet_id) VALUES
    (1, 'Юпитер сгорает', 'катастрофическая', 1),
    (2, 'Солнце покрылось льдом', 'идеальная', 2),
    (3, 'Плутон горит', 'удовлетворительная', 3);


INSERT INTO Nation (nation_id, country, capital, population_size) VALUES
    (1, 'Россия', 'Москва', 146171015),
    (2, 'США', 'Вашингтон', 331893745),
    (3, 'Китай', 'Пекин', 1402112000);

INSERT INTO Observation (observation_id, cosmic_event_id, observation_description) VALUES
    (1, 1, 'Наблюдающий 1'),
    (2, 2, 'Наблюдающий 2'),
    (3, 3, 'Наблюдающий 3');
    


INSERT INTO People (people_id, first_name, last_name, age, profession, interaction_id, room_id, behaviour_id, nation_id, observation_id) VALUES
    (1, 'Флойд', 'Мэйвезер', 42, 'boxer', 1, 1, 1, 1, 1),
    (2, 'Стас', 'Суворов', 18, 'student', 1, 1, 1, 1, 1),
    (3, 'Петр', 'Петрович', 52, 'clown', 1, 1, 1, 1, 1);



