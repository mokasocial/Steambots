<?php
/**
 * Created by JetBrains PhpStorm.
 * User: EmoryM
 * Date: 7/14/11
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */

class Event
{
	public $type;			// most things are events - the type is just a string like 'turn_begin', 'generate_heat' and so forth (whatever we need)
	public $data;			// for turn_begin, data could be the number of the turn, for generate_heat... the amount of heat generated
	public $source;			// source should usually be a player - game phases (like turn_begin) would be null... maybe...
	public $previous_event;	// if this event is generated in response to another, keep track of that - we can prevent things like damage shield bounce

	public function __construct( $type, $source, $data = null, $previous_event = null )
	{
		$this->type = $type;
		$this->data = $data;
		$this->source = $source;
		$this->previous_event = $previous_event;
	}
}