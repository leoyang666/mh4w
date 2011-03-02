package com.dwarfeng.jier.mh4w.core.view.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.dwarfeng.dutil.basic.prog.ObverserSet;
import com.dwarfeng.jier.mh4w.core.model.cm.DataListModel;
import com.dwarfeng.jier.mh4w.core.model.eum.LabelStringKey;
import com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter;
import com.dwarfeng.jier.mh4w.core.model.obv.ListOperateObverser;
import com.dwarfeng.jier.mh4w.core.model.struct.AttendanceOffset;
import com.dwarfeng.jier.mh4w.core.model.struct.CountResult;
import com.dwarfeng.jier.mh4w.core.model.struct.DefaultUnsafeAttendanceOffset;
import com.dwarfeng.jier.mh4w.core.model.struct.Mutilang;
import com.dwarfeng.jier.mh4w.core.model.struct.MutilangSupported;
import com.dwarfeng.jier.mh4w.core.model.struct.Person;
import com.dwarfeng.jier.mh4w.core.model.struct.UnsafeAttendanceOffset;
import com.dwarfeng.jier.mh4w.core.util.Constants;
import com.dwarfeng.jier.mh4w.core.util.FormatUtil;
import com.dwarfeng.jier.mh4w.core.util.Mh4wUtil;
import com.dwarfeng.jier.mh4w.core.view.obv.AttendanceOffsetPanelObverser;
import com.sun.glass.events.KeyEvent;

public class JAttendanceOffsetPanel extends JPanel implements MutilangSupported, ObverserSet<AttendanceOffsetPanelObverser> {

	private static final long serialVersionUID = 6243768107970879415L;

	/**�۲�������*/
	private final Set<AttendanceOffsetPanelObverser> obversers = Collections.newSetFromMap(new WeakHashMap<>());
	
	/**�����Խӿ�*/
	private final Mutilang mutilang;
	
	/*
	 * final ��
	 */
	private final JTable table;
	private final JTextField timeTextField;
	private final JTextField descriptionTextField;
	private final JButton submitButton;
	private final JComboBox<Person> comboBox;
	private final JLabel timeLabel;
	private final JLabel descritionLabel;
	private final JButton clearButton;
	private final JButton loadButton;
	private final JButton saveButton;

	/*
	 * ��ģ�͡�
	 */
	private DataListModel<AttendanceOffset> attendanceOffsetModel;
	private DataListModel<CountResult> countResultModel;
	
