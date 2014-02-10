package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class CellRenderer extends DefaultTableCellRenderer {

	private Color color;

	public CellRenderer(Color color) {
		super();

		this.color = color;

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		c.setForeground(color);

		return c;
	}
}