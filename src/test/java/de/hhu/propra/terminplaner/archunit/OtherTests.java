package de.hhu.propra.terminplaner.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.members;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.terminplaner.TerminplanerApplication;
import org.springframework.beans.factory.annotation.Autowired;

@AnalyzeClasses(packagesOf = TerminplanerApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class OtherTests {

  @ArchTest
  final ArchRule noDeprecatedClasses = classes()
      .should().notBeAnnotatedWith(Deprecated.class);
  @ArchTest
  final ArchRule noDeprecatedMembers = members()
      .should().notBeAnnotatedWith(Deprecated.class);
  @ArchTest
  final ArchRule fieldsPrivate = fields()
      .should().bePrivate();


  @ArchTest
  final ArchRule noFieldInjection = fields()
      .should().notBeAnnotatedWith(Autowired.class)
      .andShould().notBeMetaAnnotatedWith(Autowired.class)
      .because("field injection is not allowed");

  @ArchTest
  final ArchRule noSetterInjection = methods()
      .should().notBeAnnotatedWith(Autowired.class)
      .andShould().notBeMetaAnnotatedWith(Autowired.class)
      .because("setter injection is not allowed");
}
