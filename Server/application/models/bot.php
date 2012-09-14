<?php
/**
 * Created by JetBrains PhpStorm.
 * User: EmoryM
 * Date: 7/14/11
 * Time: 11:55 AM
 */
 
class Bot extends Responder
{
	public $stack;	// An array of all effects in play on this player
	public $health;
	public $energy;
	public $name;
	public $hand_size;
	public $amounts_per_turn;

	public function __construct( $name, $hand_size, $amounts_per_turn )
	{
		$this->health = new Health();
		$this->energy = new Energy();
		$this->stack = array();
		$this->name = $name;
		$this->hand_size = $hand_size;
		$this->amounts_per_turn = $amounts_per_turn;
        $this->priority = 0;
        $this->target = $this;	// Doesn't make a lot of sense...
	}

	public function add_to_stack( Card $card )
	{
		$card->set_owner( $this );
		$this->stack[] = $card;
		usort( $this->stack, array( 'Card', 'compare_priority' ) );
	}
	
	public function remove_from_stack( Card $card )
	{
		
	}

	public function handle_event( Event &$e )
	{
		$method = 'on_' . $e->type;

		foreach( $this->stack as $card )			// All the cards and effects are already sorted by priority
		{											// Let the event flow through them all, from bottom to top
			if( method_exists( $card, $method ) )
			{
				$card->handle_event( $e );			// Each card/effect has the ability to modify the event before it moves upward
			}
		}

        if( method_exists( $this, $method ) )
        {
            $this->$method( $e );
        }

        return $e;
	}

    public function on_turn_begin( Event &$turn_begin_event )
	{
		$e = $this->generate_event( new Event( 'generate_heat', $this, $amounts_per_turn['heat'], $turn_begin_event ) );

		$this->energy->modify( $e->data, 'heat' );

		$e = $this->generate_event( new Event( 'generate_torque', $this, $amounts_per_turn['torque'], $turn_begin_event ) );

		$this->energy->modify( $e->data, 'torque' );

		$e = $this->generate_event( new Event( 'generate_battery', $this, $amounts_per_turn['battery'], $turn_begin_event ) );

		$this->energy->modify( $e->data, 'battery' );
	}

    public function on_damage( Event &$damage_event )
	{
		$e = $this->generate_event( new Event( 'incoming_damage', $damage_event->source, $damage_event->data, $damage_event ) );

		$this->health->modify( -1 * $e->data );

		$e = $this->generate_event( new Event( 'received_damage', $damage_event->source, $e->data, $damage_event ) );
	}

    public function on_turn_end( Event &$turn_end_event )
	{
		foreach( $this->stack as $key => &$card )
		{
			if( !empty( $card->turns_until_expiry ) )
			{
				$card->turns_until_expiry--;

				if( $card->turns_until_expiry === 0 )
				{
					$e = $this->generate_event( new Event( 'card_expired', null, array( $card, true ), $turn_end_event ) );

					if( $e->data[1] )
						unset( $this->stack[$key] );
				}
			}
		}
	}
}