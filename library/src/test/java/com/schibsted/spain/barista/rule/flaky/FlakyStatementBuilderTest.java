package com.schibsted.spain.barista.rule.flaky;

import com.schibsted.spain.barista.rule.flaky.internal.AllowFlakyStatement;
import com.schibsted.spain.barista.rule.flaky.internal.FlakyStatementBuilder;
import com.schibsted.spain.barista.rule.flaky.internal.RepeatStatement;
import java.lang.annotation.Annotation;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class FlakyStatementBuilderTest {

  private static final String CLASS = "class name";
  private static final String TEST = "test name";

  @Test(expected = IllegalStateException.class)
  public void allowFlakyAndRepeatAreNotAllowedTogether() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = withAnnotations(allowFlaky(), repeat());

    createStatement(baseStatement, description);
  }

  @Test
  public void sameStatementReturnedWhenNoAnnotationsFound() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = Description.EMPTY;

    Statement resultStatement = createStatement(baseStatement, description);

    assertEquals(baseStatement, resultStatement);
  }

  @Test
  public void repeatStatementReturnedWhenRepeatAnnotationFound() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = withAnnotations(repeat());

    Statement resultStatement = createStatement(baseStatement, description);

    assertTrue(resultStatement instanceof RepeatStatement);
  }

  @Test
  public void repeatStatementReturnedWhenSettingDefaultRepeatAttempts() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = Description.EMPTY;

    Statement resultStatement = createStatementWithRepeatAttemptsByDefault(baseStatement, description);

    assertTrue(resultStatement instanceof RepeatStatement);
  }

  @Test
  public void allowFlakyStatementReturnedWhenAllowFlakyAnnotationFound() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = withAnnotations(allowFlaky());

    Statement resultStatement = createStatement(baseStatement, description);

    assertTrue(resultStatement instanceof AllowFlakyStatement);
  }

  @Test
  public void allowFlakyStatementReturnedWhenNoAnnotationsFoundButUsesDefault() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = Description.EMPTY;

    Statement resultStatement = createStatementWithAllowFlakyByDefault(baseStatement, description);

    assertTrue(resultStatement instanceof AllowFlakyStatement);
  }

  @Test
  public void lastStatementReturnedWhenDefaultRepeatAttemptsAndAllowFlakyStatementUsedAtTheSameTime() throws Exception {
    Statement baseStatement = new SomeStatement();
    Description description = Description.EMPTY;

    Statement resultStatement = createStatementWithFlakyAndReturn(baseStatement, description);

    assertTrue(resultStatement instanceof RepeatStatement);
  }

  private Statement createStatementWithFlakyAndReturn(Statement baseStatement, Description description) {
    return new FlakyStatementBuilder()
        .setBase(baseStatement)
        .setDescription(description)
        .allowFlakyAttemptsByDefault(5)
        .setRepeatAttemptsByDefault(3)
        .build();
  }

  private Statement createStatementWithAllowFlakyByDefault(Statement baseStatement, Description description) {
    return new FlakyStatementBuilder()
        .setBase(baseStatement)
        .setDescription(description)
        .allowFlakyAttemptsByDefault(5)
        .build();
  }

  //region Shortcut methods
  private Statement createStatement(Statement baseStatement, Description description) {
    return new FlakyStatementBuilder()
        .setBase(baseStatement)
        .setDescription(description)
        .build();
  }

  private Statement createStatementWithRepeatAttemptsByDefault(Statement baseStatement, Description description) {
    return new FlakyStatementBuilder()
        .setBase(baseStatement)
        .setDescription(description)
        .setRepeatAttemptsByDefault(3)
        .build();
  }

  private Description withAnnotations(Annotation... annotations) {
    return Description.createTestDescription(CLASS, TEST, annotations);
  }

  private Repeat repeat() {
    return new Repeat() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return Repeat.class;
      }

      @Override
      public int times() {
        return 0;
      }
    };
  }

  private AllowFlaky allowFlaky() {
    return new AllowFlaky() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return AllowFlaky.class;
      }

      @Override
      public int attempts() {
        return 0;
      }
    };
  }

  private static class SomeStatement extends Statement {
    @Override
    public void evaluate() throws Throwable {
      // NA
    }
  }
  //endregion
}
