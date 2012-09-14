<?php

class SteamClaw extends Card
{
	public function __construct()
	{
		$this->priority = 1;
		$this->turns_until_expiry = 1;
	}

	// Let's assume this event comes into the played card AND the event's data is the card's target
	public function on_play( Event &$card_play_event )
	{
		$opponent = $card_play_event->data;

		$outgoing_damage_event = $this->generate_event( new Event( 'outgoing_damage', $this->owner, 1, $card_play_event ) );

		$damage_event = $opponent->handle_event( new Event( 'damage', $this->owner, $outgoing_damage_event->data, $outgoing_damage_event ) );

		$this->generate_event( new Event( 'dealt_damage', $this->owner, $damage_event->data, $damage_event ) );
	}
}