package UserApp.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import CustomGUI.CustomDatePicker;
import CustomGUI.DatePickerAction;
import Struct.ForRentPowerBank;
import UserApp.UserMainFrame;
import Utils.Utils;

public class UserForRentDetail extends JPanel {

	private UserMainFrame mainFrame;
	private Color backgroundColor;

	public ForRentPowerBank powerBank;

	private JLabel rentDayPlaceHolderLabel, returnDayPlaceHolderLabel;
	private JComboBox returnAddressDropdown;

	public UserForRentDetail(UserMainFrame mainFrame, Color backgroundColor) {
		super(new BorderLayout());

		this.mainFrame = mainFrame;
		this.backgroundColor = backgroundColor;
	}

	public void createForRentDetailPage() {
		this.setBackground(backgroundColor);

		// Title
		JLabel titleLabel = new JLabel("รายละเอียดการเช่า", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 30, 0));
		titleLabel.setAlignmentX(LEFT_ALIGNMENT);
		titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		titleLabel.setBackground(backgroundColor);
		this.add(titleLabel, BorderLayout.NORTH);

		// Power bank details
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to create for rent detail page.");
			return;
		}

		JPanel detailContainerPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		detailContainerPanel.setBackground(backgroundColor);
		detailContainerPanel.setAlignmentX(LEFT_ALIGNMENT);
		detailContainerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		detailContainerPanel.add(createPowerbankBox());
		detailContainerPanel.add(createDetailInput());

		this.add(detailContainerPanel, BorderLayout.CENTER);
	}

	private JPanel createDetailInput() {
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to create detail input.");
			return null;
		}

		JPanel detailInputPanel = new JPanel();
		detailInputPanel.setLayout(new BoxLayout(detailInputPanel, BoxLayout.Y_AXIS));
		detailInputPanel.setBackground(backgroundColor);
		detailInputPanel.setAlignmentX(LEFT_ALIGNMENT);

		int margin = 8;
		int fontSize = 16;
		int fontAdd = 4;
		float componentAlignment = LEFT_ALIGNMENT;

		// Rent Day
		JPanel rentDayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rentDayPanel.setBackground(backgroundColor);
		detailInputPanel.add(rentDayPanel);

		JLabel rentDayLabel = new JLabel("วันที่เช่า:");
		rentDayLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		rentDayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		rentDayLabel.setAlignmentX(componentAlignment);
		rentDayPanel.add(rentDayLabel);

		JPanel rentDayPlaceHolder = new JPanel(new BorderLayout());
		rentDayPlaceHolder.setBackground(backgroundColor);
		rentDayPlaceHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		rentDayPlaceHolder.setPreferredSize(new Dimension(180, 30));

		rentDayPlaceHolderLabel = new JLabel("");
		rentDayPlaceHolderLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentDayPlaceHolderLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
		rentDayPlaceHolderLabel.setAlignmentX(componentAlignment);
		rentDayPlaceHolderLabel.setAlignmentY(CENTER_ALIGNMENT);
		rentDayPlaceHolder.add(rentDayPlaceHolderLabel, BorderLayout.WEST);
		rentDayPanel.add(rentDayPlaceHolder);

		ImageIcon calendarIcon = Utils.createImageIcon("/icons/calendar_icon_24.png", "Calendar Icon");
		JButton calendarButton = Utils.createIconButton(calendarIcon);
		rentDayPanel.add(calendarButton);

		calendarButton.addActionListener(e -> {
			CustomDatePicker datePicker = new CustomDatePicker(SwingUtilities.getWindowAncestor(this), backgroundColor,
					Color.LIGHT_GRAY, Color.BLACK);

			datePicker.addDatePickerAction(new DatePickerAction() {
				@Override
				public void onDatePicked() {
					if (Utils.isDatePast(datePicker.selectedDate)) {
						Utils.showDialog(mainFrame, "เลือกวันในอดีตไม่ได้!", "คุณไม่สามารถเลือกวันในอดีตได้ โปรดเลือกวันนี้หรือวันข้างหน้า");
						return;
					}
					
					
					SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
					String formattedDate = dateFormatter.format(datePicker.selectedDate.getTime());
					rentDayPlaceHolderLabel.setText(formattedDate);
				}

			});
			
			datePicker.createDatePicker();

			datePicker.setSize(600, 400);
			datePicker.setLocationRelativeTo(null);
			datePicker.setVisible(true);

		});

		return detailInputPanel;
	}

	private JPanel createPowerbankBox() {
		if (powerBank == null) {
			System.err.println("Null powerBank. Unable to create power bank box.");
			return null;
		}

		JPanel powerbankBoxPanel = new JPanel();
		powerbankBoxPanel.setLayout(new BoxLayout(powerbankBoxPanel, BoxLayout.Y_AXIS));
		powerbankBoxPanel.setBackground(backgroundColor);
		powerbankBoxPanel.add(Box.createVerticalGlue());

		int margin = 12;
		int fontSize = 20;
		int fontAdd = 4;
		float componentAlignment = CENTER_ALIGNMENT;

		ImageIcon scaledPowerBankImage = Utils.scaleImageKeepRatio(powerBank.image, 200, 200);
		JLabel powerBankImageLabel = new JLabel(scaledPowerBankImage);
		powerBankImageLabel.setAlignmentX(componentAlignment);
		powerBankImageLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(powerBankImageLabel);

		JLabel brandLabel = new JLabel(powerBank.brand);
		brandLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		brandLabel.setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));
		brandLabel.setAlignmentX(componentAlignment);
		brandLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(brandLabel);

		JLabel nameLabel = new JLabel(powerBank.name);
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		nameLabel.setAlignmentX(componentAlignment);
		nameLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(nameLabel);

		JLabel modelLabel = new JLabel(powerBank.model);
		modelLabel.setFont(new Font("Tahoma", Font.BOLD, fontSize + fontAdd));
		modelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		modelLabel.setAlignmentX(componentAlignment);
		modelLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(modelLabel);

		String whAndCapacity = String.valueOf(powerBank.wh) + " Wh / " + String.valueOf(powerBank.capacity) + " mAh";
		JLabel whAndCapacityLabel = new JLabel(whAndCapacity);
		whAndCapacityLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		whAndCapacityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		whAndCapacityLabel.setAlignmentX(componentAlignment);
		whAndCapacityLabel.setAlignmentY(componentAlignment);
		whAndCapacityLabel.setForeground(Color.GRAY);
		powerbankBoxPanel.add(whAndCapacityLabel);

		String sizeAndWeight = String.valueOf(powerBank.width) + "x" + String.valueOf(powerBank.length) + "x"
				+ String.valueOf(powerBank.height) + " cm" + " " + String.valueOf(powerBank.weight) + " kg";
		JLabel sizeAndWeightLabel = new JLabel(sizeAndWeight);
		sizeAndWeightLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		sizeAndWeightLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		sizeAndWeightLabel.setAlignmentX(componentAlignment);
		sizeAndWeightLabel.setAlignmentY(componentAlignment);
		sizeAndWeightLabel.setForeground(Color.GRAY);
		powerbankBoxPanel.add(sizeAndWeightLabel);

		/*
		 * String locker = "Locker หมายเลข " + String.valueOf(powerBank.lockerNumber);
		 * JLabel lockerLabel = new JLabel(locker); lockerLabel.setFont(new
		 * Font("Tahoma", Font.PLAIN, fontSize));
		 * lockerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		 * lockerLabel.setAlignmentX(componentAlignment);
		 * powerbankBoxPanel.add(lockerLabel);
		 */

		String rentPrice = String.valueOf(powerBank.pricePerDay) + " บาท/วัน";
		JLabel rentPriceLabel = new JLabel(rentPrice);
		rentPriceLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		rentPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		rentPriceLabel.setAlignmentX(componentAlignment);
		rentPriceLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(rentPriceLabel);

		String maxRentDay = "เช่าได้สูงสุด " + String.valueOf(powerBank.maxDuration) + " วัน";
		JLabel maxRentDayLabel = new JLabel(maxRentDay);
		maxRentDayLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
		maxRentDayLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		maxRentDayLabel.setAlignmentX(componentAlignment);
		maxRentDayLabel.setAlignmentY(componentAlignment);
		powerbankBoxPanel.add(maxRentDayLabel);

		/*
		 * String lateFee = "ค่าปรับส่งคืนสาย " +
		 * String.valueOf(powerBank.lateFeePerDay) + " บาท/วัน"; JLabel lateFeeLabel =
		 * new JLabel(lateFee); lateFeeLabel.setFont(new Font("Tahoma", Font.PLAIN,
		 * fontSize)); lateFeeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0,
		 * margin, 0)); lateFeeLabel.setAlignmentX(componentAlignment);
		 * lateFeeLabel.setForeground(new Color(255, 95, 21)); // Warning Color
		 * powerbankBoxPanel.add(lateFeeLabel);
		 */

		powerbankBoxPanel.add(Box.createVerticalGlue());

		return powerbankBoxPanel;
	}

}