package de.muenchen.allg.itd51.wollmux.core.dialog;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.awt.PosSize;
import com.sun.star.awt.Rectangle;
import com.sun.star.awt.WindowEvent;
import com.sun.star.awt.XButton;
import com.sun.star.awt.XCheckBox;
import com.sun.star.awt.XComboBox;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XProgressBar;
import com.sun.star.awt.XRadioButton;
import com.sun.star.awt.XTextComponent;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowListener;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.XMultiPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.dialog.ControlModel.Align;
import de.muenchen.allg.itd51.wollmux.core.dialog.ControlModel.Dock;
import de.muenchen.allg.itd51.wollmux.core.dialog.ControlModel.Orientation;

public class SimpleDialogLayout implements XWindowListener
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SimpleDialogLayout.class);
  private XControlContainer controlContainer;
  private XWindow containerWindow;
  private int marginBetweenControls = 5;
  private List<ControlModel> controlList = new ArrayList<>();
  private int yOffset = 0;
  private int xOffset = 0;
  private int marginTop = 0;
  private int marginRight = 0;
  private int marginLeft = 0;
  private int windowBottomMargin = 0;

  public SimpleDialogLayout(XWindow dialogWindow)
  {
    this.containerWindow = dialogWindow;
    this.containerWindow.addWindowListener(this);
    this.controlContainer = UnoRuntime.queryInterface(XControlContainer.class,
        dialogWindow);
  }

  public void draw()
  {
    this.yOffset = this.getMarginTop() > 0 ? this.getMarginTop() : 0;
    this.xOffset = this.getMarginLeft() > 0 ? this.getMarginLeft() : 0;

    Rectangle windowRect = this.getContainerWindow().getPosSize();

    for (ControlModel entry : this.getControlList())
    {
      entry.getDock().ifPresent(dock -> this.setDock(windowRect, entry, dock));

      if (entry.getOrientation() == Orientation.HORIZONTAL)
      {
        for (SimpleEntry<ControlProperties, XControl> control : entry
            .getControls())
        {
          XControl controlByControlContainer = this.controlContainer
              .getControl(control.getKey().getControlName());
          if (controlByControlContainer != null)
          {
            modifyHorizontalControl(windowRect, controlByControlContainer,
                control.getKey(), entry.getAlignment(),
                entry.getControls().size());
          } else
          {
            this.modifyHorizontalControl(windowRect, control.getValue(),
                control.getKey(), entry.getAlignment(),
                entry.getControls().size());

            this.addControlToControlContainer(control.getKey().getControlName(),
                control.getValue());
          }
        }
      } else if (entry.getOrientation() == Orientation.VERTICAL)
      {
        for (SimpleEntry<ControlProperties, XControl> control : entry
            .getControls())
        {
          XControl controlByControlContainer = this.controlContainer
              .getControl(control.getKey().getControlName());
          if (controlByControlContainer != null)
          {
            this.modifyVerticalControl(windowRect, control.getValue(),
                control.getKey(), entry.getAlignment());
          } else
          {
            this.modifyVerticalControl(windowRect, control.getValue(),
                control.getKey(), entry.getAlignment());

            this.addControlToControlContainer(control.getKey().getControlName(),
                control.getValue());
          }
        }
      }

      this.newLine(entry);
    }
  }

  private Optional<SimpleEntry<ControlProperties, XControl>> getMaxControlHeightByControlList(
      ControlModel controlModel)
  {
    return controlModel.getControls().stream()
        .max((v1, v2) -> Integer.compare(v1.getKey().getControlHeight(),
            v2.getKey().getControlHeight()));
  }

  private void setDock(Rectangle windowRect, ControlModel controlModel,
      Dock dock)
  {
    if (dock == Dock.BOTTOM)
    {
      Optional<SimpleEntry<ControlProperties, XControl>> maxControlHeight = this
          .getMaxControlHeightByControlList(controlModel);
      this.yOffset = this.getBottomYOffset(windowRect,
          maxControlHeight.isPresent()
              ? maxControlHeight.get().getKey().getControlHeight()
              : 0);
    }
  }

  private void modifyHorizontalControl(Rectangle windowRect, XControl control,
      ControlProperties controlProperties, Align alignment, int controlCount)
  {
    XWindow wnd = UnoRuntime.queryInterface(XWindow.class, control);

    int calculatedControlWidth = controlProperties.getControlPercentWidth() > 0
        ? ((windowRect.Width / 100)
            * controlProperties.getControlPercentWidth())
            - (2 * this.getMarginLeft() / controlCount)
        : (windowRect.Width - (controlCount * 10)) / controlCount;

    if (alignment == Align.RIGHT)
    {
      int calculatedControlWidthRight = (windowRect.Width / 2
          - (controlCount * 5)) / controlCount;
      wnd.setPosSize(xOffset + windowRect.Width / 2, yOffset,
          calculatedControlWidthRight, controlProperties.getControlHeight(),
          (short) (PosSize.POSSIZE));
    } else if (alignment == Align.LEFT)
    {
      int calculatedControlWidthLeft = (windowRect.Width / 2
          - (controlCount * 5)) / controlCount;
      wnd.setPosSize(xOffset, yOffset, calculatedControlWidthLeft,
          controlProperties.getControlHeight(), (short) (PosSize.POSSIZE));
    } else
    {
      xOffset = controlProperties.getMarginLeft() > 0
          ? controlProperties.getMarginLeft()
          : xOffset;

      // full width
      wnd.setPosSize(xOffset, yOffset, calculatedControlWidth,
          controlProperties.getControlHeight(), (short) (PosSize.POSSIZE));
    }

    xOffset += calculatedControlWidth + this.getMarginBetweenControls();
  }

  private void modifyVerticalControl(Rectangle windowRect, XControl control,
      ControlProperties controlProperties, Align alignment)
  {
    XWindow wnd = UnoRuntime.queryInterface(XWindow.class, control);

    Rectangle cr = wnd.getPosSize();

    if (alignment == Align.RIGHT)
    {
      wnd.setPosSize(windowRect.Width / 2, yOffset, windowRect.Width / 2,
          controlProperties.getControlHeight(), (short) (PosSize.POSSIZE));
    } else if (alignment == Align.LEFT)
    {
      wnd.setPosSize(0, yOffset, windowRect.Width / 2,
          controlProperties.getControlHeight(), (short) (PosSize.POSSIZE));
    } else
    {
      int xOffsetTemp = 0;

      xOffsetTemp = this.getMarginLeft() > 0 ? this.getMarginLeft()
          : xOffsetTemp;
      xOffsetTemp = controlProperties.getMarginLeft() > 0
          ? controlProperties.getMarginLeft()
          : xOffsetTemp;

      // full width
      wnd.setPosSize(xOffsetTemp, yOffset, windowRect.Width,
          controlProperties.getControlHeight(), (short) (PosSize.POSSIZE));
    }

    yOffset += cr.Height + (controlProperties.getMarginBetweenControls() > 0
        ? controlProperties.getMarginBetweenControls()
        : this.getMarginBetweenControls());
  }

  private void newLine(ControlModel controlModel)
  {
    yOffset += controlModel.getLinebreakHeight();
    xOffset = this.getMarginLeft() > 0 ? this.getMarginLeft() : 0;
  }

  public SimpleEntry<ControlProperties, XControl> convertToXControl(
      ControlProperties controlProperties)
  {
    String controlType = controlProperties.getControlType().toString();
    Object control = UNO.createUNOService(controlType);

    Object editModel = null;
    try
    {
      editModel = UNO.xMSF.createInstance(controlType + "Model");
    }
    catch (Exception e1)
    {
      LOGGER.error("", e1);
    }

    XMultiPropertySet propertySet = UnoRuntime
        .queryInterface(XMultiPropertySet.class, editModel);

    try
    {
      propertySet.setPropertyValues(controlProperties.getPropertySet().getKey(),
          controlProperties.getPropertySet().getValue());
    }
    catch (IllegalArgumentException | PropertyVetoException
        | WrappedTargetException e)
    {
      LOGGER.error("", e);
    }

    XControlModel modelX = UnoRuntime.queryInterface(XControlModel.class,
        editModel);

    XControl xControl = UnoRuntime.queryInterface(XControl.class, control);
    this.castControl(controlProperties, xControl);
    XWindow wnd = UnoRuntime.queryInterface(XWindow.class, xControl);
    wnd.setPosSize(0, 0, controlProperties.getControlWidth(),
        controlProperties.getControlHeight(), PosSize.SIZE);

    xControl.setModel(modelX);

    return new SimpleEntry<>(controlProperties, xControl);
  }

  int groupBoxHeight;

  public int calcGroupBoxHeightByControlProperties(
      List<ControlModel> controlModels)
  {
    if (controlModels.isEmpty())
    {
      return 0;
    }

    groupBoxHeight = 0;

    controlModels.parallelStream().forEach(controlModel -> controlModel
        .getControls().parallelStream().forEach(controlProperty -> {
          groupBoxHeight += controlProperty.getKey().getControlHeight()
              + controlProperty.getKey().getMarginBetweenControls();
        }));

    return groupBoxHeight + 10;
  }

  private void castControl(ControlProperties controlProperties,
      XControl xControl)
  {
    switch (controlProperties.getControlType())
    {
    case EDIT:
      XTextComponent textComponent = UnoRuntime
          .queryInterface(XTextComponent.class, xControl);
      break;
    case BUTTON:
      XButton button = UnoRuntime.queryInterface(XButton.class, xControl);
      button.setActionCommand(controlProperties.getButtonCommand());
      break;
    case PROGRESSBAR:
      XProgressBar progressBar = UnoRuntime.queryInterface(XProgressBar.class,
          xControl);
      progressBar.getValue();
      break;
    case CHECKBOX:
      XCheckBox checkbox = UnoRuntime.queryInterface(XCheckBox.class, xControl);
      break;
    case RADIO:
      XRadioButton radioButton = UnoRuntime.queryInterface(XRadioButton.class,
          xControl);
      break;
    case COMBOBOX:
      XComboBox comboBox = UnoRuntime.queryInterface(XComboBox.class, xControl);
      break;
    case SPINBUTTON:
      break;
    default:
      break;
    }
  }

  private int getBottomYOffset(Rectangle windowRect, int controlHeight)
  {
    return windowRect.Height - controlHeight - this.getWindowBottomMargin();
  }

  private void addControlToControlContainer(String name, XControl control)
  {
    this.getControlContainer().addControl(name, control);
  }

  public void addControlsToList(ControlModel control)
  {
    this.controlList.add(control);
  }

  public List<ControlModel> getControlList()
  {
    return this.controlList;
  }

  private int getMarginBetweenControls()
  {
    return this.marginBetweenControls;
  }

  public void setMarginBetweenControls(int margin)
  {
    this.marginBetweenControls = margin;
  }

  public void setMarginTop(int marginTop)
  {
    this.marginTop = marginTop;
  }

  private int getMarginTop()
  {
    return this.marginTop;
  }

  public void setMarginRight(int marginRight)
  {
    this.marginRight = marginRight;
  }

  private int getMarginRight()
  {
    return this.marginRight;
  }

  public void setMarginLeft(int marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  private int getWindowBottomMargin()
  {
    return this.windowBottomMargin;
  }

  public void setWindowBottomMargin(int marginBottom)
  {
    this.windowBottomMargin = marginBottom;
  }

  private int getMarginLeft()
  {
    return this.marginLeft;
  }

  public XControl getControl(String name)
  {
    return this.getControlContainer().getControl(name);
  }

  public XControl[] getControls()
  {
    return this.getControlContainer().getControls();
  }

  public XControlContainer getControlContainer()
  {
    if (this.controlContainer == null)
    {
      LOGGER.error(
          "BaseLayout: getControlContainer(): controlContainer is NULL.");
    }

    return this.controlContainer;
  }

  public XWindow getContainerWindow()
  {
    if (this.containerWindow == null)
    {
      LOGGER
          .error("BaseLayout: getContainerWindow(): containerWindow is NULL.");
    }

    return this.containerWindow;
  }

  @Override
  public void disposing(EventObject arg0)
  {
    // ...
  }

  @Override
  public void windowHidden(EventObject arg0)
  {
    // ...
  }

  @Override
  public void windowMoved(WindowEvent arg0)
  {
    // ...
  }

  @Override
  public void windowResized(WindowEvent arg0)
  {
    this.draw();
  }

  @Override
  public void windowShown(EventObject arg0)
  {
    this.draw();
  }
}