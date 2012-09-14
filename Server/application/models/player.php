<?php

class Player extends Eloquent
{
    public function updateToken()
    {
        $token = DB::query( 'SELECT UUID() AS player_token' );

        $this->token = $token[0]->player_token;
        
        $this->save();

        return $this->token;
    }
    
    public function get_number_of_cards()
    {
    	// Returns the total number of cards (quantity of each card) a player owns
    	$sql = 'SELECT SUM(quantity) FROM ' . PLAYERS_CARDS . ' WHERE player_id = ' . $this->id;
    	
    	$sql = 'SELECT COALESCE( (' . $sql . '), 0 ) AS number';
    	
    	$owned_cards = DB::query( $sql );
    	
    	return $owned_cards[0]->number;
    }
    
    public function add_cards( $card_ids )
    {
    	// TODO: Should we verify the card exists before we add it?  Probably.  
    	// Am I going to?  NOPE! =D
    	
    	foreach( $card_ids as $card_id )
    	{
    		$sql = 'SELECT quantity FROM ' . PLAYERS_CARDS . ' WHERE player_id = ' . $this->id . ' AND card_id = ' . $card_id;
    		
    		$sql = 'SELECT COALESCE( (' . $sql . '), 0 ) AS number';
    		
    		$owned = DB::query( $sql );
    		
    		if( $owned[0]->number > 0 )
    		{
    			// If the player already owns this card, increase the quantity
    			DB::table(PLAYERS_CARDS)
    				->where( 'player_id', '=', $this->id )
    				->where( 'card_id', '=', $card_id )	
    				->update( array( 'quantity' => $owned[0]->number + 1 ) );
    		}
    		else
    		{
    			// Give the player 1 of this card
    			DB::table(PLAYERS_CARDS)
    				->insert( array( 'player_id' => $this->id, 'card_id' => $card_id, 'quantity' => 1 ) );
    		}
    	}
    }
    
    public function get_decks()
    {
    	return DB::table(DECKS)->where( 'player_id', '=', $this->id )->get();
    }
    
    public function get_games_in_progress()
    {
    	// TODO table constants, ORM, etc...
    	
    	$player_games_sql = "SELECT game_id FROM games_x_players WHERE player_id = " . $this->id;
    	
    	$game_id_sql = "SELECT id, started FROM games WHERE id IN (" . $player_games_sql . ") AND finished IS NULL";
    	
    	$games = DB::query( $game_id_sql );
    	
    	$result = array();
    	
    	foreach( $games as $game )
    	{
    		$other_player_id_sql = "SELECT player_id FROM games_x_players WHERE game_id = " . $game->id . " AND player_id != " . $this->id;
    		
    		$other_player_sql = "SELECT username FROM players WHERE id = (" . $other_player_id_sql . ")";
    		
    		$other_player = DB::query( $other_player_sql );
    		
    		$last_turn_sql = "SELECT player_id, played, action FROM turns WHERE game_id = " . $game->id . " ORDER BY played DESC LIMIT 1";
    		
    		$last_turn = DB::query( $last_turn_sql );
    		
    		// TODO eval for correctness, populate and return data
    	}
    }
}