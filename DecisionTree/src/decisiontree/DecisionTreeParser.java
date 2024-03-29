package decisiontree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class DecisionTreeParser {
	public static Relation parse(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			String relation_name = null;
			ArrayList<Attribute> attributes = new ArrayList<Attribute>();
			LinkedList<Example> data = new LinkedList<Example>();
			
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty() && !line.startsWith("%") && !line.startsWith("@data")) {
					line = line.toLowerCase();
					String[] split = line.split("\\s+");
					if (split[0].equals("@relation")) {
						relation_name = split[1];
					} else if (split[0].equals("@attribute")) {
						String attr_name = split[1];
						HashSet<String> values = new HashSet<String>();
						for (int i = 2; i < split.length; i++) {
							String value = split[i].replace("{", "").replace("}", "").replace(",", "");
							values.add(value);
						}
						attributes.add(new Attribute(attr_name, values));
					} else {
						String noSpaceLine = line.replace("\\s+", "");
						String[] data_file = noSpaceLine.split(",");
						HashMap<Attribute, String> mapping = new HashMap<Attribute, String>();
						for(int i = 0; i < data_file.length; i++) {
							mapping.put(attributes.get(i), data_file[i]);
						}
						Attribute goal_attr = attributes.get(data_file.length - 1);
						String goal_value = data_file[data_file.length - 1];
						Goal goal = new Goal(goal_attr, goal_value);
						data.add(new Example(mapping, goal));
					}
				}
			}
			reader.close();
			return new Relation(relation_name, attributes, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
