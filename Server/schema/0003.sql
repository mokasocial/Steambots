USE steambots;

ALTER TABLE players CHANGE password password CHAR(60);
ALTER TABLE store_items ADD COLUMN class VARCHAR(128) NOT NULL;
ALTER TABLE store_items ADD COLUMN default_price INT NOT NULL DEFAULT 50;