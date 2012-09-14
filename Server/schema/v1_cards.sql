USE steambots;

-- This top part is just going to get the cards into the DB in as few lines as possible...
-- We'll derive class names from card names
-- More SQL will follow to fill in descriptions and provide images

INSERT INTO cards( name, description, damage, heat_cost, torque_cost, battery_cost ) VALUES
( 'Steam Claw', 'A quick steam-powered claw attack deals damage.', 1, 1, 0, 0 ),
( 'Mechanical Claw', 'A mechanized metal claw attack deals damage.', 3, 0, 1, 0 ),
( 'Electrical Claw', 'An electric servo claw attack deals damage.', 2, 0, 0, 1 ),
( 'Steam Saw', 'A fine-toothed saw slices at vulnerable cables.', 2, 1, 0, 0 ),
( 'Mechanical Saw', 'A rusty chainsaw implement slices at vulnerable cables.', 6, 0, 1, 0 ),
( 'Electrical Saw', 'A buzzsaw slices at vulnerable cables.', 4, 0, 0, 1 ),
( 'Steam Maul', 'A large mallet pounds on the opponent.', 3, 1, 0, 0 ),
( 'Mechanical Maul', 'A spinning axe slashes at the opponent.', 9, 0, 1, 0 ),
( 'Electrical Maul', 'An arcing hammer beats upon the opponent.', 6, 0, 0, 1 ),
( 'Steam Jab', 'A quick punch to distract the opponent.', 2, 2, 0, 0 ),
( 'Mechanical Jab', 'A quick punch to distract the opponent.', 0, 0, 2, 0 ),
( 'Electrical Jab', 'A quick punch to distract the opponent.', 0, 0, 0, 2 ),
( 'Steam Punch', 'A solid pneumatic punch sends the enemy reeling.', 2, 2, 0, 0 ),
( 'Mechanical Punch', 'A solid pneumatic punch sends the enemy reeling.', 4, 0, 2, 0 ),
( 'Electrical Punch', 'A solid pneumatic punch sends the enemy reeling.', 0, 0, 0, 2 ),
( 'Steam Haymaker', 'A reckless punch knocks the opponent down.', 5, 2, 0, 0 ),
( 'Mechanical Haymaker', 'A reckless punch knocks the opponent down.', 5, 0, 2, 0 ),
( 'Electrical Haymaker', 'A reckless punch knocks the opponent down.', 3, 0, 0, 2 ),
( 'Steam Beam', 'A jet of superheated steam melts rubber and softens plating.', 0, 3, 0, 0 ),
( 'Mechanical Beam', 'A heavy metal beam swings at the opponent.', 0, 0, 3, 0 ),
( 'Electrical Beam', 'Electricity arcs out to disable the opponent.', 0, 0, 0, 3 ),
( 'Steam Ray', 'A fine jet of steam shoots out accurately at a vulnerable spot.', 3, 3, 0, 0 ),
( 'Mechanical Ray', 'A railgun accelerates a conductive projectile towards the opponent.', 1, 0, 3, 0 ),
( 'Electrical Ray', 'A burst of electricity arcs out and finds your opponent.', 0, 0, 0, 3 ),
( 'Steam Missile', 'A missile damages your opponent.', 0, 3, 0, 0 ),
( 'Mechanical Missile', 'A missile damages your opponent.', 0, 0, 3, 0 ),
( 'Electrical Missile', 'A missile damages your opponent.', 3, 0, 0, 3 ),
( 'Steam Rocket', 'A rocket damages your opponent.', 5, 4, 0, 0 ),
( 'Mechanical Rocket', 'A rocket damages your opponent.', 10, 0, 4, 0 ),
( 'Electrical Rocket', 'A rocket damages your opponent.', 5, 0, 0, 4 ),
( 'Steam Bomb', 'On collision, this bomb releases white-hot vapor to damage enemy circuits.', 0, 4, 0, 0 ),
( 'Mechanical Bomb', 'This bomb is filled with shrapnel to damage vunerable areas.', 0, 0, 4, 0 ),
( 'Electrical Bomb', 'This device releases an electromagnetic pulse, disabling the opponent.', 0, 0, 0, 4 ),
( 'Steam Shield', 'A cloud of steam impedes your opponent\'s attacks.', 0, 4, 0, 0 ),
( 'Mechanical Shield', 'A sheet of rolled iron protects you from attacks.', 0, 0, 4, 0 ),
( 'Electrical Shield', 'A plasma curtain hinders your opponent\s movement.', 0, 0, 0, 4 ),
( 'Steam Field', 'Steam billows out, obscuring the opponent\'s vision.', 0, 4, 0, 0 ),
( 'Mechanical Field', 'A thick plate of steel is thrown up to give you cover.', 0, 0, 4, 0 ),
( 'Electrical Field', 'A coil resonates, creating a wide arc of electricity.', 0, 0, 0, 4 ),
( 'Steam Ward', 'A strategically placed boiler discharges scalding gouts of steam.', 0, 4, 0, 0 ),
( 'Mechanical Ward', 'A flailing contraption engages the enemy with chains and spikes.', 0, 0, 4, 0 ),
( 'Electrical Ward', 'A transformer blows, transmitting high-voltage sparks.', 0, 0, 0, 4 );

UPDATE cards
SET image = 'coming_soon' 
WHERE image IS NULL;

UPDATE cards
SET description = 'Temporary'
WHERE description IS NULL AND damage != 0;

UPDATE cards
SET description = 'More Temporary'
WHERE description IS NULL AND damage = 0;