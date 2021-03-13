package de.hhu.propra.terminplaner.archunit.strucktur;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.terminplaner.TerminplanerApplication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packagesOf = TerminplanerApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class SchichtenArchitektur {


  @ArchTest
  static final ArchRule schichten_architektur = layeredArchitecture()

      .layer("UI/Controllers").definedBy("de.hhu.propra.terminplaner.controller..")
      .layer("Business/Services").definedBy("de.hhu.propra.terminplaner.service..")
      //.layer("Domain").definedBy("de.hhu.propra.terminplaner.domain.(*)..")
      .layer("Persistence/Repositories").definedBy("de.hhu.propra.terminplaner.repos..")

      .whereLayer("UI/Controllers").mayNotBeAccessedByAnyLayer()
      .whereLayer("Business/Services").mayOnlyBeAccessedByLayers("UI/Controllers")
      //.whereLayer("Domain").mayOnlyBeAccessedByLayers("Business/Services")
      .whereLayer("Persistence/Repositories").mayOnlyBeAccessedByLayers("Business/Services")
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
