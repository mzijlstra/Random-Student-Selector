package edu.mum.student_selector.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class Course {
	@Id
	@GeneratedValue
	private long id;

	private String name;
	private Date date;
	private int currentStudent;

	@OneToMany
	@OrderColumn(name = "seq")
	@JoinColumn
	private List<Student> students = new ArrayList<Student>();

	public Course() {
		super();
	}
	
	public int getCurrent() {
		return currentStudent;
	}

	public int getEnrollmentSize() {
		return students.size();
	}
	
	public Student getCurrentStudent() {
		return students.get(currentStudent);
	}
		
	public Student getNextStudent() {
		int next = ++currentStudent;
		if (next >= students.size()) {
			shuffle();
			next = 0;
			currentStudent = 0;
		}
		return students.get(next);
	}

	public String toString() {
		return name;
	}
	
	public void shuffle() {
		Random random = new Random();
		for (int i = 0; i < students.size(); i++) {
			int swap = random.nextInt(students.size());
			Student temp = students.get(i);
			students.set(i, students.get(swap));
			students.set(swap, temp);
		}
	}
}
