package de.muenchen.allg.itd51.wollmux.core.dialog;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.ItemEvent;
import com.sun.star.awt.TextEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XItemListener;
import com.sun.star.awt.XProgressBar;
import com.sun.star.awt.XRadioButton;
import com.sun.star.awt.XTextComponent;
import com.sun.star.awt.XTextListener;
import com.sun.star.awt.XWindow;
import com.sun.star.beans.XMultiPropertySet;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import de.muenchen.allg.afid.UNO;

public class ControlModel
    implements XTextListener, XActionListener, XItemListener
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ControlModel.class);
  private Orientation orientation;
  private Align alignment;
  private Optional<Dock> dock;
  private List<XControl> controls = new ArrayList<>();
  private XMultiServiceFactory multiServiceFactory;

  public ControlModel(Orientation horizontal, Align right,
      List<SimpleEntry<ControlType, SimpleEntry<String[], Object[]>>> controls,
      Optional<Dock> dock) throws Exception
  {
    this.multiServiceFactory = UnoRuntime.queryInterface(
        XMultiServiceFactory.class, UNO.defaultContext.getServiceManager());
    this.orientation = horizontal;
    this.alignment = right;
    this.dock = dock;
    this.controls = this.convertToXControl(controls);
  }

  public Orientation getOrientation()
  {
    return this.orientation;
  }

  public Align getAlignment()
  {
    return this.alignment;
  }

  public Optional<Dock> getDock()
  {
    return this.dock;
  }

  public List<XControl> getControls()
  {
    return this.controls;
  }

  public enum Align
  {
    NONE("NONE"),
    RIGHT("RIGHT"),
    LEFT("LEFT");

    private final String alignment;

    Align(final String alignment)
    {
      this.alignment = alignment;
    }

    @Override
    public String toString()
    {
      return this.alignment;
    }
  }

  public enum Dock
  {
    TOP("TOP"),
    BOTTOM("BOTTOM");

    private final String dock;

    Dock(final String dock)
    {
      this.dock = dock;
    }

    @Override
    public String toString()
    {
      return this.dock;
    }
  }

  public enum Orientation
  {
    HORIZONTAL("HORIZONTAL"),
    VERTICAL("VERTICAL");

    private final String orientation;

    Orientation(final String orientation)
    {
      this.orientation = orientation;
    }

    @Override
    public String toString()
    {
      return this.orientation;
    }
  }

  public enum ControlType
  {
    EDIT("com.sun.star.awt.UnoControlEdit"),
    BUTTON("com.sun.star.awt.UnoControlButton"),
    PROGRESSBAR("com.sun.star.awt.UnoControlProgressBar"),
    CHECKBOX("com.sun.star.awt.UnoControlCheckBox"),
    RADIO("com.sun.star.awt.UnoControlRadioButton"),
    DATE("com.sun.star.awt.UnoControlDateField"),
    LINE("com.sun.star.awt.UnoControlFixedLine"),
    LABEL("com.sun.star.awt.UnoControlFixedText"),
    SCROLLBAR("com.sun.star.awt.UnoControlScrollBar"),
    LINEBREAK("com.sun.star.awt.UnoControlButton");

    private String controlType;

    ControlType(String controlType)
    {
      this.controlType = controlType;
    }

    @Override
    public String toString()
    {
      return this.controlType;
    }
  }

  public List<XControl> convertToXControl(
      List<SimpleEntry<ControlType, SimpleEntry<String[], Object[]>>> controlTypes)
      throws Exception
  {
    for (SimpleEntry<ControlType, SimpleEntry<String[], Object[]>> controlType : controlTypes)
    {
      Object control = UNO.createUNOService(controlType.getKey().toString());

      String controlTypeKey = controlType.getKey().toString();
      Object editModel;

      editModel = this.multiServiceFactory
          .createInstance(controlTypeKey + "Model");

      XMultiPropertySet propertySet = UnoRuntime
          .queryInterface(XMultiPropertySet.class, editModel);

      propertySet.setPropertyValues(controlType.getValue().getKey(),
          controlType.getValue().getValue());

      XControlModel modelX = UnoRuntime.queryInterface(XControlModel.class,
          editModel);

      XControl xControl = UnoRuntime.queryInterface(XControl.class, control);

      xControl.setModel(modelX);

      this.addEventListener(controlType.getKey(), xControl);

      this.controls.add(xControl);
    }

    return this.controls;
  }

  private void addEventListener(ControlType controlType, XControl xControl)
  {
    switch (controlType)
    {
    case EDIT:
      XTextComponent textComponent = UnoRuntime
          .queryInterface(XTextComponent.class, xControl);
      textComponent.addTextListener(this);
      break;
    case BUTTON:
      XButton button = UnoRuntime.queryInterface(XButton.class, xControl);
      button.addActionListener(this);
      break;
    case LINEBREAK:
      XWindow linebreakButton = UnoRuntime.queryInterface(XWindow.class,
          xControl);
      linebreakButton.setVisible(false);
      break;
    case PROGRESSBAR:
      XProgressBar progressBar = UnoRuntime.queryInterface(XProgressBar.class,
          xControl);
      progressBar.getValue();
      break;
    case CHECKBOX:
      XCheckBox checkbox = UnoRuntime.queryInterface(XCheckBox.class, xControl);
      checkbox.addItemListener(this);
      break;
    case RADIO:
      XRadioButton radioButton = UnoRuntime.queryInterface(XRadioButton.class,
          xControl);
      radioButton.addItemListener(this);
      break;
    default:
      break;
    }
  }

  @Override
  public void disposing(EventObject arg0)
  {
    //
  }

  @Override
  public void itemStateChanged(ItemEvent arg0)
  {
    //
  }

  @Override
  public void actionPerformed(ActionEvent arg0)
  {
    //
  }

  @Override
  public void textChanged(TextEvent arg0)
  {
    XControl xControl = UnoRuntime.queryInterface(XControl.class, arg0.Source);
    XControlModel xControlModel = xControl.getModel();
    XPropertySet propertySet = UnoRuntime.queryInterface(XPropertySet.class,
        xControlModel);
    try
    {
      String sName = (String) propertySet.getPropertyValue("Name");
      String textValue = (String) propertySet.getPropertyValue("Text");
      
      if(!sName.isEmpty() && !textValue.isEmpty()) {
        //
      }
    }
    catch (Exception e)
    {
      LOGGER.error("", e);
    }
  }
}
