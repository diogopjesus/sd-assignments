package commInfra;

/**
 * Memory exception.
 * Definition of an exception for access to a stack or a FIFO in the following
 * conditions:
 * <ul>
 * <li>memory instantiation without assigned storage space;</li>
 * <li>write operation on a full memory;</li>
 * <li>read operation on an empty memory.</li>
 * </ul>
 */
public class MemException extends Exception {
  /**
   * Version Id for serialization.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Conventional exception instantiation.
   *
   * @param errorMessage pertaining error message
   */
  public MemException(String errorMessage) {
    super(errorMessage);
  }

  /**
   * Exception instantiation with associated raising cause.
   *
   * @param errorMessage pertaining error message
   * @param cause        underlying exception that generated it
   */
  public MemException(String errorMessage, Throwable cause) {
    super(errorMessage, cause);
  }
}
