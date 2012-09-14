<?php

abstract class SourceGenerateMultiplier extends Effect
{
	protected $coef = array( 1, 1, 1 );

	public function on_generate_heat( Event &$e )
	{
		$e->data *= $this->coef[0];
	}

	public function on_generate_torque( Event &$e )
	{
		$e->data *= $this->coef[1];
	}

	public function on_generate_battery( Event &$e )
	{
		$e->data *= $this->coef[2];
	}
}