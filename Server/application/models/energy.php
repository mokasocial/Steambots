<?php

class Energy
{
	private $sources = array( 'heat', 'torque', 'battery' );
	private $amounts;

	public function __construct()
	{
		foreach( $this->sources as $source )
		{
			$this->amounts[ $source ] = 0;
		}
	}

	public function modify( $amount, $source )
	{
		$this->amounts[ $source ] += $amount;
	}

	public function __invoke( $source )
	{
		return $this->amounts[ $source ];
	}
}