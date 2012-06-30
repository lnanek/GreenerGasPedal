package name.nanek.greenerpedal.activity.support;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
 
public class MessageOnlyFormatter extends Formatter {
	
	private static final String lineSep = System.getProperty("line.separator");

	public String format(LogRecord record) {
		StringBuilder output = new StringBuilder()
			.append(record.getMessage())
			.append(lineSep);
		return output.toString();		
	}
 
}