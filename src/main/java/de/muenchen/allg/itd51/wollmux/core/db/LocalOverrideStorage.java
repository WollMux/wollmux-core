package de.muenchen.allg.itd51.wollmux.core.db;

import java.util.List;
import java.util.Set;

import de.muenchen.allg.itd51.wollmux.core.db.DatasourceJoiner.Status;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigThingy;

public interface LocalOverrideStorage extends Iterable<Dataset>
{

  /**
   * Falls es im LOS momentan mindestens einen Datensatz mit Schlüssel selectKey
   * gibt, so wird der durch sameKeyIndex bezeichnete zum ausgewählten Datensatz,
   * ansonsten wird, falls der LOS mindestens einen Datensatz enthält, ein
   * beliebiger Datensatz ausgewählt.
   * 
   * @param sameKeyIndex
   *          zählt ab 0 und gibt an, der wievielte Datensatz gewählt werden soll,
   *          wenn mehrere mit gleichem Schlüssel vorhanden sind. Sollte
   *          sameKeyIndex zu hoch sein, wird der letzte Datensatz mit dem
   *          entsprechenden Schlüssel ausgewählt.
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public void selectDataset(String selectKey, int sameKeyIndex);

  /**
   * Erzeugt einen neuen Datensatz, der nicht mit Hintergrundspeicher verknüpft
   * ist.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public DJDataset newDataset();

  /**
   * Erzeugt eine Kopie im LOS vom Datensatz ds, der nicht aus dem LOS kommen darf.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public DJDataset copyNonLOSDataset(Dataset ds);

  /**
   * Liefert den momentan im LOS selektierten Datensatz zurück.
   * 
   * @throws DatasetNotFoundException
   *           falls der LOS leer ist (sonst ist immer ein Datensatz selektiert).
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public DJDataset getSelectedDataset() throws DatasetNotFoundException;

  /**
   * Liefert die Anzahl der Datensätze im LOS, die den selben Schlüssel haben wie
   * der ausgewählte, und die vor diesem in der LOS-Liste gespeichert sind.
   * 
   * @throws DatasetNotFoundException
   *           falls der LOS leer ist (ansonsten ist immer ein Datensatz
   *           selektiert).
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public int getSelectedDatasetSameKeyIndex() throws DatasetNotFoundException;

  /**
   * Läd für die Datensätze des LOS aktuelle Daten aus der Datenbank database.
   * 
   * @param timeout
   *          die maximale Zeit, die database Zeit hat, anfragen zu beantworten.
   * @param status
   *          hiervon wird das Feld lostDatasets geupdatet.
   * @throws TimeoutException
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public List<Dataset> refreshFromDatabase(Datasource database, long timeout, Status status) throws TimeoutException;

  /**
   * Liefert null, falls bislang kein Schema vorhanden (weil das Laden der
   * Cache-Datei im Konstruktur fehlgeschlagen ist).
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public Set<String> getSchema(); // TESTED

  /**
   * Fügt conf die Beschreibung der Datensätze im LOS als Kinder hinzu.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public void dumpData(ConfigThingy conf);

  /**
   * Ändert das Datenbankschema. Spalten des alten Schemas, die im neuen nicht mehr
   * vorhanden sind werden aus den Datensätzen gelöscht. Im neuen Schema
   * hinzugekommene Spalten werden in den Datensätzen als unbelegt betrachtet.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public void setSchema(Set<String> schema);

  /**
   * Liefert die Anzahl der Datensätze im LOS.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public int size();

  /**
   * true, falls der LOS leer ist.
   * 
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public boolean isEmpty();

}
