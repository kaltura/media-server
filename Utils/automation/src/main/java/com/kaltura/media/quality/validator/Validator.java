package com.kaltura.media.quality.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.kaltura.media.quality.configurations.TestConfig;

public abstract class Validator {
	private static List<ValidatorTask> openTasks = new ArrayList<ValidatorTask>();

	protected static TestConfig config;

	protected class ValidatorTask extends FutureTask<String>{

		public ValidatorTask(Callable<String> callable) {
			super(callable);
		}

		@Override
		protected void done(){
			super.done();
			openTasks.remove(this);
		}
	}
	
	public Validator() {
		config = TestConfig.get();
	}
}
