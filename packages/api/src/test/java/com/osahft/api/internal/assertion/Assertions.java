package com.osahft.api.internal.assertion;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class Assertions {

    /**
     * Creates a new instance of <code>{@link com.osahft.api.model.SoftwareVersionInformationAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @org.assertj.core.util.CheckReturnValue
    public static SoftwareVersionInformationAssert assertThat(com.osahft.api.model.SoftwareVersionInformation actual) {
        return new SoftwareVersionInformationAssert(actual);
    }

    /**
     * Creates a new <code>{@link Assertions}</code>.
     */
    protected Assertions() {
        // empty
    }
}
