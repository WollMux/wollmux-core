/*
 * Dateiname: DJDatasetBase.java
 * Projekt  : WollMux
 * Funktion : Basisklasse für DJDataset-Implementierungen
 * 
 * Copyright (c) 2008-2015 Landeshauptstadt München
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
 * 28.10.2005 | BNK | Erstellung
 * 03.11.2005 | BNK | besser kommentiert.
 * -------------------------------------------------------------------
 *
 * @author Matthias Benkmann (D-III-ITD 5.1)
 * @version 1.0
 * 
 */
package de.muenchen.allg.itd51.wollmux.core.db;

import java.util.Map;
import java.util.Set;

import de.muenchen.allg.itd51.wollmux.core.db.ColumnNotFoundException;
import de.muenchen.allg.itd51.wollmux.core.db.NoBackingStoreException;
import de.muenchen.allg.itd51.wollmux.core.util.L;

/**
 * Basisklasse für DJDataset-Implementierungen.
 * 
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public abstract class DJDatasetBase implements DJDataset
{
  /**
   * Bildet Spaltennamen auf (String-)Werte ab. Die Daten in myLOS repräsentieren den
   * lokalen Override, die in myBS die (gecachten) Daten aus der
   * Hintergrunddatenbank. myLOS kann null sein, dann wird der Datensatz als nicht
   * aus dem LOS kommend betrachtet.
   */
  protected Map<String, String> myLOS;

  /**
   * Bildet Spaltennamen auf (String-)Werte ab. Die Daten in myLOS repräsentieren den
   * lokalen Override, die in myBS die (gecachten) Daten aus der
   * Hintergrunddatenbank. myBS kann null sein, dann wird der Datensatz als nur aus
   * dem LOS kommend und nicht mit einer Hintergrunddatenbank verknüpft betrachtet.
   */
  protected Map<String, String> myBS;

  /**
   * Die Menge aller Spaltennamen, die dieser Datensatz kennt. Falls dieses Set nicht
   * null ist, werfen Versuche, auf unbekannte Spalten zuzugreifen eine Exception.
   */
  protected Set<String> schema;

  /**
   * Erzeugt einen neuen Datensatz.
   * 
   * @param backingStore
   *          mappt Spaltennamen auf den Spaltenwert des Datensatzes in der
   *          Hintergrunddatenbank. Spalten, die nicht enthalten sind werden als im
   *          Datensatz unbelegt betrachtet. Als backingStore kann null übergeben
   *          werden (für einen Datensatz, der nur aus dem LOS kommt ohne
   *          Hintergrundspeicher).
   * @param overrideStore
   *          mappt Spaltenname auf den Spaltenwert im LOS- Ist eine Spalte nicht
   *          vorhanden, so hat sie keinen Override im LOS. Wird für overrideStore
   *          null übergeben, so wird der Datensatz als nicht aus dem LOS kommend
   *          betrachtet.
   * @param schema
   *          falls nicht null übergeben wird, erzeugen Zugriffe auf Spalten mit
   *          Namen, die nicht in schema sind Exceptions.
   */
  public DJDatasetBase(Map<String, String> backingStore,
      Map<String, String> overrideStore, Set<String> schema)
  { // TESTED
    myBS = backingStore;
    myLOS = overrideStore;
    this.schema = schema;
  }

  /**
   * Liefert die Map, die dem Konstruktor als backingStore Argument übergeben wurde.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public Map<String, String> getBS()
  {
    return myBS;
  }

  /**
   * Liefert die Map, die dem Konstruktor als overrideStore Argument übergeben wurde.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public Map<String, String> getLOS()
  { // TESTED
    return myLOS;
  }

  /**
   * Liefert den Wert, den dieser Datensatz in der Spalte spaltenName stehen hat
   * (override hat vorrang vor backing store). Falls schema nicht null ist, wird eine
   * ColumnNotFoundException geworfen, wenn versucht wird auf eine Spalte
   * zuzugreifen, die nicht im schema ist. Falls weder backing store noch override
   * Speicher einen Wert für diese Spalte haben, so wird null geliefert (nicht
   * verwechseln mit Zugriff auf eine nicht existierende Spalte!).
   */
  public String get(String spaltenName) throws ColumnNotFoundException
  {
    if (schema != null && !schema.contains(spaltenName))
      throw new ColumnNotFoundException(L.m("Spalte %1 existiert nicht!",
        spaltenName));
    String res;
    res = myLOS.get(spaltenName);
    if (res != null) return res;
    if (myBS != null)
    {
      res = myBS.get(spaltenName);
      return res;
    }
    return null;
  }

  public boolean hasLocalOverride(String columnName) throws ColumnNotFoundException
  {
    if (hasBackingStore())
      return myLOS.get(columnName) != null;
    else
      return true;
  }

  public void set(String columnName, String newValue)
      throws ColumnNotFoundException, UnsupportedOperationException
  {
    if (!isFromLOS())
      throw new UnsupportedOperationException(
        L.m("Nur Datensätze aus dem LOS können manipuliert werden"));
    if (newValue == null)
      throw new IllegalArgumentException(L.m("Override kann nicht null sein"));
    myLOS.put(columnName, newValue);
  }

  public void discardLocalOverride(String columnName)
      throws ColumnNotFoundException, NoBackingStoreException
  {
    if (!isFromLOS()) return;
    if (!hasBackingStore())
      throw new NoBackingStoreException(
        L.m("Datensatz nicht mit Hintergrundspeicher verknüpft"));
    myLOS.remove(columnName);
  }

  public boolean isFromLOS()
  {
    return myLOS != null;
  }

  public boolean hasBackingStore()
  {
    return myBS != null;
  }

  public abstract DJDataset copy();

  public abstract void remove() throws UnsupportedOperationException;

  public abstract boolean isSelectedDataset();

  public abstract void select() throws UnsupportedOperationException;
}
