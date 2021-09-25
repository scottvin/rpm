package com.github.scottvin.rpm;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.github.scottvin.rpm");

        noClasses()
            .that()
            .resideInAnyPackage("com.github.scottvin.rpm.service..")
            .or()
            .resideInAnyPackage("com.github.scottvin.rpm.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.github.scottvin.rpm.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
