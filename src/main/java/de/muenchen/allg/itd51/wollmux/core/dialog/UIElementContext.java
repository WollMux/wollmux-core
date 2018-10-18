package de.muenchen.allg.itd51.wollmux.core.dialog;

import java.awt.GridBagConstraints;
import java.util.Map;
import java.util.Set;

import de.muenchen.allg.itd51.wollmux.core.dialog.controls.UIElement;

public class UIElementContext
{
  public static final String DEFAULT = "default";
  
  /**
   * Bildet einen TYPE auf die dazugehörigen layout constraints (d,i, der optionale
   * zweite Parameter von
   * {@link java.awt.Container#add(java.awt.Component, java.lang.Object)
   * java.awt.Container.add()}) ab. Darf null-Werte enthalten. Ist für einen TYPE
   * kein Mapping angegeben (auch kein null-Wert), so wird erst geschaut, ob ein
   * Mapping für "default" vorhanden ist. Falls ja, so wird dieses der
   * entsprechenden Eigenschaft des erzeugten UIElements zugewiesen, ansonsten
   * null.
   */
  private Map<String, ?> mapTypeToLayoutConstraints;

  /**
   * Bildet einen TYPE auf einen Integer ab, der angibt, ob das UI Element ein
   * zusätzliches Label links oder rechts bekommen soll. Mögliche Werte sind
   * {@link UIElement#LabelPosition#LEFT}, {@link UIElement#LabelPosition#RIGHT}
   * und {@link UIElement#LabelPosition#NONE}. Darf null-Werte enthalten. Ist
   * für einen TYPE kein Mapping angegeben (auch kein null-Wert), so wird erst
   * geschaut, ob ein Mapping für "default" vorhanden ist. Falls ja, so wird
   * dieses der entsprechenden Eigenschaft des erzeugten UIElements zugewiesen,
   * ansonsten null.
   */
  private Map<String, UIElement.LabelPosition> mapTypeToLabelType;

  /**
   * Für UI Elemente, die ein zusätzliches Label links oder rechts bekommen sollen
   * (siehe {@link #mapTypeToLabelType}) liefert diese Map die layout constraints
   * für das Label. Achtung! UI Elemente mit TYPE "label" beziehen ihre layout
   * constraints nicht aus dieser Map, sondern wie alle anderen UI Elemente auch
   * aus {@link #mapTypeToLayoutConstraints}. Darf null-Werte enthalten. Ist für
   * einen TYPE kein Mapping angegeben (auch kein null-Wert), so wird erst
   * geschaut, ob ein Mapping für "default" vorhanden ist. Falls ja, so wird dieses
   * der entsprechenden Eigenschaft des erzeugten UIElements zugewiesen, ansonsten
   * null.
   */
  private Map<String, ?> mapTypeToLabelLayoutConstraints;

  /**
   * Die Menge (von Strings) der ACTIONs, die akzeptiert werden sollen. Alle
   * anderen produzieren eine Fehlermeldung.
   */
  private Set<String> supportedActions;

  /**
   * Der {@link UIElementEventHandler}, an den die erzeugten UI Elemente ihre
   * Ereignisse melden.
   */
  private UIElementEventHandler uiElementEventHandler;

  /**
   * Enthält diese Map für einen TYPE ein Mapping auf einen anderen TYPE, so wird
   * der andere TYPE verwendet. Dies ist nützlich, um abhängig vom Kontext den TYPE
   * "separator" entweder auf "h-separator" oder "v-separator" abzubilden.
   */
  private Map<String, String> mapTypeToType;

  public Object getLayoutConstraints(String type)
  {
    Object layoutConstraints;
    
    if (mapTypeToLayoutConstraints.containsKey(type))
      layoutConstraints = mapTypeToLayoutConstraints.get(type);
    else
      layoutConstraints = mapTypeToLayoutConstraints.get(DEFAULT);
    
    if (layoutConstraints instanceof GridBagConstraints)
      layoutConstraints = ((GridBagConstraints) layoutConstraints).clone();
    
    return layoutConstraints;
  }

  public void setMapTypeToLayoutConstraints(Map<String, ?> mapTypeToLayoutConstraints)
  {
    this.mapTypeToLayoutConstraints = mapTypeToLayoutConstraints;
  }

  public UIElement.LabelPosition getLabelType(String type)
  {
    if (mapTypeToLabelType.containsKey(type))
      return mapTypeToLabelType.get(type);
    else
      return mapTypeToLabelType.get(DEFAULT);
  }

  public void setMapTypeToLabelType(Map<String, UIElement.LabelPosition> mapTypeToLabelType)
  {
    this.mapTypeToLabelType = mapTypeToLabelType;
  }

  public Object getLabelLayoutConstraints(String type)
  {
    Object labelLayoutConstraints;
    if (mapTypeToLabelLayoutConstraints.containsKey(type))
      labelLayoutConstraints = mapTypeToLabelLayoutConstraints.get(type);
    else
      labelLayoutConstraints = mapTypeToLabelLayoutConstraints.get(DEFAULT);

    if (labelLayoutConstraints instanceof GridBagConstraints)
      labelLayoutConstraints = ((GridBagConstraints) labelLayoutConstraints).clone();
    
    return labelLayoutConstraints;
  }

  public void setMapTypeToLabelLayoutConstraints(Map<String, ?> mapTypeToLabelLayoutConstraints)
  {
    this.mapTypeToLabelLayoutConstraints = mapTypeToLabelLayoutConstraints;
  }

  public Set<String> getSupportedActions()
  {
    return supportedActions;
  }

  public void setSupportedActions(Set<String> supportedActions)
  {
    this.supportedActions = supportedActions;
  }

  public UIElementEventHandler getUiElementEventHandler()
  {
    return uiElementEventHandler;
  }

  public void setUiElementEventHandler(UIElementEventHandler uiElementEventHandler)
  {
    this.uiElementEventHandler = uiElementEventHandler;
  }

  public String getMappedType(String type)
  {
    if (mapTypeToType != null && mapTypeToType.containsKey(type))
      return mapTypeToType.get(type);
    return type;
  }

  public void setMapTypeToType(Map<String, String> mapTypeToType)
  {
    this.mapTypeToType = mapTypeToType;
  }
}