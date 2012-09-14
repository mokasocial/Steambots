<?php

return array(

/*
 |--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is the public API of your application. To add functionality to your
| application, you just add to the array located in this file.
|
| Simply tell Laravel the HTTP verbs and request URIs it should respond to.
| You may respond to the GET, POST, PUT, or DELETE verbs. Enjoy the simplicity
| and elegance of RESTful routing.
|
| Here is how to respond to a simple GET request to http://example.com/hello:
|
|		'GET /hello' => function()
|		{
|			return 'Hello World!';
|		}
|
| You can even respond to more than one URI:
|
|		'GET /hello, GET /world' => function()
|		{
|			return 'Hello World!';
|		}
|
| It's easy to allow URI wildcards using the (:num) or (:any) place-holders:
|
|		'GET /hello/(:any)' => function($name)
|		{
|			return "Welcome, $name.";
|		}
|
*/

    'GET /login, POST /login, PUT /login' => function()
{
	$username = Input::get('username');
	$password = Input::get('password');

	$player = Player::where( 'username', '=', $username )->first();

	$response_code = 401;
	$result = array('error_code' => 'some error happened!');

	if( $player != null )
	{
		$salt = $player->salt;
			
		if( Auth::login( $username, $password . $salt ) )
		{
			$response_code = 200;
			$result = array('token' => $player->updateToken());
		}
	}

	return Response::make( json_encode( $result ), $response_code );
},

    'GET /signup, POST /signup, PUT /signup' => function()
{
	$username = Input::get('username');
	$email = Input::get('email');
	$salt = Util::generate_salt();
	$password = Hash::make( Input::get('password') . $salt );

	$player = Player::where( 'username', '=', $username )->first();

	if( empty( $player ) )
	{
		$player = new Player();

		$player->username = $username;
		$player->email = $email;
		$player->password = $password;
		$player->salt = $salt;

		return Response::make( json_encode( array('token' => $player->updateToken())), 201 );
	} else {
		return Response::make( json_encode( array('error_code' => 'Sorry, this username is already taken')), 409 );
	}

	return Response::make( json_encode( array('error_code' => 'some error happened')), 409 );
},

    'GET /reset' => function()
{
	$sql_path = realpath( '../schema' ) . '/*.sql';

	echo 'Running ' . $sql_path;

	$files = glob( $sql_path );

	$results = array();

	foreach( $files as $filename )
	{
		// other platforms than server
		// 		$command = "mysql -u root steambots < $filename 2>&1";
		// 		$command = "/Applications/XAMPP/xamppfiles/bin/mysql -u root steambots < $filename 2>&1";
		$command = "mysql -u gearwobble --password=redrobot11! -h mysql.tacticalautomata.com steambots < $filename 2>&1";
			
		$results[ $command ] = shell_exec( $command );
	}

	new dBug( $results );
},

    'GET /test' => function()
{
	$name1 = 'Emory';
	$name2 = 'John';

	$p1 = new Bot( 'Emory' );
	$p2 = new Bot( 'John' );

	$p1->handle_event( new Event( 'turn_begin', null, 'turn number might be good here' ) );

	// p1 draws a card
	$p1->handle_event( new Event( 'draw_card', null, 'data about the card we drew might be good here' ) );

	// p1 plays steam claw (card costs aren't in yet)
	// add steam claw to playing player's stack and send the play event (which contains the target)
	$p1->add_to_stack( new SteamClaw() );
	$p1->handle_event( new Event( 'play', null, $p2 ) );

	// end of p1's turn
	echo 'Before turn end:<br>';
	foreach( $p1->stack as $obj )
	{
		echo get_class( $obj ) . '<br>';
	}

	$p1->handle_event( new Event( 'turn_end', null, '*shrug*' ) );

	echo '<br>After turn end:<br>';
	foreach( $p1->stack as $obj )
	{
		echo get_class( $obj ) . '<br>';
	}
	echo '<br>';
},

    'GET /send_game_invitation, POST /send_game_invitation, PUT /send_game_invitation' => function()
{
	$token = Input::get('token');
	$deck_id = Input::get('deck_id');

	// Create a copy of this player's deck for this game
	$deck = Deck::find($deck_id);
	$copied_deck = $deck->copy();

	// Create a game
	$game = new Game();
	$game->save();
	$game->add_deck( $copied_deck );

	try{
		$username = Input::get('username');
		$host = Player::where( 'token', '=', $token )->first();
		$invitee = Player::where( 'username', '=', $username )->first();
	} catch (Exception $e){
		// player didn't send player id, sent email
		try{
			$email = Input::get('email');
			$username = Input::get('username');
			$host = Player::where( 'token', '=', $token )->first();
			$invitee = Player::where( 'email', '=', $email )->first();
		} catch(Exception $f) {
			// player sent an email we didn't have yet, create a recruitment
			// inxception: invite a new user, fuck yeah
			$recruitment = Recruitment::where( 'from_player_id', '=', $host->id )->where( 'to_email', '=', $email )->first();
			if( empty( $invitation ) )
			{
				$recruitment = new Recruitment();
				$recruitment->from_player_id = $host->id;
				$recruitment->to_email = $email;
			}
			$recruitment->updated_at = time();
			$recruitment->save();
		}
	}

	$invitation = Invitation::where( 'from_player_id', '=', $host->id )->where( 'to_player_id', '=', $invitee->id )->first();

	if( empty( $invitation ) )
	{
		$invitation = new Invitation;
		$invitation->from_player_id = $host->id;
		$invitation->to_player_id = $invitee->id;
	}

	$invitation->game_id = $game->id;
	$invitation->updated_at = time();
	$invitation->save();
},

    'GET /respond_to_game_invitation, POST /respond_to_game_invitation, PUT /respond_to_game_invitation' => function()
{
	$token = Input::get('token');
	$invitation_id = Input::get('invitation_id');
	$deck_id = Input::get('deck_id');
	$answer = Input::get('response');

	$invitation = Invitation::get( $invitation_id );

	$game = Game::find($invitation->game_id);

	$response = array();
	switch( $answer )
	{
		case 'Y':
			$game->active = true;
			$invitation->delete();
			return Response::make(json_encode( array( 'game_id' => $invitation->game_id ) ), 200);
			break;
		case 'N':
			$game->delete();
			$invitation->delete();
			return Response::make(json_encode( array( 'result' => "OK" ) ), 200);
			break;
	}

	
},

    'GET /get_store_items, POST /get_store_items, PUT /get_store_items' => function()
{
	$token = Input::get('token');

	$player = PlayerFactory::get_player_from_token( $token );
	if (empty($player)){
		return Response::make( json_encode( array('error_code' => 'Bad token')), 403 );
	}

	$items_for_sale = array();

	$store_item_ids = DB::table(STORE_ITEMS)->get(array('id'));

	foreach( $store_item_ids as $store_item_id )
	{
		$store_item = StoreItemFactory::get_store_item_from_id( $store_item_id->id );

		$items_for_sale[] = $store_item->get_details( $player );
	}

	return Response::make(json_encode( $items_for_sale ), 200);
},

    'GET /buy_item, POST /buy_item, PUT /buy_item' => function()
{
	$token = Input::get('token');
	$store_item_id = Input::get('store_item_id');
	$player = PlayerFactory::get_player_from_token( $token );

	if (empty($player)){
		return Response::make( json_encode( array('error_code' => 'Bad token')), 403 );
	}

	$store_item = StoreItemFactory::get_store_item_from_id( $store_item_id );

	if (empty($store_item)){
		return Response::make( json_encode( array('error_code' => 'Bad product id')), 403 );
	}

	$cost = $store_item->get_cost( $player );

	if( $player->sprockets >= $cost )
	{
		DB::table(PURCHASE_HISTORY)->insert( array( 'player_id' => $player->id, 'store_item_id' => $store_item->id ) );

		$player->sprockets -= $cost;

		$items_purchased = $store_item->on_purchase( $player );

		$player->save();

		return Response::make(json_encode( $items_purchased ), 403);
	}

	return Response::make(json_encode( array( 'error_code' => 'poverty' ) ), 200);
},

    'GET /profile, POST /profile, PUT /profile' => function()
{
	$token = Input::get('token');

	$player = PlayerFactory::get_player_from_token( $token );
	
	if (empty($player)){
		return Response::make( json_encode( array('error_code' => 'Bad token')), 403 );
	}

	$result = array();

	// return the following:
	// sprocket total

	$result['sprockets'] = $player->sprockets;

	// TODO achievements
	$result['achievements'] = array( array( 'name' => 'Alpha!', 'description' => 'You played the alpha!' ) );

	// invitations
	// 		incoming
	//		outgoing

	$result['invitations'] = array(
    		'to_player' => Invitation::get_invitations_to_player( $player ),
    		'from_player' => Invitation::get_invitations_from_player( $player ) );

	$result['games_in_progress'] = array();

	$games = $player->get_games_in_progress();

	return Response::make(json_encode( $result ), 200);
},

    'GET /get_decks, POST /get_decks, PUT /get_decks' => function()
{
	$token = Input::get('token');

	$player = PlayerFactory::get_player_from_token( $token );

	$decks = $player->get_decks();

	$result = array();

	foreach( $decks as $deck )
	{
		$result[] = array( 'name' => $deck->name, 'id' => $deck->id );
	}

	return Response::make(json_encode( $result ), 200);
}
);