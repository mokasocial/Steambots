<?php

class MechanicalField extends Card
{
	public function __construct()
	{
		$this->priority = 1;
	}

	public function on_turn_begin( Event &$turn_begin_event )
	{
		$e = $this->generate_event( new Event( 'generate_health', $this->owner, 1, $turn_begin_event ) );

		$this->owner->health->modify( $e->data );
	}
}