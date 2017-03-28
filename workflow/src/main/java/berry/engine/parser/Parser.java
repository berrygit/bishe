package berry.engine.parser;

import java.util.Map;

import berry.engine.model.interfaces.Instance;

public interface Parser {

	Map<String, Instance> parse(String fileName) throws Exception;

}
