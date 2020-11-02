package isotopestudio.backdoor.engine.components.painting;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Component;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class MultiLine {

	private Line[] lines;

	/* 
	1 1, #0
	2 2, #1 line 1 #0
	3 3, #2 line 2 #1
	4 4  #3 line 3 #2
	*/
	public MultiLine(int thickness, Vector2f... locations) {
		if(locations == null || locations.length < 2)throw new IllegalStateException("You need a minimum of 3 lines for use a multilines!");
		
		this.lines = new Line[locations.length - 1];
		
		for(int i = 0; i < this.lines.length; i++) {
			Vector2f location1 = locations[i];
			Vector2f location2 = locations[i + 1];
			
			Line line = new Line(thickness, location1.x, location1.y, location2.x, location2.y);
			lines[i] = line;
		}
	}

	public MultiLine(Vector2f... locations) {
		this(1, locations);
	}
	
	/**
	 * @return the lines
	 */
	public Line[] getLines() {
		return lines;
	}
	
	public void addLines(Component parent) {
		for(Line line : lines)
			parent.add(line);
	}
	
	public void removeLines(Component parent) {
		for(Line line : lines)
			parent.remove(line);
	}
}
