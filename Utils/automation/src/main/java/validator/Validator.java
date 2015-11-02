package validator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import configurations.TestConfig;

public abstract class Validator implements Comparable<Validator> {
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

	@Override
	public int compareTo(Validator validator) {
		if (validator == this) {
			return 0;
		}

		return 1;
	}
}
