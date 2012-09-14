<?php

class AlphaDeck extends BoosterPack
{
	public function on_purchase( &$player )
	{
		// give the player 1 copy of each 1 and 2 cost card (these are IDs)
		$cards_in_booster = range( 1, 18, 1 );
		
		// and a random assortment of 12 cards from the remaining 24
		$remaining_cards = range( 19, 42, 1 );
		
		shuffle( $remaining_cards );
		
		// pick some random from the ones remaining
		for( $i = 0; $i < 12; $i++ )
		{
			$cards_in_booster[] = array_pop( $remaining_cards );
		}
		
		// add the cards to the player
		$player->add_cards( $cards_in_booster );
		
		// return some JSON containing what the player got
		// Need to go from the IDs we're using internally to some
		// JSON the client(s) can use to display the results nicely
		
		$card_rows = DB::table(CARDS)
			->where_in( 'id', $cards_in_booster )
			->get();
		
		$results = array();
			
		foreach( $card_rows as $card_row )
		{
			$results[] = array(
				'type' => 'card',
				'id' => $card_row->id,
				'name' => $card_row->name,
				'image' => $card_row->image,
				'description' => $card_row->description );
 		}
		
 		// If the player doesn't have any decks, create them a deck named 'Starter Deck'
 		// and add these cards into it
 		
 		$player_decks = DB::table(DECKS)
 			->where( 'player_id', '=', $player->id )
 			->get();
 			
 		if( empty( $player_decks ) )
 		{
 			$deck = new Deck();
 			$deck->name = 'Starter Deck';
 			$deck->player_id = $player->id;
 			$deck->save();
 			
 			foreach( $cards_in_booster as $card_id )
 			{
 				DB::table(DECKS_X_CARDS)
 					->insert( array( 'deck_id' => $deck->id, 'card_id' => $card_id ) );
 			}
 		}
 		
		return $results;
	}
	
	public function get_cost( &$player )
	{
		if( $player->get_number_of_cards() == 0 )
		{
			return 0;
		}
		
		return $this->default_price;
	}
}