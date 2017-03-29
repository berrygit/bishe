package berry.engine.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.xml.sax.XMLReader;

import berry.engine.model.interfaces.Instance;

@Component("parser")
public class XmlWorkflowSequenceParser implements Parser, ApplicationContextAware {

	private XMLReader reader;

	private XmlWorkflowInstanceHandler handler;

	private ApplicationContext context;

	private Map<String, Instance> instanceCache = new HashMap<String, Instance>();

	@PostConstruct
	public void init() throws Exception {
		// 创建解析工厂
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// 创建解析器
		SAXParser parser = factory.newSAXParser();
		// 得到读取器
		reader = parser.getXMLReader();

		// 设置内容处理器
		handler = new XmlWorkflowInstanceHandler(this.context);

		reader.setContentHandler(handler);

	}

	private String[] getFileList(String filepath) {

		File file = new File(filepath);

		if (!file.isDirectory()) {

			throw new IllegalStateException("无法找到文件");

		} else {
			String[] filelist = file.list();

			return filelist;
		}

	}

	@Override
	public Map<String, Instance> parse(String path) throws Exception {

		String[] fileList = getFileList(path);

		if (fileList != null) {
			for (String file : fileList) {

				String fileName = path + "/" + file;

				// 读取xml文档
				reader.parse(fileName);

				Instance instance = handler.getInstance();

				instanceCache.put(instance.getName(), instance);

			}
		}

		return instanceCache;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

}
