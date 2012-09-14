<?php

class Health
{
	private $health;

	public function __construct()
	{
		$this->health = 20;
	}

	public function modify( $amount )
	{
		$this->health += $amount;
	}

	public function __invoke()
	{
		return $this->health;
	}
}