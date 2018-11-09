package de.muenchen.allg.itd51.wollmux.core.dialog;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.star.awt.PosSize;
import com.sun.star.awt.Rectangle;
import com.sun.star.awt.WindowEvent;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowListener;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.uno.UnoRuntime;
import de.muenchen.allg.itd51.wollmux.dialog.ControlModel.Align;
import de.muenchen.allg.itd51.wollmux.dialog.ControlModel.Dock;
import de.muenchen.allg.itd51.wollmux.dialog.ControlModel.Orientation;

public class SimpleDialogLayout implements XWindowListener
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(SimpleDialogLayout.class);
  private XControlContainer controlContainer;
  private XWindow containerWindow;
  private int linebreakHeight = 20;
  private int marginBetweenControls = 5;
  private List<ControlModel> controlList = new ArrayList<ControlModel>();
  private int yOffset = 0;
  private int xOffset = 0;
  private int controlHeight = 20; // default

  public SimpleDialogLayout(XControlContainer controlContainer,
      XWindow containerWindow)
  {
    this.controlContainer = controlContainer;
    this.containerWindow = containerWindow;
    this.containerWindow.addWindowListener(this);
  }

  public void draw() throws UnknownPropertyException, WrappedTargetException
  {
    this.yOffset = 0;

    Rectangle windowRect = this.getContainerWindow().getPosSize();

    for (ControlModel entry : this.getControlList())
    {
      entry.getDock().ifPresent(dock -> this.setDock(windowRect, entry, dock));

      if (entry.getOrientation() == Orientation.HORIZONTAL)
      {
	for (XControl control : entry.getControls())
	{
	  this.addControlHorizontal(windowRect, control, entry.getAlignment(),
	      entry.getControls().size());
	}
      } else if (entry.getOrientation() == Orientation.VERTICAL)
      {
	for (XControl control : entry.getControls())
	{
	  this.addControlVertical(windowRect, control, entry.getAlignment(),
	      entry.getControls().size());
	}
      }

      this.newLine(windowRect);
    }
  }

  private void setDock(Rectangle windowRect, ControlModel controlModel,
      Dock dock)
  {
    if (dock == Dock.BOTTOM)
    {
      this.yOffset = this.getBottomYOffset(windowRect, this.getControlHeight(),
          controlModel.getControls().size());
    }
  }

  private void addControlHorizontal(Rectangle windowRect, XControl control,
      Align alignment, int controlCount)
      throws UnknownPropertyException, WrappedTargetException
  {
    XWindow wnd = UnoRuntime.queryInterface(XWindow.class, control);

    Rectangle cr = wnd.getPosSize();

    int calculatedControlWidth = (windowRect.Width - (controlCount * 10))
        / controlCount;

    if (alignment == Align.RIGHT)
    {
      int calculatedControlWidthRight = (windowRect.Width / 2
          - (controlCount * 5)) / controlCount;
      wnd.setPosSize(xOffset + windowRect.Width / 2, yOffset,
          calculatedControlWidthRight, this.getControlHeight(),
          (short) (PosSize.POSSIZE));
    } else if (alignment == Align.LEFT)
    {
      int calculatedControlWidthLeft = (windowRect.Width / 2
          - (controlCount * 5)) / controlCount;
      wnd.setPosSize(xOffset, yOffset, calculatedControlWidthLeft,
          this.getControlHeight(), (short) (PosSize.POSSIZE));
    } else
    {
      // full width
      wnd.setPosSize(xOffset, yOffset, calculatedControlWidth,
          this.getControlHeight(), (short) (PosSize.POSSIZE));
    }

    xOffset += cr.Width + this.getMarginBetweenControls();

    this.addControlToControlContainer(this.getControlName(control.getModel()),
        control);
  }

  private void addControlVertical(Rectangle windowRect, XControl control,
      Align alignment, int controlCount)
      throws UnknownPropertyException, WrappedTargetException
  {
    XWindow wnd = UnoRuntime.queryInterface(XWindow.class, control);

    Rectangle cr = wnd.getPosSize();

    if (alignment == Align.RIGHT)
    {
      wnd.setPosSize(windowRect.Width / 2, yOffset, windowRect.Width / 2,
          this.getControlHeight(), (short) (PosSize.POSSIZE));
    } else if (alignment == Align.LEFT)
    {
      wnd.setPosSize(0, yOffset, windowRect.Width / 2, this.getControlHeight(),
          (short) (PosSize.POSSIZE));
    } else
    {
      // full width
      wnd.setPosSize(0, yOffset, windowRect.Width, this.getControlHeight(),
          (short) (PosSize.POSSIZE));
    }

    yOffset += cr.Height + this.getMarginBetweenControls();

    this.addControlToControlContainer(this.getControlName(control.getModel()),
        control);
  }

  private String getControlName(XControlModel controlModel)
  {
    XPropertySet propertySet = UnoRuntime.queryInterface(XPropertySet.class,
        controlModel);

    try
    {
      return (String) propertySet.getPropertyValue("Name");
    } catch (UnknownPropertyException e)
    {
      LOGGER.error("", e);
    } catch (WrappedTargetException e)
    {
      LOGGER.error("", e);
    }

    return "";
  }

  private void newLine(Rectangle windowRect)
  {
    yOffset += this.getLinebreakHeight();
    xOffset = 0;
  }

  private int getBottomYOffset(Rectangle windowRect, int controlHeight,
      int controlCount)
  {
    return windowRect.Height - this.getMarginBetweenControls() - 5
        - (controlHeight * controlCount);
  }

  private void addControlToControlContainer(String name, XControl control)
  {
    this.getControlContainer().addControl(name, control);
  }

  public void addControlsToList(ControlModel control)
  {
    this.controlList.add(control);
  }

  private List<ControlModel> getControlList()
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

  private int getLinebreakHeight()
  {
    return this.linebreakHeight;
  }

  private int getControlHeight()
  {
    return this.controlHeight;
  }

  public void setControlHeight(int controlHeight)
  {
    this.controlHeight = controlHeight;
  }

  public void setLinebreakHeight(int height)
  {
    this.linebreakHeight = height;
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
    // TODO Auto-generated method stub
  }

  @Override
  public void windowHidden(EventObject arg0)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void windowMoved(WindowEvent arg0)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void windowResized(WindowEvent arg0)
  {
    try
    {
      this.draw();
    } catch (UnknownPropertyException e)
    {
      LOGGER.error("", e);
    } catch (WrappedTargetException e)
    {
      LOGGER.error("", e);
    }
  }

  @Override
  public void windowShown(EventObject arg0)
  {
    try
    {
      this.draw();
    } catch (UnknownPropertyException e)
    {
      LOGGER.error("", e);
    } catch (WrappedTargetException e)
    {
      LOGGER.error("", e);
    }
  }
}