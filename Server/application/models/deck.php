<?php

class Deck extends Eloquent
{
	public function copy()
	{
		// Create the new deck
		$new_deck = new Deck();
		$new_deck->name = $this->name;
		$new_deck->player_id = $this->player_id;
		$new_deck->save();
		
		// Copy cards into it
		DB::query( 
			"INSERT INTO " . DECKS_X_CARDS . "( deck_id, card_id, location )
			SELECT " . $new_deck->id . ", card_id, 0
			FROM " . DECKS_X_CARDS . "
			WHERE deck_id = " . $this->id );
		
		// Return the newly created deck's id
		return $new_deck;
	}
}