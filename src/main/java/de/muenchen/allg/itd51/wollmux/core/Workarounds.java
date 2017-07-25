package de.muenchen.allg.itd51.wollmux.core;

import java.util.regex.Pattern;

import de.muenchen.allg.itd51.wollmux.core.util.L;
import de.muenchen.allg.itd51.wollmux.core.util.Logger;
import de.muenchen.allg.itd51.wollmux.core.util.Utils;

public class Workarounds
{
  /*
   * Das ".*" nach dem \\A dürfte da eigentlich nicht sein, aber wenn es nicht da
   * ist, wird bei der Abtretungserklärung gemeckert wegen des Issues 101249.
   */
  public static final Pattern INSERTFORMVALUE_BOOKMARK_TEXT_THAT_CAN_BE_SAFELY_DELETED_WORKAROUND =
    Pattern.compile("\\A.*[<\\[{].*[\\]>}]\\z");

  private static Boolean workaround100374 = null;

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

  /**
   * Issue #100374 betrifft OOo 3.0.x. Der Workaround kann entfernt werden, wenn
   * voraussichtlich OOo 3.1 flächendeckend eingesetzt wird.
   */
  public static boolean applyWorkaroundForOOoIssue100374()
  {
    if (workaround100374 == null)
    {
      String version = Utils.getOOoVersion();
      // -100374 ist der Marker für unsere selbst gepatchten Versionen ohne den
      // Fehler
      if (version != null && version.startsWith("3.0")
        && !version.contains("-100374"))
      {
        workaround100374 = applyWorkaround("100374");
      }
      else
        workaround100374 = Boolean.FALSE;
    }

    return workaround100374.booleanValue();
  }
}
