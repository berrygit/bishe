package berry;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import berry.engine.parser.XmlWorkflowSequenceParser;

@ComponentScan
@Configuration
@PropertySource("classpath:conf/workflow.properties")
public class ProjectBootstrap {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new AnnotationConfigApplicationContext(ProjectBootstrap.class);
		
		Map<String, XmlWorkflowSequenceParser> person = context.getBeansOfType(XmlWorkflowSequenceParser.class);
		
		//Map<String, WorkflowInstanceModel> map = person.values().iterator().next().parse("src/main/resources/workflow");
		
		System.out.println(person.values().iterator().next());
		
	}

}
