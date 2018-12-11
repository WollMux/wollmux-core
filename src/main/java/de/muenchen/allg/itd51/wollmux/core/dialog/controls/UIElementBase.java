package de.muenchen.allg.itd51.wollmux.core.dialog.controls;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;

import de.muenchen.allg.itd51.wollmux.core.form.model.Control;
import de.muenchen.allg.itd51.wollmux.core.functions.Function;

/**
 * Abstrakte Basis-Klasse für UIElemente.
 * 
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public abstract class UIElementBase implements UIElement
{

  protected UIElement.LabelPosition labelType = UIElement.LabelPosition.NONE;

  protected JLabel label = null;

  protected Object layoutConstraints = null;

  protected Object labelLayoutConstraints = null;

  protected Function constraints = null;

  protected String id = "";

  @Deprecated
  protected Object addData = null;

  protected Control field = null;

  @Override
  public void setBackground(Color bg)
  {
    this.getComponent().setBackground(bg);
  }
  
  @Override
  public void setEnabled(boolean enabled)
  {
    this.getComponent().setEnabled(enabled);
  }

  @Override
  public UIElement.LabelPosition getLabelType()
  {
    return labelType;
  }

  @Override
  public Component getLabel()
  {
    return label;
  }

  @Override
  public Object getLayoutConstraints()
  {
    return layoutConstraints;
  }

  @Override
  public Object getLabelLayoutConstraints()
  {
    return labelLayoutConstraints;
  }

  @Override
  public Object getAdditionalData()
  {
    return addData;
  }

  @Override
  public void setAdditionalData(Object o)
  {
    addData = o;
  }

  @Override
  public Control getFormField()
  {
    return field;
  }

  @Override
  public void setFormField(Control field)
  {
    this.field = field;
  }

  @Override
  public void setVisible(boolean vis)
  {
    if (getLabel() != null)
    {
      getLabel().setVisible(vis);
    }
    getComponent().setVisible(vis);
    /*
     * einige Komponenten (z.B. JTextField) tun dies nicht richtig siehe
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4403550
     */
    ((JComponent) getComponent().getParent()).revalidate();
  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setString(String str)
  {
  }

  @Override
  public boolean hasFocus()
  {
    return getComponent().isFocusOwner();
  }

  @Override
  public void takeFocus()
  {
    getComponent().requestFocusInWindow();
  }
}