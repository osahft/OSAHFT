package com.osahft.api.internal.assertion;

import com.osahft.api.model.CreateMailTransferRequest;

/**
 * {@link CreateMailTransferRequest.CreateMailTransferRequestBuilder} specific assertions - Generated by CustomAssertionGenerator.
 * <p>
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractCreateMailTransferRequestCreateMailTransferRequestBuilderAssert} instead.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class CreateMailTransferRequestCreateMailTransferRequestBuilderAssert extends AbstractCreateMailTransferRequestCreateMailTransferRequestBuilderAssert<CreateMailTransferRequestCreateMailTransferRequestBuilderAssert, CreateMailTransferRequest.CreateMailTransferRequestBuilder> {

    /**
     * Creates a new <code>{@link CreateMailTransferRequestCreateMailTransferRequestBuilderAssert}</code> to make assertions on actual CreateMailTransferRequest.CreateMailTransferRequestBuilder.
     *
     * @param actual the CreateMailTransferRequest.CreateMailTransferRequestBuilder we want to make assertions on.
     */
    public CreateMailTransferRequestCreateMailTransferRequestBuilderAssert(CreateMailTransferRequest.CreateMailTransferRequestBuilder actual) {
        super(actual, CreateMailTransferRequestCreateMailTransferRequestBuilderAssert.class);
    }

    /**
     * An entry point for CreateMailTransferRequestCreateMailTransferRequestBuilderAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
     * With a static import, one can write directly: <code>assertThat(myCreateMailTransferRequest.CreateMailTransferRequestBuilder)</code> and get specific assertion with code completion.
     *
     * @param actual the CreateMailTransferRequest.CreateMailTransferRequestBuilder we want to make assertions on.
     * @return a new <code>{@link CreateMailTransferRequestCreateMailTransferRequestBuilderAssert}</code>
     */
    @org.assertj.core.util.CheckReturnValue
    public static CreateMailTransferRequestCreateMailTransferRequestBuilderAssert assertThat(CreateMailTransferRequest.CreateMailTransferRequestBuilder actual) {
        return new CreateMailTransferRequestCreateMailTransferRequestBuilderAssert(actual);
    }
}