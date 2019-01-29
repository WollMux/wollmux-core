/*
 * Dateiname: DatasetListElement.java
 * Projekt  : WollMux
 * Funktion : Wrapper um einen Datansatz des DJ für die Darstellung 
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
 * 03.11.2005 | LUT | Erstellung als DJDatasetListElement
 * 06.04.2010 | BED | +Icon
 * 07.04.2010 | BED | displayTemplate als Instanzvariable
 * 11.01.2011 | LUT | Verallgemeinert zu DatasetListElement
 * -------------------------------------------------------------------
 *
 * @author Christoph Lutz (D-III-ITD 5.1)
 * 
 */
package de.muenchen.allg.itd51.wollmux.core.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetListElement implements Comparable<DatasetListElement>
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(DatasetListElement.class);
  
  /**
   * Enthält das Dataset-Element.
   */
  private Dataset ds;

  /**
   * Erzeugt ein neues DatasetListElement für die Darstellung in einer Liste.
   * 
   * @param ds
   *          das DatasetElement das über das DatasetListElement dargestellt werden
   *          soll.
   */
  public DatasetListElement(Dataset ds)
  {
    this.ds = ds;
  }

  /**
   * Liefert den Dataset dieses DatasetListElements.
   * 
   * @return den Dataset dieses DatasetListElements.
   */
  public Dataset getDataset()
  {
    return ds;
  }

  /**
   * Vergleicht die String-Repräsentation ({@link #toString()}) zweier Listenelemente
   * über die compareTo()-Methode der Klasse String.
   * 
   * @param o
   *          das DatasetListElement mit dem verglichen werden soll
   * @return Rückgabewert von this.toString().compareTo(o.toString())
   */
  @Override
  public int compareTo(DatasetListElement o)
  {
    try
    {
      String dbRolle = this.getDataset().get("Rolle") == null || this.getDataset().get("Rolle").isEmpty() ? "" : this.getDataset().get("Rolle");
      String dbNachname = this.getDataset().get("Nachname") == null ? "" : this.getDataset().get("Nachname");
      String dbVorname = this.getDataset().get("Vorname") == null ? "" : this.getDataset().get("Vorname");
      String dbOrgaKurz = this.getDataset().get("OrgaKurz") == null ? "" : this.getDataset().get("OrgaKurz");
      
      String dbRolleO = o.getDataset().get("Rolle") == null || o.getDataset().get("Rolle").isEmpty() ? "" : o.getDataset().get("Rolle");
      String dbNachnameO = o.getDataset().get("Nachname") == null ? "" : o.getDataset().get("Nachname");
      String dbVornameO = o.getDataset().get("Vorname") == null ? "" : o.getDataset().get("Vorname");
      String dbOrgaKurzO = o.getDataset().get("OrgaKurz") == null ? "" : o.getDataset().get("OrgaKurz");
      
      return dbRolle.compareTo(dbRolleO)
	  + dbNachname.compareTo(dbNachnameO)
	  + dbVorname.compareTo(dbVornameO)
	  + dbOrgaKurz.compareTo(dbOrgaKurzO);
    } catch (ColumnNotFoundException e)
    {
      LOGGER.error("", e);
    }
    
    return 0;
  }

  /**
   * Liefert <code>true</code> zurück, wenn die String-Repräsentation (
   * {@link #toString()}) des übergebenen DatasetListElements gleich (im Hinblick auf
   * {@link String#equals(Object)} ist zu der String-Repräsentation von this.
   */
  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o instanceof DatasetListElement)
    {
      return this.toString().equals(o.toString());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    try
    {
      String dbRolle = this.getDataset().get("Rolle") == null || this.getDataset().get("Rolle").isEmpty() ? "" : this.getDataset().get("Rolle");
      String dbNachname = this.getDataset().get("Nachname") == null ? "" : this.getDataset().get("Nachname");
      String dbVorname = this.getDataset().get("Vorname") == null ? "" : this.getDataset().get("Vorname");
      String dbOrgaKurz = this.getDataset().get("OrgaKurz") == null ? "" : this.getDataset().get("OrgaKurz");
      
      return dbRolle.hashCode()
	  + dbNachname.hashCode() 
	  + dbVorname.hashCode()
	  + dbOrgaKurz.hashCode();
    } catch (ColumnNotFoundException e)
    {
      LOGGER.error("", e);
    }
    
    return 0;
  }
}
