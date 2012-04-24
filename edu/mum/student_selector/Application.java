package edu.mum.student_selector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.mum.student_selector.domain.Course;
import edu.mum.student_selector.domain.Student;

public class Application {
	private JFrame frame;
	private JPanel imagePanel;
	private JLabel nameLabel;
	private JButton next;
	private Course course;

	public static void main(String[] args) throws IOException {
		new Application();
	}

	public Application() throws IOException {
		// start by retrieving the latest course, and its students
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction t = s.beginTransaction();
		List<Course> courses = s.createQuery("from Course order by date").list();
		course = courses.get(courses.size() - 1);
		Student student = course.getCurrentStudent();
		t.commit();
		

		// course combobox -- TODO make this actually do something
		JComboBox courseList = new JComboBox(courses.toArray());

		// panel that shows the student's picture
		imagePanel = new JPanel();
		imagePanel.add(new JLabel(course.toString()));
		imagePanel.setPreferredSize(new Dimension(450, 500));

		// panel holding student name and next button
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		next = new JButton("Next");
		next.addActionListener(new NextBtnListener());
		infoPanel.add(next);
		nameLabel = new JLabel("");
		infoPanel.add(nameLabel);


		// actual window (frame)
		frame = new JFrame("Random Student Selectør");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(BorderLayout.NORTH, courseList);
		frame.add(BorderLayout.CENTER, imagePanel);
		frame.add(BorderLayout.SOUTH, infoPanel);
		frame.pack();
		frame.setResizable(false);

		showStudent(student);
		
		frame.setVisible(true);		
	}
	
	private void showStudent(Student student) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(
					"/home/mzijlstra/Documents/Courses/CS465_OperatingSystems/2012-05/pictures/"
							+ student.getStudentId() + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Image img = image.getScaledInstance(450, 500, 0);
		imagePanel.removeAll();
		imagePanel.add(new JLabel(new ImageIcon(img)));		
		nameLabel.setText(student.toString());		
		frame.repaint();		
	}
	
	private class NextBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Student student = course.getNextStudent();
			showStudent(student);
			
			Session s = HibernateUtil.getSessionFactory().getCurrentSession();
			Transaction t = s.beginTransaction();
			s.update(course);
			t.commit();			
		}
		
	}
}
