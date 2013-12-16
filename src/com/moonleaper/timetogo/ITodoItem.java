package com.moonleaper.timetogo;

import java.util.Date;

public interface ITodoItem {
	String getTitle();
	Date getDueDate();
	int getPriority();
}
