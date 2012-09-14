<?php

class StoreItemFactory
{
	public static function get_store_item_from_id( $store_item_id )
	{
		$class = DB::table(STORE_ITEMS)
			->where( 'id', '=', $store_item_id )
			->first( array( 'class' ) );
			
		if( $class != null )
		{
			$class = $class->class;
			
			return $class::find($store_item_id);
		}
		
		return null;
	}
}