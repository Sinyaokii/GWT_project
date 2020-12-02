package com.gwt.client;

import java.util.Random;

import com.google.gwt.core.client.EntryPoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Gwt implements EntryPoint {

	private final Label errorLabel = new Label();
	private final VerticalPanel mainPanel = new VerticalPanel();

	public void onModuleLoad() {
		prepairMainPanel();

		enterNumberScreen();

	}

	private void prepairMainPanel() {
		errorLabel.setStyleName("errorLabelField");
		RootPanel.get("container").add(mainPanel);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	}

	private void enterNumberScreen() {
		mainPanel.clear();

		final TextBox numberField = new TextBox();
		final Label label = new Label();
		final Button enterButton = new Button("Enter");

		// adding styles for widgets

		numberField.setStyleName("numberField");
		label.setStyleName("labelField");
		enterButton.getElement().getStyle().setBackgroundColor("blue");
		enterButton.addStyleName("blueButton");

		label.setText("How many numbers to display?");

		// adding widgets on panel

		mainPanel.add(label);
		mainPanel.add(numberField);
		mainPanel.add(enterButton);

		class MyHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				actionOnClick();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					actionOnClick();
				}
			}

			private void actionOnClick() {
				String data = numberField.getText();
				numberField.setText("");

				if (data.matches("(^|-)[0-9]*")) {
					int number = Integer.parseInt(data);

					if (number < 1) {
						errorLabel.setText("Please enter a positive value");
					} else if (number > 30) {
						errorLabel.setText("Please select a value smaller or equal to 30");
					} else if (number > 0) {
						errorLabel.setText("");
						sortScreen(number);

						errorLabel.setVisible(false);

					}
				} else if (data.length() < 1) {
					errorLabel.setText("Please enter value");
				} else {
					errorLabel.setText("Please enter number value");

				}

				mainPanel.add(errorLabel);

			}
		}

		MyHandler handler = new MyHandler();

		enterButton.addClickHandler(handler);
		numberField.addKeyUpHandler(handler);

	}

	private void sortScreen(int arraySize) {

		mainPanel.clear();
		mainPanel.add(errorLabel);
		mainPanel.add(createSortPanel(arraySize));

	}

	private Grid createSortPanel(int numberArraySize) {
		Grid grid = new Grid(10, 7);
		grid.setStyleName("gridStyle");
		int firstIndex = 0;

		// creating number buttons

		Button[] array = createButtonArray(numberArraySize);

		// creating control buttons

		Button sortButton = new Button("Sort");
		sortButton.getElement().getStyle().setBackgroundColor("green");
		sortButton.setStyleName("greenButton");
		sortButton.addClickHandler(new ClickHandler() {

			boolean reversArray = true;

			public void onClick(ClickEvent event) {
				sortButton.setEnabled(false);
				quickSort(array, 0, array.length - 1, reversArray);
				reversArray = !reversArray;
				sortButton.setEnabled(true);
			}
		});

		Button resetButton = new Button("Reset");
		resetButton.setStyleName("greenButton");
		resetButton.getElement().getStyle().setBackgroundColor("green");
		resetButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				enterNumberScreen();
			}
		});

		// adding number buttons to the grid

		for (int column = 0; column < grid.getColumnCount(); column++) {
			for (int row = 0; row < grid.getRowCount(); row++) {

				if (firstIndex < array.length) {
					grid.setWidget(row, column, array[firstIndex]);
					firstIndex++;
				}

				grid.getCellFormatter().setWidth(row, column, "130px");
			}
		}

		// adding control buttons to the grid

		grid.setWidget(0, grid.getColumnCount() - 1, sortButton);
		grid.setWidget(1, grid.getColumnCount() - 1, resetButton);

		return grid;
	}

	private Button[] createButtonArray(int size) {
		Button[] array = new Button[size];
		Random random = new Random();

		for (int i = 0; i < size; i++) {
			int value;

			if (i == 0) {
				value = random.nextInt(30) + 1;
			} else {
				value = random.nextInt(1000) + 1;
			}

			Button b = new Button(String.valueOf(value));
			b.setStyleName("blueButton");
			b.getElement().getStyle().setBackgroundColor("blue");
			b.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					int number = Integer.parseInt(b.getText());
					if (number <= 30) {
						sortScreen(number);
						errorLabel.setText("");
						errorLabel.setVisible(false);
					} else {
						errorLabel.setText("Please select a value smaller or equal to 30.");
						mainPanel.add(errorLabel);
						errorLabel.setVisible(true);
					}

				}
			});

			array[i] = b;
		}

		return array;
	}

	private void quickSort(Button[] array, int low, int high, boolean reverse) {
		int timerDelay = 1000;
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (array.length == 0)
					return;

				if (low >= high)
					return;

				int reverseCoef = 1;

				if (reverse) {
					reverseCoef = -1;
				}

				int middle = low + (high - low) / 2;
				int opora = reverseCoef * Integer.parseInt(array[middle].getText());

				int i = low, j = high;

				while (i <= j) {

					while (reverseCoef * Integer.parseInt(array[i].getText()) < opora) {
						i++;
					}

					while (reverseCoef * Integer.parseInt(array[j].getText()) > opora) {
						j--;
					}

					if (i <= j) {

						String tmp = array[i].getText();
						array[i].setText(array[j].getText());
						array[j].setText(tmp);

						i++;
						j--;

					}
				}

				if (low < j)
					quickSort(array, low, j, reverse);

				if (high > i)
					quickSort(array, i, high, reverse);
			}

		};
		timer.schedule(timerDelay);

	}

}