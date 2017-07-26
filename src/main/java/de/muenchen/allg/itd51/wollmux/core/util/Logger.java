/*
 * Dateiname: Logger.java
 * Projekt  : WollMux
 * Funktion : Logging-Mechanismus zum Schreiben von Nachrichten auf eine PrintStream.
 *
 * Copyright (c) 2010-2015 Landeshauptstadt München
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the European Union Public Licence (EUPL),
 * version 1.0 (or any later version).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 *
 * You should have received a copy of the European Union Public Licence
 * along with this program. If not, see
 * http://ec.europa.eu/idabc/en/document/7330
 *
 * Änderungshistorie:
 * Datum      | Wer | Änderungsgrund
 * -------------------------------------------------------------------
 * 13.10.2005 | LUT | Erstellung
 * 14.10.2005 | BNK | Kommentar korrigiert: Standard ist LOG nicht NONE
 *                  | System.err als Standardausgabestrom
 * 14.10.2005 | LUT | critical(*) --> error(*)
 *                    + Anzeige des Datums bei allen Meldungen.
 * 27.10.2005 | BNK | Leerzeile nach jeder Logmeldung
 * 31.10.2005 | BNK | +error(msg, e)
 *                  | "critical" -> "error"
 * 02.11.2005 | BNK | LOG aus Default-Modus
 * 24.11.2005 | BNK | In init() das Logfile nicht löschen.
 * 05.12.2005 | BNK | line.separator statt \n
 * 06.12.2005 | BNK | bessere Separatoren, kein Test mehr in init, ob Logfile schreibbar
 * 20.04.2006 | BNK | bessere Datum/Zeitangabe, Angabe des Aufrufers
 * 24.04.2006 | BNK | korrekte Monatsangabe.
 * 15.05.2006 | BNK | Cause ausgeben in printException()
 * 16.05.2006 | BNK | println() und printException() vereinheitlicht
 * 30.05.2006 | BNK | bei init(PrintStream,...) den file zurücksetzen, damit
 *                  | die Zuweisung auch wirksam wird.
 * 18.06.2010 | BED | Sekundenausgabe hinzugefügt
 * -------------------------------------------------------------------
 *
 * @author Christoph Lutz (D-III-ITD 5.1)
 */

package de.muenchen.allg.itd51.wollmux.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.Appender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.WriterAppender;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Der Logger ist ein simpler Logging Mechanismus, der im Programmablauf auftretende
 * Nachrichten verschiedener Prioritäten entgegennimmt und die Nachrichten
 * entsprechend der Einstellung des Logging-Modus auf einem PrintStream ausgibt
 * (Standardeinstellung: System.err). Die Logging-Nachrichten werden über
 * unterschiedliche Methodenaufrufe entsprechend der Logging-Priorität abgesetzt.
 * Folgende Methoden stehen dafür zur Verfügung: error(), log(), debug(), debug2()
 * </p>
 * <p>
 * Der Logging-Modus kann über die init()-Methode initialisiert werden. Er
 * beschreibt, welche Nachrichten aus den Prioritätsstufen angezeigt werden und
 * welche nicht. Jeder Logging Modus zeigt die Nachrichten seiner Priorität und die
 * Nachrichten der höheren Prioritätsstufen an. Standardmässig ist der Modus
 * Logging.LOG voreingestellt.
 * </p>
 */
@Deprecated
public class Logger
{

  /**
   * Im Logging-Modus <code>NONE</code> werden keine Nachrichten ausgegeben.
   */
  public static final int NONE = 0;

  /**
   * Der Logging-Modus <code>ERROR</code> zeigt Nachrichten der höchsten
   * Prioritätsstufe "ERROR" an. ERROR enthält Nachrichten, die den Programmablauf
   * beeinflussen - z.B. Fehlermeldungen und Exceptions.
   */
  public static final int ERROR = 1;

  /**
   * Der Logging-Modus <code>LOG</code> ist der Standard Modus. Er zeigt Nachrichten
   * und wichtige Programminformationen an, die im täglichen Einsatz interessant
   * sind. Dieser Modus ist die Defaulteinstellung.
   */
  public static final int LOG = 3;

  /**
   * Der Logging-Modus <code>DEBUG</code> wird genutzt, um detaillierte Informationen
   * über den Programmablauf auszugeben. Er ist vor allem für DEBUG-Zwecke geeignet.
   */
  public static final int DEBUG = 5;

