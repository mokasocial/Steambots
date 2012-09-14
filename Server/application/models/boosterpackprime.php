<?php

// BoosterPackPrime only contains the initial set of cards
// Then maybe BoosterPackv2 after we add a few
// Ideally player will go to the store and select between several different booster packs (just like MTG)

class BoosterPackPrime extends BoosterPack
{
	public function on_purchase( &$player )
	{

	}

	public function get_cost( &$player )
	{
		return $this->default_price;
	}
}