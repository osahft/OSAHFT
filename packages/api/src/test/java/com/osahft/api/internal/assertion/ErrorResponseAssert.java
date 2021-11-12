package com.osahft.api.internal.assertion;

import com.osahft.api.model.ErrorResponse;

/**
 * {@link ErrorResponse} specific assertions - Generated by CustomAssertionGenerator.
 * <p>
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractErrorResponseAssert} instead.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class ErrorResponseAssert extends AbstractErrorResponseAssert<ErrorResponseAssert, ErrorResponse> {

  /**
   * Creates a new <code>{@link ErrorResponseAssert}</code> to make assertions on actual ErrorResponse.
   *
   * @param actual the ErrorResponse we want to make assertions on.
   */
  public ErrorResponseAssert(ErrorResponse actual) {
    super(actual, ErrorResponseAssert.class);
  }

  /**
   * An entry point for ErrorResponseAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myErrorResponse)</code> and get specific assertion with code completion.
   *
   * @param actual the ErrorResponse we want to make assertions on.
   * @return a new <code>{@link ErrorResponseAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static ErrorResponseAssert assertThat(ErrorResponse actual) {
    return new ErrorResponseAssert(actual);
  }
}
