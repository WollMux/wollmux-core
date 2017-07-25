package de.muenchen.allg.itd51.wollmux.core;

import java.util.regex.Pattern;

import de.muenchen.allg.itd51.wollmux.core.util.L;
import de.muenchen.allg.itd51.wollmux.core.util.Logger;

public class Workarounds
{
  /*
   * Das ".*" nach dem \\A dürfte da eigentlich nicht sein, aber wenn es nicht da
   * ist, wird bei der Abtretungserklärung gemeckert wegen des Issues 101249.
   */
  public static final Pattern INSERTFORMVALUE_BOOKMARK_TEXT_THAT_CAN_BE_SAFELY_DELETED_WORKAROUND =
    Pattern.compile("\\A.*[<\\[{].*[\\]>}]\\z");

  private static Pattern workaround101249 = null;

  private Workarounds()
  {}

  public static Boolean applyWorkaround(String issueNumber)
  {
    Logger.debug("Workaround für Issue "
      + issueNumber
      + " aktiv. Bestimmte Features sind evtl. nicht verfügbar. Die Performance kann ebenfalls leiden.");
    return Boolean.TRUE;
  }

  /**
   * Wegen http://qa.openoffice.org/issues/show_bug.cgi?id=101249 muss ein laxeres
   * Pattern verwendet werden, zum Test, ob ein Text in einem insertFormValue
   * Bookmark problematisch ist.
   *
   * @return das Pattern das zum Testen verwendet werden soll
   */
  public static Pattern workaroundForIssue101249()
  {
    if (workaround101249 == null)
    {
      Logger.debug(L.m("Workaround für Issue 101249 aktiv."));
      workaround101249 =
        INSERTFORMVALUE_BOOKMARK_TEXT_THAT_CAN_BE_SAFELY_DELETED_WORKAROUND;
    }
    return workaround101249;
  }
}
