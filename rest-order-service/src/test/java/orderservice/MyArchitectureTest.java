package orderservice;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import org.junit.jupiter.api.Test;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

public class MyArchitectureTest {

	 @Test
	    void layeredArchitectureIsRespected() {
	        JavaClasses importedClasses =
	                new ClassFileImporter().importPackages("orderservice");

	        ArchRule rule = layeredArchitecture()
	                .consideringAllDependencies()
	                .layer("Controller").definedBy("..controller..")
	                .layer("Service").definedBy("..service..")
	                .layer("Repository").definedBy("..repository..");
	                
//	                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
//	                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
//	                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
//
	        rule.check(importedClasses);
	    }
}