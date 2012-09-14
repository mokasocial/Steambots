USE steambots;

INSERT INTO store_items( id, name, description, image, class, default_price )
VALUES
( 1, 'Starter Deck', 'This starter deck contains 30 cards', 'starter_deck_1', 'AlphaDeck', 150 ),
( 2, 'Booster Pack', 'This booster pack contains 5 random cards', 'booster_pack_1', 'BoosterPackPrime', 40 ),
( 3, 'Super Booster Pack', 'This booster pack contains 3 rare cards', 'booster_pack_2', 'BoosterPackPrime', 60 ),
( 4, 'Bulk Booster Pack', 'This booster pack contains 10 random cards', 'booster_pack_3', 'BoosterPackPrime', 70 ),
( 5, 'Gearwobble', 'Gearwobble has no quibbles in its missiles, nor a conscience. Just more missiles.', 'bot_1', 'RobotItem', 100 ),
( 6, 'Bitsmasher', 'Bitsmasher came here to check oil and kick ass, and it is all out of oil.', 'bot_2', 'RobotItem', 100 ),
( 7, 'Cannonaxle', 'Cannonaxle never met another robot it didn\'t like firing cannons at. Someday...', 'bot_3', 'RobotItem', 100 ),
( 8, 'Boltlasher', 'Boltlasher is not evil, it is simply misunderstood and more or less out to cause suffering.', 'bot_4', 'RobotItem', 100 ),
( 9, 'Amperion', 'Amperion is a highly refined bot, a sophisticated killing machine. Sorry to boast.', 'bot_5', 'RobotItem', 100 );