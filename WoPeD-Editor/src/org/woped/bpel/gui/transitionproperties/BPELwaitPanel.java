package org.woped.bpel.gui.transitionproperties;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.woped.core.model.petrinet.TransitionModel;
import org.woped.editor.controller.TransitionPropertyEditor;
import org.woped.translations.Messages;

import com.toedter.calendar.JCalendar;

/**
 * @author Kristian Kindler / Esther Landes
 *
 * This is a panel in the transition properties, which enables the user to maintain data for a "wait" BPEL activity.
 *
 * Created on 16.12.2007
 */

@SuppressWarnings("serial")

public class BPELwaitPanel extends BPELadditionalPanel implements ActionListener{

	private ButtonGroup waitButtonGroup = null;
	private JPanel waitDurationEntry = null;
	private JRadioButton waitDurationRadioButton = null;
	private JRadioButton waitDeadlineRadioButton = null;

	private JPanel radioButtonPanel;
	private JPanel radioButtonSubPanel;
	private JPanel calendarPanel;
	private JPanel deadlinePanel;
	private JPanel deadlineTimePanel;
	private JPanel deadlineTimeSubPanel;
	private JCalendar calendar;

	private JPanel durationPanel;
	private JPanel durationSubPanel;

	private JTextField deadLineTextFieldHour;
	private JTextField deadLineTextFieldMinute;
	private JTextField deadLineTextFieldSecond;

	private JTextField durationTextFieldYear;
	private JTextField durationTextFieldMonth;
	private JTextField durationTextFieldDay;
	private JTextField durationTextFieldHour;
	private JTextField durationTextFieldMinute;
	private JTextField durationTextFieldSecond;

	private static final String WAIT_DURATION = Messages.getString("Transition.Properties.BPEL.Wait.Duration");
	private static final String WAIT_DEADLINE = Messages.getString("Transition.Properties.BPEL.Wait.Deadline");

	private GridBagConstraints c1;

	public BPELwaitPanel(TransitionPropertyEditor t_editor, TransitionModel transition){

		super(t_editor, transition);

		setLayout(new GridBagLayout());
		c1 = new GridBagConstraints();

		waitButtonGroup = new ButtonGroup();
		waitButtonGroup.add(getWaitDurationRadioButton());
		waitButtonGroup.add(getWaitDeadlineRadioButton());


		c1.weightx = 1;
		c1.weighty = 1;
		c1.anchor = GridBagConstraints.WEST;
		c1.fill = GridBagConstraints.HORIZONTAL;

		c1.gridx = 0;
		c1.gridy = 0;
		add(getRadioButtonPanel(), c1);

		c1.gridx = 0;
		c1.gridy = 1;
		c1.insets = new Insets(5,0,20,0);
		add(new JSeparator(), c1);

		c1.gridx = 0;
		c1.gridy = 2;
		c1.insets = new Insets(0,0,10,0);

		add(getDurationPanel(), c1);

	}




	private JPanel getDurationPanel() {
		if (durationPanel == null) {
			durationPanel = new JPanel();
			durationPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0,10,0,0);
			c.gridx = 0;
			c.gridy = 0;
			durationPanel.add(getDurationSubPanel(), c);
		}

