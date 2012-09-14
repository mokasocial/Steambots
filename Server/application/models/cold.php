<?php

class Cold extends SourceGenerateMultiplier
{
	public function __construct()
	{
		$this->priority = 2;
		$this->coef[0] = 0;
	}
}