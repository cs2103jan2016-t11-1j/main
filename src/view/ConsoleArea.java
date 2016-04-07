package view;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

public class ConsoleArea extends OutputStream {
	private TextArea out;

	public ConsoleArea(TextArea out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.appendText(String.valueOf((char) b));
	}

}
