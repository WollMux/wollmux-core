package de.muenchen.allg.itd51.wollmux.core.document.nodes;

import de.muenchen.allg.itd51.wollmux.core.document.DocumentTree;

/**
 * Implementiert von Knoten, die Nachfahren haben können, z,B, Absätzen.
 *
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public interface Container
{
  /**
   * Rückgabewert für {@link #getType()} falls die Art des Containers nicht
   * näher bestimmt ist.
   */
  public static final int CONTAINER_TYPE = 0;

  /**
   * Rückgabewert für {@link #getType()} falls der Container ein Absatz ist.
   */
  public static final int PARAGRAPH_TYPE = 1;

  /**
   * Liefert die Art des Knotens, z,B, {@link #PARAGRAPH_TYPE}.
   */
  public int getType();
}