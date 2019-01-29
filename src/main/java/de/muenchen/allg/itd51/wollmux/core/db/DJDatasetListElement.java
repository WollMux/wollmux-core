/*
 * Dateiname: DJDatasetListElement.java
 * Projekt  : WollMux
 * Funktion : Wrapper um ein vom DJ gelieferten Datensatz für die Darstellung 
 *            in einer Liste.
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
 * 03.11.2005 | LUT | Erstellung
 * 06.04.2010 | BED | +Icon
 * 07.04.2010 | BED | displayTemplate als Instanzvariable
 * -------------------------------------------------------------------
 *
 * @author Christoph Lutz (D-III-ITD 5.1)
 * 
 */
package de.muenchen.allg.itd51.wollmux.core.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DJDatasetListElement extends DatasetListElement
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DJDatasetListElement.class);
  
  /**
   * Enthält das DJDataset-Element.
   */
  private DJDataset ds;

  /**
   * Erzeugt ein neues DJDatasetListElement für die Darstellung in einer Liste.
   * 
   * @param ds
   *          das DJDatasetElement das über das DJDatasetListElement dargestellt
   *          werden soll.
   * @param displayTemplate
   *          gibt an, wie die Personen in den Listen angezeigt werden sollen.
   *          %{Spalte}-Syntax um entsprechenden Wert des Datensatzes einzufügen,
   *          z.B. "%{Nachname}, %{Vorname}" für die Anzeige "Meier, Hans" etc.
   * @param icon
   *          das Icon, das in der Liste für das Element verwendet werden soll. Falls
   *          kein Icon vorhanden ist, kann <code>null</code> übergeben werden.
   */
  public DJDatasetListElement(DJDataset ds)
  {
    super(ds);
    this.ds = ds;
  }

  /**
   * Liefert den DJDataset dieses DJDatasetListElements.
   * 
   * @return den DJDataset dieses DJDatasetListElements.
   */
  @Override
  public DJDataset getDataset()
  {
    return ds;
  }
  
  /**
   * Liefert den in der Listbox anzuzeigenden String.
   */
  @Override
  public String toString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    
    try
    {
      String rolle = ds.get("Rolle");
      String nachname = ds.get("Nachname");
      String vorname = ds.get("Vorname");
      String orgaKurz = ds.get("OrgaKurz");
      
      stringBuilder.append(rolle == null || rolle.isEmpty() ? "" : " (" + rolle + ")");
      stringBuilder.append(nachname == null || nachname.isEmpty() ? "" : nachname);
      stringBuilder.append(", ");
      stringBuilder.append(vorname == null || vorname.isEmpty() ? "" : vorname);
      stringBuilder.append(" ");
      stringBuilder.append(orgaKurz == null || orgaKurz.isEmpty() ? "" : orgaKurz);
    } catch (ColumnNotFoundException e)
    {
      LOGGER.error("", e);
    }
    
    return stringBuilder.toString();
  }
  
}
