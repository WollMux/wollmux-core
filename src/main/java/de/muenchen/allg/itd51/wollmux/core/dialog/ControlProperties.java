package de.muenchen.allg.itd51.wollmux.core.dialog;

import java.util.AbstractMap.SimpleEntry;

import de.muenchen.allg.itd51.wollmux.core.dialog.ControlModel.ControlType;

public class ControlProperties
{
  private ControlType controlType;
  private int controlWidth;
  private int controlHeight;
  private int controlPercentWidth;
  private int controlPercentHeight;
  private int marginBetweenControls;
  private int marginLeft;
  private String controlName;
  private SimpleEntry<String[], Object[]> propertySet;

  public ControlProperties(ControlType controlType, String controlName,
      int controlWidth, int controlHeight, int controlPercentWidth,
      int controlPercentHeight, SimpleEntry<String[], Object[]> propertySet)
  {
    this.controlType = controlType;
    this.controlName = controlName;
    this.controlWidth = controlWidth;
    this.controlHeight = controlHeight;
    this.controlPercentWidth = controlPercentWidth;
    this.controlPercentHeight = controlPercentHeight;
    this.propertySet = propertySet;
  }

  public void setMarginLeft(int marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  public int getMarginLeft()
  {
    return this.marginLeft;
  }

  public void setMarginBetweenControls(int margin)
  {
    this.marginBetweenControls = margin;
  }

  public int getMarginBetweenControls()
  {
    return this.marginBetweenControls;
  }

  public String getControlName()
  {
    return this.controlName;
  }

  public ControlType getControlType()
  {
    return this.controlType;
  }

  public int getControlWidth()
  {
    return this.controlWidth;
  }

  public int getControlHeight()
  {
    return this.controlHeight;
  }

  public int getControlPercentWidth()
  {
    return this.controlPercentWidth;
  }

  public int getControlPercentHeight()
  {
    return this.controlPercentHeight;
  }

  public SimpleEntry<String[], Object[]> getPropertySet()
  {
    return this.propertySet;
  }

}
