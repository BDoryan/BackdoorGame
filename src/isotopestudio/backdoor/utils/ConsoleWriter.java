package isotopestudio.backdoor.utils;

import org.joml.Vector4i;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class ConsoleWriter {

	private final char ANGLE_LEFT_TOP = '╔';
	private final char ANGLE_LEFT_BOTTOM = '╚';
	private final char ANGLE_RIGHT_TOP = '╗';
	private final char ANGLE_RIGHT_BOTTOM = '╝';
	private final char ANGLE_MIDDLLE_LEFT = '╠';
	private final char ANGLE_MIDDLLE_RIGHT = '╣';

	private final char VERTICAL_LINE = '║';
	private final char HORIZONTAL_LINE = '═';

	private StringBuilder builder = new StringBuilder();

	/**
	 * 
	 * Check the documentation for use 'spacing variable'
	 * 
	 * @param content
	 * @param spacing x (left), y (top), z (right), w (bottom);
	 */
	public void addContainer(String content) {
		addContainer(content, new Vector4i(2, 2, 2, 2));
	}

	/**
	 * 
	 * Check the documentation for use 'spacing variable'
	 * 
	 * @param content
	 * @param spacing x (left), y (top), z (right), w (bottom);
	 */
	public void addContainer(String content, Vector4i spacing) {
		String[] contentLines = content.split("\n");
		int max = 0;
		for (String line : contentLines) {
			if (max < line.length()) {
				max = line.length();
			}
		}
		max += spacing.x + spacing.z;

		StringBuilder top_line = new StringBuilder();
		StringBuilder bottom_line = new StringBuilder();

		StringBuilder empty_line = new StringBuilder();

		top_line.append(ANGLE_LEFT_TOP);
		bottom_line.append(ANGLE_LEFT_BOTTOM);
		empty_line.append(VERTICAL_LINE);
		for (int i = 0; i < max; i++) {
			top_line.append(HORIZONTAL_LINE);
			bottom_line.append(HORIZONTAL_LINE);
			empty_line.append(' ');
		}
		top_line.append(ANGLE_RIGHT_TOP);
		top_line.append("\n");
		bottom_line.append(ANGLE_RIGHT_BOTTOM);
		bottom_line.append("\n");
		empty_line.append(VERTICAL_LINE);
		empty_line.append("\n");

		builder.append(top_line);
		for (int i = 0; i < spacing.y; i++) {
			builder.append(empty_line);
		}
		for (String line : contentLines) {
			StringBuilder builder = new StringBuilder();
			builder.append(VERTICAL_LINE);
			for (int i = 0; i < spacing.x; i++) {
				builder.append(' ');
			}
			builder.append(line);
			for (int i = builder.length(); i < max + 1 /* for left space */; i++) {
				builder.append(' ');
			}
			builder.append(VERTICAL_LINE);
			builder.append("\n");
			this.builder.append(builder.toString());
		}
		for (int i = 0; i < spacing.w; i++) {
			builder.append(empty_line);
		}
		builder.append(bottom_line);
	}

	public void addTitle(int title_position, String title, Vector4i content_spacing) {
		addTitle(title_position, 0, title, content_spacing);
	}

	public void addTitle(int title_position, int left_space, String title, Vector4i content_spacing) {
		String space_left_string = "";

		for (int i = 0; i < left_space; i++) {
			space_left_string += ' ';
		}

		int max = content_spacing.x + title.length() + content_spacing.z;
		
		String line = "";
		for (int i = 0; i < max; i++) {
			line += "_";
		}

		builder.append(space_left_string + line + "\n");

		for (int i = 0; i < content_spacing.y; i++) {
			builder.append("\n");
		}
		if (title_position == POSITION_LEFT) {
			StringBuilder builder = new StringBuilder();
			builder.append(' ');
			builder.append(title);
			for (int i = builder.length(); i < max /* for left space */; i++) {
				builder.append(' ');
			}
			this.builder.append(space_left_string + builder.toString() + "\n");
		} else if (title_position == POSITION_CENTER) {
			int width = max;
			int start_x = (int) ((double) (width / 2) - (title.length() / 2));

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < width; i++) {
				if (i == start_x) {
					builder.append(title);
					i += title.length() - 1;
				} else {
					builder.append(' ');
				}
			}
			for (int i = builder.length(); i < max /* for left space */; i++) {
				builder.append(' ');
			}
			this.builder.append(space_left_string + builder.toString() + "\n");
		} else if (title_position == POSITION_RIGHT) {
			StringBuilder builder = new StringBuilder();
			builder.append(' ');
			for (int i = builder.length(); i < max - title.length() - 1 /* for left space */; i++) {
				builder.append(' ');
			}
			builder.append(title);
			builder.append(' ');
			this.builder.append(space_left_string + builder.toString() + "\n");
		}

		for (int i = 0; i < content_spacing.w; i++) {
			builder.append(" \n");
		}
		builder.append(space_left_string + line + "\n");
	}

	public static int POSITION_LEFT = 0;
	public static int POSITION_CENTER = 1;
	public static int POSITION_RIGHT = 2;

	/**
	 * 
	 * @param title_position
	 * @param title
	 * @param content
	 * @param content_spacing
	 */
	public void addTitleContainer(int title_position, String title, String content, Vector4i content_spacing) {
		addTitleContainer(title_position, title, 0, content, content_spacing);
	}

	/**
	 * 
	 * @param title_position
	 * @param title
	 * @param left_space
	 * @param content
	 * @param content_spacing
	 */
	public void addTitleContainer(int title_position, String title, int left_space, String content,
			Vector4i content_spacing) {
		String space_left_string = "";

		for (int i = 0; i < left_space; i++) {
			space_left_string += ' ';
		}

		String[] contentLines = content.split("\n");
		int max = 0;
		for (String line : contentLines) {
			if (max < line.length()) {
				max = line.length();
			}
		}
		if (max < title.length()) {
			max = title.length();
		}
		max += content_spacing.x + content_spacing.z;

		StringBuilder top_line = new StringBuilder();
		StringBuilder bottom_line = new StringBuilder();
		StringBuilder middle_line = new StringBuilder();
		StringBuilder empty_line = new StringBuilder();

		top_line.append(space_left_string + ANGLE_LEFT_TOP);
		bottom_line.append(space_left_string + ANGLE_LEFT_BOTTOM);
		empty_line.append(space_left_string + VERTICAL_LINE);
		middle_line.append(space_left_string + ANGLE_MIDDLLE_LEFT);
		for (int i = 0; i < max; i++) {
			top_line.append(HORIZONTAL_LINE);
			bottom_line.append(HORIZONTAL_LINE);
			middle_line.append(HORIZONTAL_LINE);
			empty_line.append(' ');
		}
		top_line.append(ANGLE_RIGHT_TOP + "\n");
		bottom_line.append(ANGLE_RIGHT_BOTTOM + "\n");
		empty_line.append(VERTICAL_LINE + "\n");
		middle_line.append(ANGLE_MIDDLLE_RIGHT + "\n");

		builder.append(top_line);
		builder.append(space_left_string + VERTICAL_LINE);
		if (title_position == POSITION_LEFT) {
			StringBuilder builder = new StringBuilder();
			builder.append(' ');
			builder.append(title);
			for (int i = builder.length(); i < max /* for left space */; i++) {
				builder.append(' ');
			}
			this.builder.append(builder.toString());
		} else if (title_position == POSITION_CENTER) {
			int width = max;
			int start_x = (int) ((double) (width / 2) - (title.length() / 2));

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < width; i++) {
				if (i == start_x) {
					builder.append(title);
					i += title.length() - 1;
				} else {
					builder.append(' ');
				}
			}
			for (int i = builder.length(); i < max /* for left space */; i++) {
				builder.append(' ');
			}
			this.builder.append(builder.toString());
		} else if (title_position == POSITION_RIGHT) {
			StringBuilder builder = new StringBuilder();
			builder.append(' ');
			for (int i = builder.length(); i < max - title.length() - 1 /* for left space */; i++) {
				builder.append(' ');
			}
			builder.append(title);
			builder.append(' ');
			this.builder.append(builder.toString());
		}
		builder.append(VERTICAL_LINE + "\n");

		builder.append(middle_line);
		for (int i = 0; i < content_spacing.y; i++) {
			builder.append(empty_line);
		}
		for (String line : contentLines) {
			StringBuilder builder = new StringBuilder();
			builder.append(VERTICAL_LINE);
			for (int i = 0; i < content_spacing.x; i++) {
				builder.append(' ');
			}
			builder.append(line);
			for (int i = builder.length(); i < max + 1 /* for left space */; i++) {
				builder.append(' ');
			}
			builder.append(VERTICAL_LINE);
			builder.append("\n");
			this.builder.append(space_left_string + builder.toString());
		}
		for (int i = 0; i < content_spacing.w; i++) {
			builder.append(empty_line);
		}
		builder.append(bottom_line);
	}

	public void addList(String title, String... list) {
		addList(title, 4, list);
	}

	public void addList(String title, int left_space, String... list) {
		builder.append(title + "\n");
		backToLine();
		for (String line : list) {
			for (int i = 0; i < left_space; i++) {
				builder.append(' ');
			}
			builder.append(line + "\n");
		}
	}

	public void addLine(String text) {
		builder.append(text + "\n");
	}

	public void backToLine() {
		builder.append("\n");
	}

	/**
	 * @return the builder
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

	public void write() {
		for (String line : build().split("\n")) {
			System.out.println(line);
		}
	}

	public String build() {
		return builder.toString();
	}
}
