package de.hhu.terminplaner.archunit.strucktur;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.terminplaner.TerminplanerApplication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packagesOf = TerminplanerApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class SchichtenArchitektur {


  @ArchTest
  static final ArchRule schichten_architektur = layeredArchitecture()

      .layer("Controllers").definedBy("de.hhu.terminplaner.controller..")
      .layer("Services").definedBy("de.hhu.terminplaner.service..")
      .layer("Model").definedBy("de.hhu.terminplaner.model.(*)..")
      .layer("Repositories").definedBy("de.hhu.terminplaner.repos..")

      .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
      .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
      .whereLayer("Model").mayOnlyBeAccessedByLayers("Services")
      .whereLayer("Repositories").mayOnlyBeAccessedByLayers("Services")
      .because("Abhängigkeiten zwischen Schichten haben klare Richtung");


  @ArchTest
  final ArchRule controllerClassNamesShouldEndWithController = classes()
      .that().areAnnotatedWith(Controller.class).or().areMetaAnnotatedWith(Controller.class)
      .should().haveSimpleNameEndingWith("Controller").andShould()
      .resideInAPackage("..controller..");


  @ArchTest
  final ArchRule serviceClassNamesShouldEndWithService = classes()
      .that().areAnnotatedWith(Service.class).or().areMetaAnnotatedWith(Service.class)
      .should().haveSimpleNameEndingWith("Service").andShould()
      .resideInAPackage("..service..");


}
