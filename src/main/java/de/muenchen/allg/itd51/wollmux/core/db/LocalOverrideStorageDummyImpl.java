package de.muenchen.allg.itd51.wollmux.core.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.muenchen.allg.itd51.wollmux.core.db.ColumnNotFoundException;
import de.muenchen.allg.itd51.wollmux.core.db.DJDataset;
import de.muenchen.allg.itd51.wollmux.core.db.Dataset;
import de.muenchen.allg.itd51.wollmux.core.db.DatasetNotFoundException;
import de.muenchen.allg.itd51.wollmux.core.db.Datasource;
import de.muenchen.allg.itd51.wollmux.core.db.DatasourceJoiner;
import de.muenchen.allg.itd51.wollmux.core.db.LocalOverrideStorage;
import de.muenchen.allg.itd51.wollmux.core.db.NoBackingStoreException;
import de.muenchen.allg.itd51.wollmux.core.db.TimeoutException;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigThingy;

public class LocalOverrideStorageDummyImpl implements LocalOverrideStorage
{
  DJDataset dummyDataset;
  Set<String> schema = new HashSet<String>();

  public LocalOverrideStorageDummyImpl()
  {
    dummyDataset = new DJDataset()
    {

      @Override
      public String getKey()
      {
        return "<key>";
      }

      @Override
      public String get(String columnName) throws ColumnNotFoundException
      {
        return columnName;
      }

      @Override
      public void set(String columnName, String newValue)
          throws ColumnNotFoundException
      {
        throw new UnsupportedOperationException();
      }

      @Override
      public void select()
      {
        throw new UnsupportedOperationException();
      }

      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean isSelectedDataset()
      {
        return true;
      }

      @Override
      public boolean isFromLOS()
      {
        return true;
      }

      @Override
      public boolean hasLocalOverride(String columnName)
          throws ColumnNotFoundException
      {
        return false;
      }

      @Override
      public boolean hasBackingStore()
      {
        return false;
      }

      @Override
      public void discardLocalOverride(String columnName)
          throws ColumnNotFoundException, NoBackingStoreException
      {}

      @Override
      public DJDataset copy()
      {
        return dummyDataset;
      }
    };
  }

  @Override
  public void selectDataset(String selectKey, int sameKeyIndex)
  {}

  @Override
  public DJDataset newDataset()
  {
    return dummyDataset;
  }

  @Override
  public DJDataset copyNonLOSDataset(Dataset ds)
  {
    return dummyDataset;
  }

  @Override
  public DJDataset getSelectedDataset() throws DatasetNotFoundException
  {
    return dummyDataset;
  }

  @Override
  public int getSelectedDatasetSameKeyIndex() throws DatasetNotFoundException
  {
    return 0;
  }

  @Override
  public List<Dataset> refreshFromDatabase(Datasource database, long timeout)
      throws TimeoutException
  {
    return new ArrayList<Dataset>();
  }

  @Override
  public Set<String> getSchema()
  {
    if ( schema.isEmpty()){
      schema.add(DatasourceJoiner.NOCONFIG);
    }
    return schema;
  }

  @Override
  public void dumpData(ConfigThingy conf)
  {}

  @Override
  public void setSchema(Set<String> schema)
  {
    this.schema = schema;
  }

  @Override
  public int size()
  {
    return 1;
  }

  @Override
  public Iterator<? extends Dataset> iterator()
  {
    List<Dataset> list = new ArrayList<Dataset>();
    list.add(dummyDataset);
    return list.iterator();
  }

  @Override
  public boolean isEmpty()
  {
    return false;
  }
}
