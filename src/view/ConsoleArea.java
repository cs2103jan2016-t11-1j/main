package view;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

public class ConsoleArea extends OutputStream {
	private FlexiArea out;

	public ConsoleArea(FlexiArea out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.println(String.valueOf((char) b));
	}

}
