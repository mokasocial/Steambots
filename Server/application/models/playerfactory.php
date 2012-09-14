<?php

class PlayerFactory
{
	public static function get_player_from_token( $token )
	{
		return Player::where( 'token', '=', $token )->first();
	}
}