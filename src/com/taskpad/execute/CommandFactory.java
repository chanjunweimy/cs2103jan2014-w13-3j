package com.taskpad.execute;

import java.util.LinkedList;
import java.util.logging.Logger;

import com.taskpad.storage.CommandRecord;
import com.taskpad.storage.DataFileStack;
import com.taskpad.storage.DataManager;
import com.taskpad.storage.NoPreviousCommandException;
import com.taskpad.storage.NoPreviousFileException;
import com.taskpad.storage.Task;
import com.taskpad.storage.TaskList;

public class CommandFactory {
	private static final String FEEDBACK_CLEAR = "All tasks have been deleted. You can use undo to get them back.";
	private static final String FEEDBACK_CANNOT_UNDO = "You don't have things to undo.";
	private static Logger logger = Logger.getLogger("TaskPad");
	
	protected static void add(String description, String deadline, String startDate,
			String startTime, String endDate,
			String endTime, String venue) {
		logger.info("adding task: " + description);
		TaskList listOfTasks = CommandFactoryBackend.archiveForUndo();
		
		Task taskAdded = CommandFactoryBackend.addTask(description, deadline, startDate, startTime,
				endDate, endTime, venue, listOfTasks);
		
		int taskId = listOfTasks.size();
		OutputToGui.output(OutputToGui.generateFeedbackForAdd(taskId, taskAdded));
	}
	
	protected static void listUndone() {
		TaskList listOfTasks = DataManager.retrieve(DataFileStack.FILE);
		
		LinkedList<Integer> tasks = CommandFactoryBackend.getUndoneTasks(listOfTasks);
		
		if (tasks.size() == 0) {
			OutputToGui.output("No undone task found.");
		} else {
			String text = OutputToGui.generateTextForTasks(tasks, listOfTasks);
			OutputToGui.output(text);
		}
		
	}

	protected static void listDone() {
		TaskList listOfTasks = DataManager.retrieve(DataFileStack.FILE);

		LinkedList<Integer> tasks = CommandFactoryBackend.getFinishedTasks(listOfTasks);
		
		if (tasks.size() == 0) {
			OutputToGui.output("No finished task found.");
		} else {
			String text = OutputToGui.generateTextForTasks(tasks, listOfTasks);
			OutputToGui.output(text);
		}
	}

	protected static void listAll() {
		TaskList listOfTasks = DataManager.retrieve(DataFileStack.FILE);

		LinkedList<Integer> tasks = CommandFactoryBackend.getAllTasks(listOfTasks);
		
		if (tasks.size() == 0) {
			OutputToGui.output("No task found.");
		} else {
			String text = OutputToGui.generateTextForTasks(tasks, listOfTasks);
			OutputToGui.output(text);
		}
	}

	protected static void undo() {
		try {
			String previousFile = CommandFactoryBackend.updateDataForUndo();		
			String command = CommandFactoryBackend.updateCommandRecordForUndo(previousFile);
			
			OutputToGui.output("Undo of '" + command + "' completed.");
		} catch (NoPreviousFileException e) {
			OutputToGui.output(FEEDBACK_CANNOT_UNDO);
		} catch (NoPreviousCommandException e) {
			// should never come to this
		}
	}
	
	protected static void redo() {
		try {
			String previousFile = CommandFactoryBackend.updateDataForRedo();
			String command = CommandFactoryBackend.updateCommandRecordForRedo(previousFile);
			
			OutputToGui.output("Redo of '" + command + "' completed.");
		} catch (NoPreviousFileException e) {
			OutputToGui.output("You don't have things to redo.");
		} catch (NoPreviousCommandException e) {
			// should never come to this
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void search(String keywordsString) {
		TaskList listOfTasks = DataManager.retrieve(DataFileStack.FILE);
		
		String[] keywords = keywordsString.split(" ");
		LinkedList<Integer> results = CommandFactoryBackend.getSearchResult(listOfTasks, keywords);
		
		// pass feedback to GUI
		String feedback = OutputToGui.generateTextForTasks(results, listOfTasks);
		OutputToGui.output("Number of tasks found: " + results.size() + "\n\n" + feedback);
	}

	protected static void edit(String taskIdString, String description) {
		TaskList listOfTasks = CommandFactoryBackend.archiveForUndo();
		
		String taskHistory = CommandFactoryBackend.editTaskDescription(taskIdString, description,
				listOfTasks);
		
		// pass feedback to gui
		OutputToGui.output("'" + taskHistory + "' changed to '" 
				+ OutputToGui.generateTitleForOneTask(taskIdString, description) + "'");
	}

	protected static void markAsDone(String taskIdString) {
		TaskList listOfTasks = CommandFactoryBackend.archiveForUndo();

		Task task = CommandFactoryBackend.markTaskAsDone(taskIdString, listOfTasks);

		OutputToGui.output(OutputToGui.generateTextForOneTask(
				Integer.parseInt(taskIdString), task));
	}

	protected static void clear() {
		CommandFactoryBackend.archiveForUndo();	
		CommandFactoryBackend.clearTasks();
		
		// pass feedback to gui
		OutputToGui.output(FEEDBACK_CLEAR);
	}

	protected static void addInfo(String taskIdString, String info) {
		TaskList listOfTasks = CommandFactoryBackend.archiveForUndo();
		
		int index = getIndexById(taskIdString);
		Task task = CommandFactoryBackend.addInfoToTask(info, listOfTasks, index);
		
		OutputToGui.output(OutputToGui.generateTextForOneTask(index + 1, task));
	}

	protected static int getIndexById(String taskIdString) {
		return Integer.parseInt(taskIdString) - 1;
	}

	protected static void delete(String taskIdString) {		
		TaskList listOfTasks = CommandFactoryBackend.archiveForUndo();
		
		int indexOfTask = Integer.parseInt(taskIdString) - 1;	
		assert(indexOfTask < listOfTasks.size());
		
		Task taskDeleted = CommandFactoryBackend.deleteTask(listOfTasks, indexOfTask);
		
		OutputToGui.output(OutputToGui.generateFeedbackForDelete(taskDeleted));
	}


}
