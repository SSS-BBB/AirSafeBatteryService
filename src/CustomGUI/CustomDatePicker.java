package CustomGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Utils.Utils;

public class CustomDatePicker extends JDialog {

	private JPanel calendarPanel;
	private Color backgroundColor, lineColor, fontColor;

	private JLabel monthLabel, yearLabel;

	private LocalDate currentDatePicked;
	public LocalDate selectedDate;
	private String[] monthName;
	private String[] dayName;
	private JButton[][] dateButtons;

	private DatePickerAction datePickerAction;

	public CustomDatePicker(Window parent, Color backgroundColor, Color lineColor, Color fontColor) {
		super(parent, "เลือกวันที่", ModalityType.APPLICATION_MODAL);

		this.backgroundColor = backgroundColor;
		this.lineColor = lineColor;
		this.fontColor = fontColor;

		currentDatePicked = LocalDate.now();
		currentDatePicked = currentDatePicked.withDayOfMonth(1); // to make sure you can go forward and backward by one month

		monthName = new String[] { "มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
				"สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม" };
		dayName = new String[] { "อาทิตย์", "จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์", "เสาร์" };
		dateButtons = new JButton[6][7];
	}

	public void createDatePicker() {
		if (currentDatePicked == null) {
			System.err.println("Null current calendar date. Unable to create date picker dialog.");
			return;
		}

		calendarPanel = new JPanel(new BorderLayout());
		calendarPanel.setBackground(backgroundColor);
		calendarPanel.add(createMonthYearPanel(), BorderLayout.NORTH);
		calendarPanel.add(createGridDatePanel(), BorderLayout.CENTER);
		this.add(calendarPanel);
	}

	public void addDatePickerAction(DatePickerAction datePickerAction) {
		this.datePickerAction = datePickerAction;
	}

	private JPanel createGridDatePanel() {
		if (currentDatePicked == null) {
			System.err.println("Null current calendar date. Unable to create grid date panel.");
			return new JPanel();
		}

		JPanel gridDatePanel = new JPanel(new GridLayout(0, 7));
		gridDatePanel.setBackground(backgroundColor);

		int fontSize = 16;

		// Sun -> Sat
		for (String day : dayName) {
			JPanel dayOfWeekPanel = new JPanel(new BorderLayout());
			dayOfWeekPanel.setBackground(backgroundColor);
			dayOfWeekPanel.setBorder(Utils.createPaddingBorder(lineColor, 5));

			JLabel dayOfWeekLabel = new JLabel(day);
			dayOfWeekLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
			dayOfWeekLabel.setForeground(fontColor);
			dayOfWeekPanel.add(dayOfWeekLabel, BorderLayout.CENTER);
			gridDatePanel.add(dayOfWeekPanel);
		}

		// empty date
		for (int i = 0; i < 42; i++) {
			
			int row = i / 7;
			int col = i % 7;
			
			JPanel datePanel = new JPanel(new BorderLayout());
			datePanel.setBackground(backgroundColor);
			datePanel.setBorder(Utils.createPaddingBorder(lineColor, 5));

			dateButtons[row][col] = Utils.createNoBackgroundButton("", fontColor, fontSize);

			dateButtons[row][col].addActionListener(e -> {
				selectedDate = LocalDate.now();
				selectedDate = selectedDate.withYear(currentDatePicked.getYear());
				selectedDate = selectedDate.withMonth(currentDatePicked.getMonthValue());

				// get date from button text
				if (dateButtons[row][col].getText().isEmpty())
					return;

				int selectedDateOfMonth = Integer.parseInt(dateButtons[row][col].getText());
				selectedDate = selectedDate.withDayOfMonth(selectedDateOfMonth);

				if (datePickerAction != null)
					datePickerAction.onDatePicked();

				dispose();
			});

			dateButtons[row][col].setEnabled(false);

			datePanel.add(dateButtons[row][col], BorderLayout.CENTER);
			gridDatePanel.add(datePanel);
		}
		
		updateDate(0, 0);
		return gridDatePanel;
	}

