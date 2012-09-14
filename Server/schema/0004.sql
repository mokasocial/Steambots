USE steambots;

CREATE TABLE recruitments(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  from_player_id INT NOT NULL,
  to_email TEXT (100) NOT NULL,
  game_id INT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  UNIQUE( from_player_id, to_email (100) ) );