  /**
   * Der Logging-Modus <code>ALL</code> gibt uneingeschränkt alle Nachrichten aus. Er
   * enthält auch Nachrichten der Priorität debug2, die sehr detaillierte
   * Informationen ausgibt, die selbst für normale DEBUG-Zwecke zu genau sind.
   */
  public static final int ALL = 7;

  /**
   * Das Feld <code>mode</code> enthält den aktuellen Logging-Mode
   */
  private static int mode = LOG;

  /**
   * Wenn ignoreInit==true, wird der nächte init-Aufruf ignoriert.
   */
  private static boolean ignoreInit = false;

  private static final Layout LAYOUT = new EnhancedPatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c:%L - %m%n");

  private static final org.apache.log4j.Logger ROOT_LOGGER = LogManager.getRootLogger();

  private Logger()
  {}

  /**
   * Über die Methode init wird der Logger mit einem PrintStream und einem
   * Logging-Modus initialisiert. Ohne diese Methode schreibt der Logger auf
   * System.err im Modus LOG.
   *
   * @param loggingMode
   *          Der neue Logging-Modus kann über die statischen Felder Logger.MODUS (z.
   *          B. Logger.DEBUG) angegeben werden.
   */
  public static void init(PrintStream outputPrintStream, int loggingMode)
  {
    if (ignoreInit) {
      return;
    }
    init(loggingMode);
    Appender appender = new WriterAppender(LAYOUT, outputPrintStream);
    ROOT_LOGGER.removeAllAppenders();
    ROOT_LOGGER.addAppender(appender);
  }

  /**
   * Über die Methode init wird der Logger mit einer Ausgabedatei und einem
   * Logging-Modus initialisiert. Ohne diese Methode schreibt der Logger auf
   * System.err im Modus LOG.
   *
   * @param outputFile
   *          Datei, in die die Ausgaben geschrieben werden.
   * @param loggingMode
   *          Der neue Logging-Modus kann über die statischen Felder Logger.MODUS (z.
   *          B. Logger.DEBUG) angegeben werden.
   * @throws FileNotFoundException
   */
  public static void init(File outputFile, int loggingMode)
  {
    if (ignoreInit) {
      return;
    }
    init(loggingMode);
    try {
      Appender appender = new FileAppender(LAYOUT, outputFile.getAbsolutePath(), true);
      ROOT_LOGGER.removeAllAppenders();
      ROOT_LOGGER.addAppender(appender);
    } catch (IOException e) {
      Logger.error(e);
    }
  }

  /**
   * Über die Methode init wird der Logger in dem Logging-Modus loggingMode
   * initialisiert. Ohne diese Methode schreibt der Logger auf System.err im Modus
   * LOG.
   *
   * @param loggingMode
   *          Der neue Logging-Modus kann über die statischen Felder Logger.MODUS (z.
   *          B. Logger.DEBUG) angegeben werden.
   */
  public static void init(int loggingMode)
  {
    if (ignoreInit) {
      return;
    }

    mode = loggingMode;
    switch (mode)
    {
    case NONE:
      ROOT_LOGGER.setLevel(Level.OFF);
      break;
    case ERROR:
      ROOT_LOGGER.setLevel(Level.ERROR);
      break;
    case DEBUG:
      ROOT_LOGGER.setLevel(Level.DEBUG);
      break;
    case ALL:
      ROOT_LOGGER.setLevel(Level.TRACE);
      break;
    case LOG:
    default:
      ROOT_LOGGER.setLevel(Level.INFO);
      break;
    }
    Logger.debug2("========================== Logger::init(): LoggingMode = " + mode
        + " ========================");
  }

  /**
   * Über die Methode init wird der Logger in dem Logging-Modus loggingMode
   * initialisiert, der in Form eines den obigen Konstanten-Namen übereinstimmenden
   * Strings vorliegt. Ohne diese Methode schreibt der Logger auf System.err im Modus
   * LOG.
   *
   * @param loggingMode
   *          Der neue Logging-Modus kann über die statischen Felder Logger.MODUS (z.
   *          B. Logger.DEBUG) angegeben werden.
   */
  public static void init(String loggingMode)
  {
    if (ignoreInit) {
      return;
    }

    if (loggingMode.compareToIgnoreCase("NONE") == 0) {
      init(NONE);
    }
    if (loggingMode.compareToIgnoreCase("ERROR") == 0) {
      init(ERROR);
    }
    if (loggingMode.compareToIgnoreCase("LOG") == 0) {
      init(LOG);
    }
    if (loggingMode.compareToIgnoreCase("DEBUG") == 0) {
      init(DEBUG);
    }
    if (loggingMode.compareToIgnoreCase("ALL") == 0) {
      init(ALL);
    }
  }

