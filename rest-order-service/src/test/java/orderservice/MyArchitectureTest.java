package orderservice;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue; // REQUIRED
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage; // REQUIRED
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import org.junit.jupiter.api.Test;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

public class MyArchitectureTest {


  @Test
  public void layeredArchitectureIsRespected() {
    JavaClasses importedClasses = new ClassFileImporter()
        // 1. Standard exclusion
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        // 2. FORCE exclude your specific test files by name pattern
        .withImportOption(location -> !location.contains("Test")).importPackages("orderservice");

    ArchRule rule = layeredArchitecture().consideringAllDependencies().layer("controller")
        .definedBy("..controller..").layer("service").definedBy("..service..").layer("repository")
        .definedBy("..repository..").layer("config").definedBy("..config..")

        .ignoreDependency(alwaysTrue(), resideInAnyPackage("..entity.."))

        .whereLayer("controller").mayNotBeAccessedByAnyLayer().whereLayer("service")
        .mayOnlyBeAccessedByLayers("controller", "config").whereLayer("repository")
        .mayOnlyBeAccessedByLayers("service", "config");

    rule.check(importedClasses);
  }

}
