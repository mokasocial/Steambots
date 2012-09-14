<?php

class Overclocked extends SourceGenerateMultiplier
{
	public function __construct()
	{
		$this->priority = 1;
		$this->coef[0] = $this->coef[1] = $this->coef[2] = 2;
	}
}