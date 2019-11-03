package com.hus.cis.collocation;


import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.mvc.controller.Controller;

public class Run {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
        Controller control = new Controller();
		DirectorySavedResult.pathToSavedResult();
	}
}
