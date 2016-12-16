package de.muenchen.allg.itd51.wollmux.core.dialog.controls;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Textarea extends UIElementBase
{
  private JTextArea textarea;

  private Component textAreaComponent;

  public Textarea(String id, JTextArea textarea, Component textAreaComponent,
      Object layoutConstraints, Integer labelType, String label,
      Object labelLayoutConstraints)
  {
    this.textarea = textarea;
    this.textAreaComponent = textAreaComponent;
    this.layoutConstraints = layoutConstraints;
    this.labelLayoutConstraints = labelLayoutConstraints;
    this.label = new JLabel(label);
    this.labelType = labelType;
    this.id = id;
  }

  @Override
  public Component getComponent()
  {
    return textAreaComponent;
  }

  /**
   * Da getComponent das Panel zurückliefert, in dem sich die Textarea befindet,
   * gibt diese Funktion das eigentliche JTextArea-Objekt zurück.
   * 
   * @return
   * @author Andor Ertsey (D-III-ITD-D101)
   */
  public JTextArea getTextArea()
  {
    return textarea;
  }

  @Override
  public String getString()
  {
    return textarea.getText();
  }

  @Override
  public boolean getBoolean()
  {
    return !getString().isEmpty();
  }

  @Override
  public void setString(String str)
  {
    textarea.setText(str);
  }

  @Override
  public void setBackground(Color bg)
  {
    textarea.setBackground(bg);
  }

  @Override
  public boolean isStatic()
  {
    return false;
  }
}