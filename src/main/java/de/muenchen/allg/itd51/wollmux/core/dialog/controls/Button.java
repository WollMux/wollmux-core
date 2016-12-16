package de.muenchen.allg.itd51.wollmux.core.dialog.controls;

import java.awt.Component;

import javax.swing.AbstractButton;

public class Button extends UIElementBase
{
  private AbstractButton button;

  public Button(String id, AbstractButton button, Object layoutConstraints)
  {
    this.button = button;
    this.layoutConstraints = layoutConstraints;
    this.id = id;
  }

  @Override
  public Component getComponent()
  {
    return button;
  }

  @Override
  public String getString()
  {
    return "false";
  }

  @Override
  public boolean getBoolean()
  {
    return false;
  }

  @Override
  public boolean isStatic()
  {
    return true;
  }
}