		return durationPanel;
	}


	private JPanel getDurationSubPanel() {
		if (durationSubPanel == null) {
			durationSubPanel = new JPanel();
			durationSubPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;

			c.insets = new Insets(0,0,0,20);
			c.gridx = 0;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Years")), c);
			c.gridx = 1;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Months")), c);
			c.gridx = 2;
			c.gridy = 0;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Days")), c);

			c.insets = new Insets(0,0,5,20);
			c.gridx = 0;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldYear(), c);
			c.gridx = 1;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldMonth(), c);
			c.gridx = 2;
			c.gridy = 1;
			durationSubPanel.add(getDurationInputfieldDay(), c);

			c.insets = new Insets(0,0,0,20);
			c.gridx = 0;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Hours")), c);
			c.gridx = 1;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Minutes")), c);
			c.gridx = 2;
			c.gridy = 2;
			durationSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Seconds")), c);

			c.gridx = 0;
			c.gridy = 3;
			c.insets = new Insets(0,0,0,20);
			durationSubPanel.add(getDurationInputfieldHour(), c);
			c.gridx = 1;
			c.gridy = 3;
			durationSubPanel.add(getDurationInputfieldMinute(), c);
			c.gridx = 2;
			c.gridy = 3;
			durationSubPanel.add(getDurationInputfieldSecond(), c);
		}

		return durationSubPanel;
	}


	private JPanel getDeadlinePanel() {
		if (deadlinePanel == null) {
			deadlinePanel = new JPanel();
			deadlinePanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.insets = new Insets(0,10,0,0);
			c.gridx = 0;
			c.gridy = 0;
			deadlinePanel.add(getCalendarPanel(), c);
			c.gridx = 0;
			c.gridy = 1;
			deadlinePanel.add(getDeadlineTimePanel(), c);
		}

		return deadlinePanel;
	}


	private JPanel getCalendarPanel() {
		if (calendarPanel == null) {
			calendarPanel = new JPanel();
			calendarPanel.setLayout(new BorderLayout());
			calendarPanel.add(getDeadlineCalendar(), BorderLayout.WEST);
		}

		return calendarPanel;
	}

	private JCalendar getDeadlineCalendar() {
		if (calendar == null) {
			calendar = new JCalendar();
			// To do: hier noch WoPeD aktuelle Sprache abfragen
			calendar.setLocale(Locale.ENGLISH);
		}
		return calendar;
	}

	private JPanel getDeadlineTimePanel() {
		if (deadlineTimePanel == null) {
			deadlineTimePanel = new JPanel();
			deadlineTimePanel.setLayout(new BorderLayout());
			deadlineTimePanel.add(getDeadlineTimeSubPanel(), BorderLayout.WEST);
		}

		return deadlineTimePanel;
	}

	private JPanel getDeadlineTimeSubPanel() {
		if (deadlineTimeSubPanel == null) {
			deadlineTimeSubPanel = new JPanel();
			deadlineTimeSubPanel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(10,0,0,20);
			deadlineTimeSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Hours")), c);
			c.gridx = 1;
			c.gridy = 0;
			deadlineTimeSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Minutes")), c);
			c.gridx = 2;
			c.gridy = 0;
			deadlineTimeSubPanel.add(new JLabel(Messages.getString("Transition.Properties.BPEL.Wait.Seconds")), c);
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0,0,0,20);
			deadlineTimeSubPanel.add(getDeadlineInputfieldHour(), c);
			c.gridx = 1;
			c.gridy = 1;
			deadlineTimeSubPanel.add(getDeadlineInputfieldMinute(), c);
			c.gridx = 2;
			c.gridy = 1;
			deadlineTimeSubPanel.add(getDeadlineInputfieldSecond(), c);
		}

		return deadlineTimeSubPanel;
	}

	private JTextField getDeadlineInputfieldHour(){
		if (deadLineTextFieldHour == null) {
			deadLineTextFieldHour = new JTextField(10);
			deadLineTextFieldHour.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldHour;
	}

	private JTextField getDeadlineInputfieldMinute(){
		if (deadLineTextFieldMinute == null) {
			deadLineTextFieldMinute = new JTextField(10);
			deadLineTextFieldMinute.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldMinute;
	}

	private JTextField getDeadlineInputfieldSecond(){
		if (deadLineTextFieldSecond == null) {
			deadLineTextFieldSecond = new JTextField(10);
			deadLineTextFieldSecond.setActionCommand(WAIT_DEADLINE);
		}
		return deadLineTextFieldSecond;
	}


	private JTextField getDurationInputfieldYear(){
		if (durationTextFieldYear == null) {
			durationTextFieldYear = new JTextField(10);
			durationTextFieldYear.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldYear;
	}

	private JTextField getDurationInputfieldMonth(){
		if (durationTextFieldMonth == null) {
			durationTextFieldMonth = new JTextField(10);
			durationTextFieldMonth.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldMonth;
	}

	private JTextField getDurationInputfieldDay(){
		if (durationTextFieldDay == null) {
			durationTextFieldDay = new JTextField(10);
			durationTextFieldDay.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldDay;
	}

	private JTextField getDurationInputfieldHour(){
		if (durationTextFieldHour == null) {
			durationTextFieldHour = new JTextField(10);
			durationTextFieldHour.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldHour;
	}

	private JTextField getDurationInputfieldMinute(){
		if (durationTextFieldMinute == null) {
			durationTextFieldMinute = new JTextField(10);
			durationTextFieldMinute.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldMinute;
	}

	private JTextField getDurationInputfieldSecond(){
		if (durationTextFieldSecond == null) {
			durationTextFieldSecond = new JTextField(10);
			durationTextFieldSecond.setActionCommand(WAIT_DEADLINE);
		}
		return durationTextFieldSecond;
	}



	private JPanel getRadioButtonPanel() {
		if (radioButtonPanel == null) {
			radioButtonPanel = new JPanel();
			radioButtonPanel.setLayout(new BorderLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			radioButtonPanel.add(getRadioButtonSubPanel(), BorderLayout.WEST);
		}

		return radioButtonPanel;
	}

	private JPanel getRadioButtonSubPanel() {
		if (radioButtonSubPanel == null) {
			radioButtonSubPanel = new JPanel();
			radioButtonSubPanel.setLayout(new FlowLayout());
			radioButtonSubPanel.add(getWaitDurationEntry());
			radioButtonSubPanel.add(getWaitDeadlineRadioButton());
		}

		return radioButtonSubPanel;
	}


	private JPanel getWaitDurationEntry() {
		if (waitDurationEntry == null) {
			waitDurationEntry = new JPanel();
			waitDurationEntry.setLayout(new BorderLayout());
			waitDurationEntry.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			waitDurationEntry.add(getWaitDurationRadioButton(), c);
		}

		return waitDurationEntry;
	}

	private JRadioButton getWaitDurationRadioButton(){
			if (waitDurationRadioButton == null) {
				waitDurationRadioButton = new JRadioButton(WAIT_DURATION);
				waitDurationRadioButton.setSelected(true);
				waitDurationRadioButton.setActionCommand(WAIT_DURATION);
				waitDurationRadioButton.addActionListener(this);
			}
			return waitDurationRadioButton;
	}

	private JRadioButton getWaitDeadlineRadioButton(){
		if (waitDeadlineRadioButton == null) {
			waitDeadlineRadioButton = new JRadioButton(WAIT_DEADLINE);
			waitDeadlineRadioButton.setActionCommand(WAIT_DEADLINE);
			waitDeadlineRadioButton.addActionListener(this);
		}
		return waitDeadlineRadioButton;
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(WAIT_DURATION)){
			if (deadlinePanel != null){
				remove(deadlinePanel);
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0,0,10,0);
				add(getDurationPanel(), c1);
				t_editor.repaintTabPane();
			}
		}

		else if (e.getActionCommand().equals(WAIT_DEADLINE)){
			if (durationPanel != null){
				remove(durationPanel);
				c1.gridx = 0;
				c1.gridy = 2;
				c1.insets = new Insets(0,0,10,0);
				add(getDeadlinePanel(), c1);
				t_editor.repaintTabPane();
			}
		}

	}
	
	
	//	***************** content getter methods  **************************
	
	public String getSelectedRadioButton(){
		if (waitDurationRadioButton.isSelected()==true){
			return "Duration";
		}
		return "Deadline";
	}

// noch mit Alex ankl�ren (Esther)	
	
	
	// ***** Deadline *****
	
	public String getDeadline()
	{
		return "'" + getDeadLineYear() + "-" + getDeadLineMonth() + "-" + getDeadLineDay() + "T" + getDeadLineHour() + ":" + getDeadLineMinute() + ":" + getDeadLineSecond() + "+1:00'";
	}
	
	public String getDuration()
	{
		return "'P" + getDurationYear() + "Y" + getDurationMonth() + "M" + getDurationDay() + "DT" + getDurationHour() + "H" + getDurationMinute() + "M" + getDurationSecond() + "S'";
	}
	
	public String getDeadLineDay(){
		return ""+calendar.getDate().getDay();
	}
	
	public String getDeadLineMonth(){
		if (deadLineTextFieldMinute.getText() == null)
			return null;
		return ""+calendar.getDate().getMonth();
	}
	
	public String getDeadLineYear(){
		return ""+calendar.getDate().getYear();
	}
	
	public String getDeadLineHour(){
		if (deadLineTextFieldHour.getText() == null)
			return null;
		return deadLineTextFieldHour.getText();
	}
	
	public String getDeadLineMinute(){
		if (deadLineTextFieldMinute.getText() == null)
			return null;
		return deadLineTextFieldMinute.getText();
	}
	
	public String getDeadLineSecond(){
		if (deadLineTextFieldSecond.getText() == null)
			return null;
		return deadLineTextFieldSecond.getText();
	}
	
	// ***** Duration ******
	
	public String getDurationYear(){
		if (durationTextFieldYear.getText() == null)
			return null;
		return durationTextFieldYear.getText();
	}
	
	public String getDurationMonth(){
		if (durationTextFieldMonth.getText() == null)
			return null;
		return durationTextFieldMonth.getText();
	}
	
	public String getDurationDay(){
		if (durationTextFieldDay.getText() == null)
			return null;
		return durationTextFieldDay.getText();
	}
	
	public String getDurationHour(){
		if (durationTextFieldHour.getText() == null)
			return null;
		return durationTextFieldHour.getText();
	}
	
	public String getDurationMinute(){
		if (durationTextFieldMinute.getText() == null)
			return null;
		return durationTextFieldMinute.getText();
	}
	
	public String getDurationSecond(){
		if (durationTextFieldSecond.getText() == null)
			return null;
		return durationTextFieldSecond.getText();
	}
	
	
	//	***************** content setter methods  **************************

// nach abkl�ren mit Alex an getter anpassen (Esther)	
	
/*	public void setRadioButton(String durationDeadline){
			
	}
	
	public void setDurationDay(String variable){
		toVariableComboBox.addItem(variable);	
	}*/

}