	/*
	 * ��ͼģ���Լ���Ⱦ
	 */
	private final DefaultTableModel tableModel = new DefaultTableModel(){

		private static final long serialVersionUID = 1995931789304479415L;

		/*
		 * (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#getColumnCount()
		 */
		@Override
		public int getColumnCount() {
			return 3;
		};
		
		/*
		 * (non-Javadoc)
		 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
		 */
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	private final DefaultComboBoxModel<Person> comboBoxModel = new DefaultComboBoxModel<>();
	private final TableCellRenderer tableRenderer = new DefaultTableCellRenderer(){
		
		private static final long serialVersionUID = -2854380488244617595L;

		@Override
		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if(column == 1){
				setHorizontalAlignment(JLabel.RIGHT);
				setText(FormatUtil.formatDouble((double) value));
			}
			if(column == 0 || column == 2){
				setHorizontalAlignment(JLabel.LEFT);
			}
			return this;
		};
	};
	private final ListCellRenderer<Object> comboBoxRenderer = new DefaultListCellRenderer(){
	
		private static final long serialVersionUID = -6046116864760471615L;

		@Override
		public java.awt.Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			//������ת���ǰ�ȫ��
			if(Objects.nonNull(value)){
				setText(FormatUtil.formatPerson((Person) value));
			}
			return this;
		};
		
	};
	
	/*
	 * ��ģ�͵Ĺ۲�����
	 */
	private final ListOperateAdapter<AttendanceOffset> attendanceOffsetObverser = new ListOperateAdapter<AttendanceOffset>() {

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireAdded(int, java.lang.Object)
		 */
		@Override
		public void fireAdded(int index, AttendanceOffset value) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					tableModel.insertRow(index, new Object[]{
							FormatUtil.formatPerson(value.getPerson()),
							value.getValue(),
							value.getDescription(),
					});
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireChanged(int, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void fireChanged(int index, AttendanceOffset oldValue, AttendanceOffset newValue) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					tableModel.removeRow(index);
					tableModel.insertRow(index, new Object[]{
							FormatUtil.formatPerson(newValue.getPerson()),
							newValue.getValue(),
							newValue.getDescription(),
					});
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireRemoved(int)
		 */
		@Override
		public void fireRemoved(int index) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					tableModel.removeRow(index);
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireCleared()
		 */
		@Override
		public void fireCleared() {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					int count = tableModel.getRowCount();
					for(int i = 0 ; i < count ; i ++){
						tableModel.removeRow(0);
					}
				}
			});
		}
		
	};
	private final ListOperateObverser<CountResult> countResultObverser = new ListOperateAdapter<CountResult>() {

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireAdded(int, java.lang.Object)
		 */
		@Override
		public void fireAdded(int index, CountResult value) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					int size = comboBoxModel.getSize();
					comboBoxModel.insertElementAt(value.getPerson(), index);
					if(size == 0){
						comboBox.setSelectedIndex(0);
					}
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireChanged(int, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void fireChanged(int index, CountResult oldValue, CountResult newValue) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					comboBoxModel.removeElementAt(index);
					comboBoxModel.insertElementAt(newValue.getPerson(), index);
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireRemoved(int)
		 */
		@Override
		public void fireRemoved(int index) {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					comboBoxModel.removeElementAt(index);
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see com.dwarfeng.jier.mh4w.core.model.obv.ListOperateAdapter#fireCleared()
		 */
		@Override
		public void fireCleared() {
			Mh4wUtil.invokeInEventQueue(new Runnable() {
				@Override
				public void run() {
					comboBoxModel.removeAllElements();
				}
			});
		}
		
	};

	/**
	 * ��ʵ����
	 */
	public JAttendanceOffsetPanel() {
		this(Constants.getDefaultLabelMutilang(), null, null);
	}

	/**
	 * ��ʵ����
	 * @param mutilang ָ���Ķ����Խӿڣ�����Ϊ <code>null</code>��
	 * @param attendanceOffsetModel ָ���Ŀ��ڲ���ģ�͡�
	 * @param countResultModel ָ����ͳ�ƽ��ģ�͡�
	 */
	public JAttendanceOffsetPanel(Mutilang mutilang, DataListModel<AttendanceOffset> attendanceOffsetModel,
			DataListModel<CountResult> countResultModel) {
		Objects.requireNonNull(mutilang, "��ڲ��� mutilang ����Ϊ null��");
		
		this.mutilang = mutilang;
		
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		table.setModel(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "remove");
		table.getActionMap().put("remove", new AbstractAction() {
			
			private static final long serialVersionUID = 7760542975339282553L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if(index >= 0 && tableModel.getRowCount() >= 0){
					fireRemoveAttendanceOffset(index);
				}
			}
		});
		table.getColumnModel().getColumn(0).setCellRenderer(tableRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(tableRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(tableRenderer);

		table.getColumnModel().getColumn(0).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_1));
		table.getColumnModel().getColumn(1).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_2));
		table.getColumnModel().getColumn(2).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_3));

		((JLabel) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
		
		scrollPane.setViewportView(table);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		comboBox = new JComboBox<>();
		comboBox.setModel(comboBoxModel);
		comboBox.setRenderer(comboBoxRenderer);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);
		
		timeLabel = new JLabel();
		timeLabel.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_5));
		GridBagConstraints gbc_timeLabel = new GridBagConstraints();
		gbc_timeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_timeLabel.anchor = GridBagConstraints.EAST;
		gbc_timeLabel.gridx = 1;
		gbc_timeLabel.gridy = 0;
		panel.add(timeLabel, gbc_timeLabel);
		
		timeTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		panel.add(timeTextField, gbc_textField);
		timeTextField.setColumns(10);
		
		submitButton = new JButton();
		submitButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_4));
		submitButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "submit");
		submitButton.getActionMap().put("submit", new AbstractAction() {
			
			private static final long serialVersionUID = 7760542975339282553L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(Objects.isNull(comboBox.getSelectedItem())) return;
				//�˴�ת���ǰ�ȫ�ġ�
				Person person = (Person) comboBox.getSelectedItem();
				fireSubmitAttendanceOffset(new DefaultUnsafeAttendanceOffset(person.getName(), person.getDepartment(), 
						person.getWorkNumber(), timeTextField.getText(), descriptionTextField.getText()));
			}
		});
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Objects.isNull(comboBox.getSelectedItem())) return;
				//�˴�ת���ǰ�ȫ�ġ�
				Person person = (Person) comboBox.getSelectedItem();
				fireSubmitAttendanceOffset(new DefaultUnsafeAttendanceOffset(person.getName(), person.getDepartment(), 
						person.getWorkNumber(), timeTextField.getText(), descriptionTextField.getText()));
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 1;
		panel.add(submitButton, gbc_btnNewButton);
		
		descritionLabel = new JLabel();
		descritionLabel.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_6));
		GridBagConstraints gbc_descritionLabel = new GridBagConstraints();
		gbc_descritionLabel.insets = new Insets(0, 0, 0, 5);
		gbc_descritionLabel.anchor = GridBagConstraints.EAST;
		gbc_descritionLabel.gridx = 1;
		gbc_descritionLabel.gridy = 1;
		panel.add(descritionLabel, gbc_descritionLabel);
		
		descriptionTextField = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 1;
		panel.add(descriptionTextField, gbc_textField_1);
		descriptionTextField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		clearButton = new JButton();
		clearButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_7));
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireClearAttendanceOffset();
			}
		});
		GridBagConstraints gbc_clearButton = new GridBagConstraints();
		gbc_clearButton.fill = GridBagConstraints.BOTH;
		gbc_clearButton.insets = new Insets(0, 0, 5, 0);
		gbc_clearButton.gridx = 0;
		gbc_clearButton.gridy = 1;
		panel_1.add(clearButton, gbc_clearButton);
		
		saveButton = new JButton();
		saveButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_8));
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireSaveAttendanceOffset();
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_2.gridx = 0;
		gbc_btnNewButton_2.gridy = 2;
		panel_1.add(saveButton, gbc_btnNewButton_2);
		
		loadButton = new JButton();
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireLoadAttendanceOffset();
			}
		});
		loadButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_9));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.fill = GridBagConstraints.BOTH;
		gbc_button.insets = new Insets(0, 0, 5, 0);
		gbc_button.gridx = 0;
		gbc_button.gridy = 3;
		panel_1.add(loadButton, gbc_button);
		
		if(Objects.nonNull(attendanceOffsetModel)){
			attendanceOffsetModel.addObverser(attendanceOffsetObverser);
			attendanceOffsetModel.getLock().readLock().lock();
			try{
				for(AttendanceOffset attendanceOffset : attendanceOffsetModel){
					tableModel.addRow(new Object[]{
							FormatUtil.formatPerson(attendanceOffset.getPerson()),
							attendanceOffset.getValue(),
							attendanceOffset.getDescription(),
						});
				}
			}finally {
				attendanceOffsetModel.getLock().readLock().unlock();
			}
		}
		
		this.attendanceOffsetModel = attendanceOffsetModel;
		
		if(Objects.nonNull(countResultModel)){
			countResultModel.addObverser(countResultObverser);
			countResultModel.getLock().readLock().lock();
			try{
				for(CountResult countResult : countResultModel){
					comboBoxModel.addElement(countResult.getPerson());
				}
			}finally {
				countResultModel.getLock().readLock().unlock();
			}
		}
		
		this.countResultModel = countResultModel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#getObversers()
	 */
	@Override
	public Set<AttendanceOffsetPanelObverser> getObversers() {
		return Collections.unmodifiableSet(obversers);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#addObverser(com.dwarfeng.dutil.basic.prog.Obverser)
	 */
	@Override
	public boolean addObverser(AttendanceOffsetPanelObverser obverser) {
		return obversers.add(obverser);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#removeObverser(com.dwarfeng.dutil.basic.prog.Obverser)
	 */
	@Override
	public boolean removeObverser(AttendanceOffsetPanelObverser obverser) {
		return obversers.remove(obverser);
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#clearObverser()
	 */
	@Override
	public void clearObverser() {
		obversers.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.jier.mh4w.core.model.struct.MutilangSupported#getMutilang()
	 */
	@Override
	public Mutilang getMutilang() {
		return mutilang;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.jier.mh4w.core.model.struct.MutilangSupported#updateMutilang()
	 */
	@Override
	public void updateMutilang() {
		//���¸���ǩ���ı���
		table.getColumnModel().getColumn(0).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_1));
		table.getColumnModel().getColumn(1).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_2));
		table.getColumnModel().getColumn(2).setHeaderValue(getLabel(LabelStringKey.JAttendanceOffsetPanel_3));
		
		timeLabel.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_5));
		descritionLabel.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_6));
		
		submitButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_4));
		clearButton.setText(getLabel(LabelStringKey.JAttendanceOffsetPanel_7));

	}

	/**
	 * @return the attendanceOffsetModel
	 */
	public DataListModel<AttendanceOffset> getAttendanceOffsetModel() {
		return attendanceOffsetModel;
	}

	/**
	 * @param attendanceOffsetModel the attendanceOffsetModel to set
	 */
	public void setAttendanceOffsetModel(DataListModel<AttendanceOffset> attendanceOffsetModel) {
		int count = tableModel.getRowCount();
		for(int i = 0 ; i < count ; i ++){
			tableModel.removeRow(0);
		}
		
		if(Objects.nonNull(this.attendanceOffsetModel)){
			this.attendanceOffsetModel.removeObverser(attendanceOffsetObverser);
		}
		
		if(Objects.nonNull(attendanceOffsetModel)){
			attendanceOffsetModel.addObverser(attendanceOffsetObverser);
			attendanceOffsetModel.getLock().readLock().lock();
			try{
				for(AttendanceOffset attendanceOffset : attendanceOffsetModel){
					tableModel.addRow(new Object[]{
							FormatUtil.formatPerson(attendanceOffset.getPerson()),
							attendanceOffset.getValue(),
							attendanceOffset.getDescription(),
						});
				}
			}finally {
				attendanceOffsetModel.getLock().readLock().unlock();
			}
		}
		
		this.attendanceOffsetModel = attendanceOffsetModel;
	}


	/**
	 * @return the countResultModel
	 */
	public DataListModel<CountResult> getCountResultModel() {
		return countResultModel;
	}

	/**
	 * @param countResultModel the countResultModel to set
	 */
	public void setCountResultModel(DataListModel<CountResult> countResultModel) {
		comboBoxModel.removeAllElements();
		
		if(Objects.nonNull(this.countResultModel)){
			this.countResultModel.removeObverser(countResultObverser);
		}
		
		if(Objects.nonNull(countResultModel)){
			countResultModel.addObverser(countResultObverser);
			countResultModel.getLock().readLock().lock();
			try{
				for(CountResult countResult : countResultModel){
					comboBoxModel.addElement(countResult.getPerson());
				}
			}finally {
				countResultModel.getLock().readLock().unlock();
			}
		}
		
		this.countResultModel = countResultModel;
		
	}

	/**
	 * �ͷ���Դ��
	 */
	public void dispose() {
		if(Objects.nonNull(attendanceOffsetModel)){
			attendanceOffsetModel.removeObverser(attendanceOffsetObverser);
		}
		if(Objects.nonNull(countResultModel)){
			countResultModel.removeObverser(countResultObverser);
		}
	}

	private void fireSubmitAttendanceOffset(UnsafeAttendanceOffset unsafeAttendanceOffset) {
		for(AttendanceOffsetPanelObverser obverser : obversers){
			if(Objects.nonNull(obverser)) obverser.fireSubmitAttendanceOffset(unsafeAttendanceOffset);
		}
	}

	private void fireClearAttendanceOffset() {
		for(AttendanceOffsetPanelObverser obverser : obversers){
			if(Objects.nonNull(obverser)) obverser.fireClearAttendanceOffset();
		}
	}

	private void fireSaveAttendanceOffset() {
		for(AttendanceOffsetPanelObverser obverser : obversers){
			if(Objects.nonNull(obverser)) obverser.fireSaveAttendanceOffset();
		}
	}

	private void fireLoadAttendanceOffset() {
		for(AttendanceOffsetPanelObverser obverser : obversers){
			if(Objects.nonNull(obverser)) obverser.fireLoadAttendanceOffset();
		}
	}

	private void fireRemoveAttendanceOffset(int index) {
		for(AttendanceOffsetPanelObverser obverser : obversers){
			if(Objects.nonNull(obverser)) obverser.fireRemoveAttendanceOffset(index);
		}
	}

	private String getLabel(LabelStringKey labelStringKey){
		return mutilang.getString(labelStringKey.getName());
	}

}