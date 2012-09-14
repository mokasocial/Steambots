<?php

// Sticking table defines here
define( "CARDS", "cards" );
define( "DECKS", "decks" );
define( "DECKS_X_CARDS", "decks_x_cards" );
define( "PLAYERS_CARDS", "players_cards" );
define( "PURCHASE_HISTORY", "purchase_history" );
define( "STORE_ITEMS", "store_items" );
define( "GAMES_X_PLAYERS", "games_x_players" );
define( "GAMES_X_DECKS", "games_x_decks" );
define( "GAMES_X_MESSAGES", "games_x_messages" );

return array(

	/*
	|--------------------------------------------------------------------------
	| Default Database Connection
	|--------------------------------------------------------------------------
	|
	| The name of your default database connection.
	|
	| This connection will be the default for all database operations unless a
	| different connection is specified when performing the operation.
	|
	*/

	'default' => 'mysql',

	/*
	|--------------------------------------------------------------------------
	| Database Connections
	|--------------------------------------------------------------------------
	|
	| All of the database connections used by your application.
	|
	| Supported Drivers: 'mysql', 'pgsql', 'sqlite'.
	|
	| Note: When using the SQLite driver, the path and "sqlite" extention will
	|       be added automatically. You only need to specify the database name.
	|
	| Using a driver that isn't supported? You can still establish a PDO
	| connection. Simply specify a driver and DSN option:
	|
	|		'odbc' => array(
	|			'driver'   => 'odbc',
	|			'dsn'      => 'your-dsn',
	|			'username' => 'username',
	|			'password' => 'password',
	|		)
	|
	| Note: When using an unsupported driver, Eloquent and the fluent query
	|       builder may not work as expected.
	|
	*/

	'connections' => array(

		'sqlite' => array(
			'driver'   => 'sqlite',
			'database' => 'application',
		),

		'mysql' => array(
			'driver'   => 'mysql',
			'host'     => '127.0.0.1',
			'database' => 'steambots',
			'username' => 'root',
			'password' => '',
			'charset'  => 'utf8',
		),

		'pgsql' => array(
			'driver'   => 'pgsql',
			'host'     => 'localhost',
			'database' => 'database',
			'username' => 'root',
			'password' => 'password',
			'charset'  => 'utf8',
		),

	),

);