USE steambots;

ALTER TABLE games_x_players ADD COLUMN life INT NOT NULL;

CREATE TABLE store_items(
	id INT NOT NULL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description TEXT NOT NULL,
	image VARCHAR(255) NOT NULL );
	
CREATE TABLE purchase_history(
	player_id INT NOT NULL,
	store_item_id INT NOT NULL,
	purchase_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP );
	
CREATE TABLE cards(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255),
	image VARCHAR(255),
	description TEXT,
	damage INT NOT NULL DEFAULT 0,
	duration INT NOT NULL DEFAULT 0,
	heat_cost INT NOT NULL DEFAULT 0,
	torque_cost INT NOT NULL DEFAULT 0,
	battery_cost INT NOT NULL DEFAULT 0 );
	
CREATE TABLE bots(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255),
	image VARCHAR(255),
	description TEXT,
	hand_size INT NOT NULL,
	heat_regen INT NOT NULL DEFAULT 1,
	torque_regen INT NOT NULL DEFAULT 1,
	battery_regen INT NOT NULL DEFAULT 1 );