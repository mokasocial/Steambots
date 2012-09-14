USE steambots;

CREATE TABLE players(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	username VARCHAR(32) UNIQUE NOT NULL,
	email VARCHAR(255) NOT NULL,
	password CHAR(40) NOT NULL,
	salt CHAR(16) NOT NULL,
	token CHAR(37) UNIQUE NOT NULL,
	sprockets INT NOT NULL DEFAULT 0 );

CREATE TABLE games( 
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	started DATETIME NOT NULL,
	finished DATETIME );

CREATE TABLE games_x_players(
	game_id INT NOT NULL,
	player_id INT NOT NULL,
	heat INT,
	voltage INT,
	torque INT );

CREATE TABLE games_x_decks(
	game_id INT NOT NULL,
	player_id INT NOT NULL,
	deck_id INT NOT NULL );

CREATE TABLE turns(
	game_id INT NOT NULL,
	turn_number INT NOT NULL DEFAULT 1,
	player_id INT NOT NULL,
	played DATETIME,
	action INT );

CREATE TABLE decks(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	player_id INT NOT NULL,
	bot_id INT NOT NULL,
	game_id INT );

CREATE TABLE decks_x_cards(
	deck_id INT NOT NULL,
	card_id INT NOT NULL,
	location INT NOT NULL );

CREATE TABLE players_cards(
	player_id INT NOT NULL,
	card_id INT NOT NULL,
	quantity INT NOT NULL );

CREATE TABLE games_x_messages(
	game_id INT NOT NULL,
	player_id INT NOT NULL,
	message VARCHAR(140) );

CREATE TABLE invitations(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  from_player_id INT NOT NULL,
  to_player_id INT NOT NULL,
  game_id INT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  UNIQUE( from_player_id, to_player_id ) );