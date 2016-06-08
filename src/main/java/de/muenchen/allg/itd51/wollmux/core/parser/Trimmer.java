package de.muenchen.allg.itd51.wollmux.core.parser;

/**
 * A Utility class for Strings.
 *
 * @author Daniel Sikeler
 */
public final class Trimmer
{

  private Trimmer()
  {
  }

  /**
   * Remove the first and last quote of the string.
   *
   * @param value
   *          The string to change.
   * @return The input without quotes.
   */
  public static String trimQuotes(final String value)
  {
    int start = 0;
    int end = value.length();
    if ((value.startsWith("\"") && value.endsWith("\""))
        || (value.startsWith("'") && value.endsWith("'")))
    {
      start = 1;
      end--;
    }
    return value.substring(start, end);
  }

  /**
   * Add quotes around the string.
   *
   * @param value
   *          The string.
   * @return The string with quotes.
   */
  public static String addQuoates(final String value)
  {
    if (!value.contains("\""))
    {
      return "\"" + value + "\"";
    } else if (!value.contains("'"))
    {
      return "'" + value + "'";
    } else
    {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
  }

  /**
   * Remove the last slash of the string.
   *
   * @param value
   *          The string to change.
   * @return The input without the last slash.
   */
  public static String trimSlash(final String value)
  {
    int end = value.length();
    if (value.endsWith("/"))
    {
      end--;
    }
    return value.substring(0, end);
  }
}