	private JPanel createMonthYearPanel() {
		if (currentDatePicked == null) {
			System.err.println("Null current calendar date. Unable to create month year panel.");
			return new JPanel();
		}

		JPanel monthYearPanel = new JPanel(new BorderLayout());
		monthYearPanel.setBackground(backgroundColor);

		int currentYear = currentDatePicked.getYear();
		int currentMonthIndex = currentDatePicked.getMonthValue() - 1;
		String currentMonth = monthName[currentMonthIndex];

		int fontSize = 18;

		// Month
		JPanel monthPanel = new JPanel();
		monthPanel.setBackground(backgroundColor);
		monthYearPanel.add(monthPanel, BorderLayout.CENTER);

		JButton monthBackButton = Utils.createNoBackgroundButton("<", fontColor, fontSize);
		monthPanel.add(monthBackButton);
		monthBackButton.addActionListener(e -> {
			updateDate(-1, 0);
		});

		monthLabel = new JLabel(currentMonth);
		monthLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		monthLabel.setForeground(fontColor);
		monthLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		monthPanel.add(monthLabel);

		JButton monthNextButton = Utils.createNoBackgroundButton(">", fontColor, fontSize);
		monthPanel.add(monthNextButton);
		monthNextButton.addActionListener(e -> {
			updateDate(1, 0);
		});

		// Year
		JPanel yearPanel = new JPanel();
		yearPanel.setBackground(backgroundColor);
		monthYearPanel.add(yearPanel, BorderLayout.EAST);

		JButton yearBackButton = Utils.createNoBackgroundButton("<", fontColor, fontSize);
		yearPanel.add(yearBackButton);
		yearBackButton.addActionListener(e -> {
			updateDate(0, -1);
		});

		yearLabel = new JLabel(String.valueOf(currentYear));
		yearLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		yearLabel.setForeground(fontColor);
		yearLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		yearPanel.add(yearLabel);

		JButton yearNextButton = Utils.createNoBackgroundButton(">", fontColor, fontSize);
		yearPanel.add(yearNextButton);
		yearNextButton.addActionListener(e -> {
			updateDate(0, 1);
		});

		return monthYearPanel;
	}
	
	private void updateDate(int monthUpdate, int yearUpdate) {
		currentDatePicked = currentDatePicked.plusMonths(monthUpdate);
		currentDatePicked = currentDatePicked.plusYears(yearUpdate);

		int selectedYear = currentDatePicked.getYear();
		int selectedMonthIndex = currentDatePicked.getMonthValue() - 1;
		String selectedMonth = monthName[selectedMonthIndex];

		// update month and year
		monthLabel.setText(selectedMonth);
		yearLabel.setText(String.valueOf(selectedYear));

		// update date
		LocalDate c = LocalDate.now();
		c = c.withDayOfMonth(1);
		c = c.withMonth(currentDatePicked.getMonthValue());
		c = c.withYear(currentDatePicked.getYear());

		int firstDayOfWeek = c.getDayOfWeek().getValue() % 7;
		int lastDayOfMonth = c.lengthOfMonth();

		// clear all buttons
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 7; col++) {
				dateButtons[row][col].setText("");
				dateButtons[row][col].setEnabled(false);
			}
		}

		// date
		for (int date = 1; date <= lastDayOfMonth; date++) {
			int index = firstDayOfWeek + date - 1;
			int row = index / 7;
			int col = index % 7;

			dateButtons[row][col].setText(String.valueOf(date));
			dateButtons[row][col].setEnabled(true);
			
			/*
			dateButtons[row][col].addActionListener(e -> {
				selectedDate = Calendar.getInstance();
				selectedDate.set(Calendar.YEAR, currentCalendarPicked.get(Calendar.YEAR));
				selectedDate.set(Calendar.MONTH, currentCalendarPicked.get(Calendar.MONTH));

				// get date from button text
				int selectedDateOfMonth = Integer.parseInt(dateButtons[row][col].getText());
				selectedDate.set(Calendar.DATE, selectedDateOfMonth);

				if (datePickerAction != null)
					datePickerAction.onDatePicked();

				dispose();
			});
			*/
		}
	}

}