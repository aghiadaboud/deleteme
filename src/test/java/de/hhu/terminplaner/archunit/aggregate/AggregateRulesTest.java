package de.hhu.terminplaner.archunit.aggregate;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.terminplaner.TerminplanerApplication;
import de.hhu.terminplaner.stereotype.AggregateRoot;

@AnalyzeClasses(packagesOf = TerminplanerApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class AggregateRulesTest {


  @ArchTest
  public static ArchRule aggregateRootVisibilityRule =
      classes().that().areAnnotatedWith(AggregateRoot.class).should().bePublic();


//
//  @ArchTest
//  static final ArchRule oneAggregateRootPerAggregate = slices()
//      .matching("..model.(*)..")
//      .should(HAVE_EXACTLY_ONE_AGGREGATE_ROOT);
//
//  @ArchTest
//  static final ArchRule onlyAggregateRootsArePublic = classes()
//      .that()
//      .areNotAnnotatedWith(AggregateRoot.class)
//      .and()
//      .resideInAPackage("..model.(*)..")
//      .should()
//      .notBePublic();

}
