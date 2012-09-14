<?php

class Recruitment extends Eloquent
{
    public static $timestamps = true;
    
    public static function get_recruitments_to_player( &$player )
    {
    	$results = array();
    	
    	$recruitments = Recruitment::where( 'to_email', '=', $player->email )->get();
    	
    	foreach( $recruitments as $recruitment )
    	{
    		$from_player = Player::find( $recruitment->from_player_id );
    		
    		$results[] = array( 
    			'id' => $recruitment->id,
    			'from_player' => $from_player->username,
    			'created' => $recruitment->created_at );
    	}
    	
    	return $results;
    }
    
    public static function get_recruitments_from_player( &$player )
    {
    	$results = array();
    	
    	$recruitments = Recruitment::where( 'from_player_id', '=', $player->id )->get();
    	
    	foreach( $recruitments as $recruitment )
    	{
    		$to_player = Player::find( $recruitment->to_email );

    		$results[] = array(
    			'id' => $recruitment->id,
    			'to_player' => $to_player->username,
    			'created' => $recruitment->created_at );
    	}
    	
    	return $results;
    }
}