  /**
   * Nach einem Aufruf dieser Methode mit ignoreInit==true werden alle folgenden
   * init-Aufrufe ignoriert.
   */
  public static void setIgnoreInit(boolean ignoreInit)
  {
    Logger.ignoreInit = ignoreInit;
  }

  /**
   * Nachricht der höchsten Priorität "error" absetzen. Als "error" sind nur
   * Ereignisse einzustufen, die den Programmablauf unvorhergesehen verändern oder
   * die weitere Ausführung unmöglich machen.
   *
   * @param msg
   *          Die Logging-Nachricht
   */
  public static void error(String msg)
  {
    if (mode >= ERROR) {
      getLogger(2).error(msg);
    }
  }

  /**
   * Wie {@link #error(String)}, nur dass statt dem String eine Exception ausgegeben
   * wird.
   *
   * @param e
   */
  public static void error(Throwable e)
  {
    if (mode >= ERROR) {
      getLogger(2).error("", e);
    }
  }

  /**
   * Wie {@link #error(String)}, nur dass statt dem String eine Exception ausgegeben
   * wird.
   *
   * @param e
   */
  public static void error(String msg, Exception e)
  {
    if (mode >= ERROR) {
      getLogger(2).error(msg, e);
    }
  }

  /**
   * Nachricht der Priorität "log" absetzen. "log" enthält alle Nachrichten, die für
   * den täglichen Programmablauf beim Endanwender oder zur Auffindung der gängigsten
   * Bedienfehler interessant sind.
   *
   * @param msg
   *          Die Logging-Nachricht
   */
  public static void log(String msg)
  {
    if (mode >= LOG) {
      getLogger(2).info(msg);
    }
  }

  /**
   * Wie {@link #log(String)}, nur dass statt dem String eine Exception ausgegeben
   * wird.
   *
   * @param e
   */
  public static void log(Throwable e)
  {
    if (mode >= LOG) {
      getLogger(2).info("", e);
    }
  }

  /**
   * Nachricht der Priorität "debug" absetzen. Die debug-Priorität dient zu debugging
   * Zwecken. Sie enthält Informationen, die für Programmentwickler interessant sind.
   *
   * @param msg
   *          Die Logging-Nachricht
   */
  public static void debug(String msg)
  {
    if (mode >= DEBUG) {
      getLogger(2).debug(msg);
    }
  }

  /**
   * Wie {@link #debug(String)}, nur dass statt dem String eine Exception ausgegeben
   * wird.
   *
   * @param e
   */
  public static void debug(Throwable e)
  {
    if (mode >= DEBUG) {
      getLogger(2).debug("", e);
    }
  }

  /**
   * Nachricht der geringsten Priorität "debug2" absetzen. Das sind Meldungen, die im
   * Normalfall selbst für debugging-Zwecke zu detailliert sind. Beispielsweise
   * Logging-Meldungen von privaten Unterfunktionen, die die Ausgabe nur unnötig
   * unübersichtlich machen, aber nicht zum schnellen Auffinden von Standard-Fehlern
   * geeignet sind. "debug2" ist geeignet, um ganz spezielle Fehler ausfindig zu
   * machen.
   *
   * @param msg
   *          Die Logging-Nachricht.
   */
  public static void debug2(String msg)
  {
    if (mode >= ALL) {
      getLogger(2).trace(msg);
    }
  }

  /**
   * Wie {@link #debug2(String)}, nur dass statt dem String eine Exception ausgegeben
   * wird.
   *
   * @param e
   */
  public static void debug2(Throwable e)
  {
    if (mode >= ALL) {
      getLogger(2).trace("", e);
    }
  }

  /**
   * Liefert Datei (ohne java Extension) und Zeilennummer des Elements level des
   * Stacks. Level 1 ist dabei die Funktion, die getCaller() aufruft.
   *
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  private static org.slf4j.Logger getLogger(int level)
  {
    try
    {
      Throwable grosserWurf = new Throwable();
      grosserWurf.fillInStackTrace();
      StackTraceElement[] dickTracy = grosserWurf.getStackTrace();
      return LoggerFactory.getLogger(dickTracy[level].getClassName());
    }
    catch (Exception x)
    {
      Logger.debug2(x);
      return LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }
  }
}
