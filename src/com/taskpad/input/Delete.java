/**
 * This class creates a Delete object
 * 
 * Current syntax for delete: del <taskID>
 * 
 * Returns Input object
 * 
 */

package com.taskpad.input;

public class Delete extends Command{
	
	private static String COMMAND_DELETE = "DELETE";
	private static String PARAMETER_TASK_ID = "TASKID";
	private static int NUMBER_ARGUMENTS = 1;		//Number of arguments for delete
	@SuppressWarnings("unused")
	private static String TEST = "TESTING CONFLICTS";

	public Delete(String input, String fullInput) {
		super(input, fullInput);	
	}
	
	@Override
	protected void initialiseOthers(){
		setNUMBER_ARGUMENTS(NUMBER_ARGUMENTS);
		setCOMMAND(COMMAND_DELETE);	
	}

	@Override
	protected boolean commandSpecificRun() {
		putInputParameters();
		return true;
	}

	@Override
	protected void initialiseParametersToNull() {
		inputParameters.put(PARAMETER_TASK_ID, "");		
	}

	@Override
	protected void putInputParameters() {
		putOneParameter(PARAMETER_TASK_ID, input.trim());
	}
	
}
