<?php

class Responder
{
	protected $responded;	// an array of the events we've already responded to (to prevent loops)
	protected $priority;	// cards are executed from lowest to highest priority
	protected $target;		// for a buff or debuff, the target of the ongoing effect.  
							// For an attack, the target of the attack
	protected $turns_until_expiry;	// the number of turns until this card expires
    protected $name;

	public static function compare_priority( Card $a, Card $b )
	{
		if( $a->priority == $b->priority )
			return 0;

		return $a->priority > $b->priority;
	}

	// if we've got a method like on_turn_begin and an Event w/type turn_begin, pass the Event to the method
	// prevent a single card from responding to the same event twice
	public function handle_event( Event &$e )
	{
		if( empty( $this->responded[ $e->type ] ) )
		{
			$method = 'on_' . $e->type;

			$this->responded[ $e->type ] = true;

			$this->$method( $e );
		}

		return $e;
	}

	// prevent a card from responding to events it generates
	public function generate_event( Event &$e )
	{
		$this->responded[ $e->type ] = true;

		$this->owner->handle_event( $e );

		return $e;
	}

	// owner is a misnomer I guess, each card knows who it is 'on'
	public function set_owner( Bot &$owner )
	{
		$this->owner = $owner;
	}
}