package de.hhu.propra.terminplaner.archunit.aggregate;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static de.hhu.propra.terminplaner.archunit.aggregate.rules.HaveExactlyOneAggregateRoot.HAVE_EXACTLY_ONE_AGGREGATE_ROOT;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.terminplaner.TerminplanerApplication;
import de.hhu.propra.terminplaner.stereotype.AggregateRoot;

@AnalyzeClasses(packagesOf = TerminplanerApplication.class, importOptions =
    ImportOption.DoNotIncludeTests.class)
public class AggregateRulesTest {


  @ArchTest
  static final ArchRule oneAggregateRootPerAggregate = slices()
      .matching("..domain.(*)..")
      .should(HAVE_EXACTLY_ONE_AGGREGATE_ROOT);
  @ArchTest
  static final ArchRule onlyAggregateRootsArePublic = classes()
      .that()
      .areNotAnnotatedWith(AggregateRoot.class)
      .and()
      .resideInAPackage("..domain.(*)..")
      .should()
      .notBePublic();
  @ArchTest
  public static ArchRule aggregateRootVisibilityRule =
      classes().that().areAnnotatedWith(AggregateRoot.class).should().bePublic();

}
