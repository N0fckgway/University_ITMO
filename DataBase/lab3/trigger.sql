-- CREATE TABLE device_status_history (
--     history_id SERIAL PRIMARY KEY,
--     device_id INTEGER NOT NULL,
--     old_status VARCHAR(32),
--     new_status VARCHAR(32),
--     changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     changed_by VARCHAR(50)
-- );

-- CREATE OR REPLACE FUNCTION log_device_status_change()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     INSERT INTO device_status_history (
--         device_id,
--         old_status,
--         new_status,
--         changed_by
--     ) VALUES (
--         NEW.device_id,
--         OLD.device_status,
--         NEW.device_status,
--         CURRENT_USER
--     );
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE TRIGGER device_status_change_trigger
-- AFTER UPDATE OF device_status ON device_status
-- FOR EACH ROW
-- WHEN (OLD.device_status IS DISTINCT FROM NEW.device_status)
-- EXECUTE FUNCTION log_device_status_change();

CREATE TABLE GameSessions (
    game_session_id SERIAL PRIMARY KEY,
    game_winning INTEGER,
    current_game_status VARCHAR(32),
    number_of_stone INTEGER
);


CREATE TABLE Moves (
    move_id SERIAL PRIMARY KEY,
    game_session_id INTEGER REFERENCES GameSessions(game_session_id),
    player_name VARCHAR(32),
    add_of_stones INTEGER,
    stones_after_move INTEGER
);

CREATE OR REPLACE FUNCTION check_game_winner()
RETURNS TRIGGER AS $$
DECLARE
    game_winning_threshold INTEGER;
    current_status VARCHAR(32);
    curr_number_of_stone INTEGER;
    total_stones INTEGER;
BEGIN
    SELECT game_winning, current_game_status, number_of_stone
    INTO game_winning_threshold, current_status, curr_number_of_stone
    FROM GameSessions
    WHERE game_session_id = NEW.game_session_id;

    SELECT COALESCE(SUM(add_of_stones), 0) # ну так чисто если null, то будем просто добавлять 0
    INTO total_stones
    FROM Moves 
    WHERE game_session_id = NEW.game_session_id;

    total_stones := total_stones + NEW.add_of_stones;

    NEW.stones_after_move := total_stones;


    IF current_status = 'We play' AND total_stones >= game_winning_threshold THEN
        UPDATE GameSessions
        SET current_game_status = 
            CASE NEW.player_name
                WHEN 'Petya' THEN 'Petya_win'
                WHEN 'Vanya' THEN 'Vanya_win'
                ELSE 'Error'
            END
        WHERE game_session_id = NEW.game_session_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_game_winner
BEFORE INSERT ON Moves
FOR EACH ROW
EXECUTE FUNCTION check_game_winner();
