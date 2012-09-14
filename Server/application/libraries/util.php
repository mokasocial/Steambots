<?php

class Util
{
	public static function unique_md5() 
	{
    	mt_srand(microtime(true)*100000 + memory_get_usage(true));
    	return md5(uniqid(mt_rand(), true));
	}
	
	public static function generate_salt()
	{
		return substr( Util::unique_md5(), 0, 16 );
	}
}