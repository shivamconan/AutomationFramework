package utility;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YAMLFileUtility {
	
	private static ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper(new YAMLFactory());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private static LogUtility logUtility = new LogUtility(YAMLFileUtility.class);
	
	public static <T> void convertYAMLToJava(String filePath, Class<T> cls) {
		T result = null; 
		try {
			mapper.findAndRegisterModules();
			result = mapper.readValue(new File(filePath), cls);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
