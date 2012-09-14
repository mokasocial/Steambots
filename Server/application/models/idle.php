<?php

class Idle extends SourceGenerateMultiplier
{
	public function __construct()
	{
		$this->priority = 2;
		$this->coef[0] = $this->coef[1] = $this->coef[2] = 0;
	}
}