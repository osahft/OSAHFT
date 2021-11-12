package com.osahft.api.internal.assertion;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class Assertions {

  /**
   * Creates a new instance of <code>{@link MailReceiverDownloadLinkMappingAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static MailReceiverDownloadLinkMappingAssert assertThat(com.osahft.api.document.MailReceiverDownloadLinkMapping actual) {
    return new MailReceiverDownloadLinkMappingAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link MailReceiverDownloadLinkMappingMailReceiverDownloadLinkMappingBuilderAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static MailReceiverDownloadLinkMappingMailReceiverDownloadLinkMappingBuilderAssert assertThat(com.osahft.api.document.MailReceiverDownloadLinkMapping.MailReceiverDownloadLinkMappingBuilder actual) {
    return new MailReceiverDownloadLinkMappingMailReceiverDownloadLinkMappingBuilderAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link MailTransferAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static MailTransferAssert assertThat(com.osahft.api.document.MailTransfer actual) {
    return new MailTransferAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link MailTransferMailTransferBuilderAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static MailTransferMailTransferBuilderAssert assertThat(com.osahft.api.document.MailTransfer.MailTransferBuilder actual) {
    return new MailTransferMailTransferBuilderAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link MailTransferStateAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static MailTransferStateAssert assertThat(com.osahft.api.document.MailTransfer.State actual) {
    return new MailTransferStateAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CreateMailTransferRequestAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static CreateMailTransferRequestAssert assertThat(com.osahft.api.model.CreateMailTransferRequest actual) {
    return new CreateMailTransferRequestAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CreateMailTransferRequestCreateMailTransferRequestBuilderAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static CreateMailTransferRequestCreateMailTransferRequestBuilderAssert assertThat(com.osahft.api.model.CreateMailTransferRequest.CreateMailTransferRequestBuilder actual) {
    return new CreateMailTransferRequestCreateMailTransferRequestBuilderAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link CreateMailTransferResponseAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static CreateMailTransferResponseAssert assertThat(com.osahft.api.model.CreateMailTransferResponse actual) {
    return new CreateMailTransferResponseAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ErrorResponseAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static ErrorResponseAssert assertThat(com.osahft.api.model.ErrorResponse actual) {
    return new ErrorResponseAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link ErrorResponseErrorResponseBuilderAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static ErrorResponseErrorResponseBuilderAssert assertThat(com.osahft.api.model.ErrorResponse.ErrorResponseBuilder actual) {
    return new ErrorResponseErrorResponseBuilderAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link SoftwareVersionInformationAssert}</code>.
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
