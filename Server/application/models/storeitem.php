<?php

abstract class StoreItem extends Eloquent
{
	public static $table = 'store_items';
	
	// on_purchase will return JSON for the client to display what the player received from the purchase
	public function on_purchase( &$player )
	{
		// Add whatever was in the StoreItem to the player
		// Return an array of what they received
	}
	
	// cost depends on player - this is mainly for pricing the initial bot and initial starter deck
	public function get_cost( &$player )
	{
		return $this->default_price;
	}
	
	// this will be converted to JSON for the client to parse
	public function get_details( &$player )
	{
		return array(
			'id' => $this->id,
			'name' => $this->name,
			'description' => $this->description,
			'image' => $this->image,
			'class' => $this->class,
			'cost' => $this->get_cost( $player ) );
	}
}