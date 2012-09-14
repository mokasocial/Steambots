<?php

class Invitation extends Eloquent
{
    public static $timestamps = true;
    
    public static function get_invitations_to_player( &$player )
    {
    	$results = array();
    	
    	$invitations = Invitation::where( 'to_player_id', '=', $player->id )->get();
    	
    	foreach( $invitations as $invitation )
    	{
    		$from_player = Player::find( $invitation->from_player_id );
    		
    		$results[] = array( 
    			'id' => $invitation->id,
    			'from_player' => $from_player->username,
    			'created' => $invitation->created_at );
    	}
    	
    	return $results;
    }
    
    public static function get_invitations_from_player( &$player )
    {
    	$results = array();
    	
    	$invitations = Invitation::where( 'from_player_id', '=', $player->id )->get();
    	
    	foreach( $invitations as $invitation )
    	{
    		$to_player = Player::find( $invitation->to_player_id );

    		$results[] = array(
    			'id' => $invitation->id,
    			'to_player' => $to_player->username,
    			'created' => $invitation->created_at );
    	}
    	
    	return $results;
    }
}