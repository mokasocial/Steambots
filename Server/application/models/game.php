<?php

class Game extends Eloquent
{
	public function add_deck( $deck )
	{
		DB::table(GAMES_X_DECKS)->insert( array( 
			'game_id' => $this->id,
			'deck_id' => $deck->id,
			'player_id' => $deck->player_id ) );
	}
}