package de.muenchen.allg.itd51.wollmux.core.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.star.awt.Rectangle;
import com.sun.star.awt.WindowAttribute;
import com.sun.star.awt.WindowClass;
import com.sun.star.awt.WindowDescriptor;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlContainer;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.chart2.XFormattedString;
import com.sun.star.chart2.XTitle;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XFramesSupplier;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import de.muenchen.allg.afid.UNO;

public class UNODialogFactory
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(UNODialogFactory.class);
  private XToolkit xToolkit;
  private XControl m_xDialogControl;
  private XControlContainer contXContainer;
  private XComponentContext context;
  private XMultiComponentFactory xmcf;
  private XWindow modalBaseDialogWindow;

  public UNODialogFactory(XMultiComponentFactory xmcf,
      XComponentContext context)
  {
    this.xmcf = xmcf;
    this.context = context;
  }

  public void createDialog() throws Exception
  {
    Object cont = UNO.createUNOService("com.sun.star.awt.UnoControlContainer");
    m_xDialogControl = UnoRuntime.queryInterface(XControl.class, cont);
    contXContainer = UnoRuntime.queryInterface(XControlContainer.class, cont);

    Object unoControlContainerModelO = UNO
        .createUNOService("com.sun.star.awt.UnoControlContainerModel");
    XControlModel unoControlContainerModel = UnoRuntime
        .queryInterface(XControlModel.class, unoControlContainerModelO);
    m_xDialogControl.setModel(unoControlContainerModel);

    XWindow contXWindow = UNO.XWindow(m_xDialogControl);

    Object toolkit = xmcf.createInstanceWithContext("com.sun.star.awt.Toolkit",
        context);
    xToolkit = UnoRuntime.queryInterface(XToolkit.class, toolkit);

    XWindow currentWindow = UNO.desktop.getCurrentFrame().getContainerWindow();
    XWindowPeer currentWindowPeer = UNO.XWindowPeer(currentWindow);
    XWindowPeer modalBaseDialog = createModalBaseDialog(xToolkit,
        currentWindowPeer);
    modalBaseDialogWindow = UNO.XWindow(modalBaseDialog);

    // dialog = UnoRuntime.queryInterface(XDialog.class, modalBaseDialogWindow);
    // dialog.setTitle("test");

    Object testFrame = xmcf
        .createInstanceWithContext("com.sun.star.frame.Frame", context);
    XFrame xFrameTest = UNO.XFrame(testFrame);

    xFrameTest.initialize(modalBaseDialogWindow);
    XFramesSupplier creator = UNO.desktop.getCurrentFrame().getCreator();
    xFrameTest.setCreator(creator);
    xFrameTest.activate();
    m_xDialogControl.createPeer(xToolkit, modalBaseDialog);

    boolean isSuccessfullySet = xFrameTest.setComponent(contXWindow, null);

    if (!isSuccessfullySet)
    {
      LOGGER.error(
          "UNODialogExample: createDialog: XFrame has not been set successfully.");
      return;
    }

    modalBaseDialogWindow.setEnable(true);
    modalBaseDialogWindow.setVisible(true);
  }

  public static XTitle createTitle(String titleString)
  {
    XTitle xtitle = UnoRuntime.queryInterface(XTitle.class,
        "com.sun.star.chart2.Title");
    if (xtitle == null)
    {
      System.out.println("Unable to create xtitle interface");
      return null;
    }

    XFormattedString xtitleStr = UnoRuntime.queryInterface(
        XFormattedString.class, "com.sun.star.chart2.FormattedString");
    if (xtitleStr == null)
    {
      System.out.println("Unable to create formatted string");
      return null;
    }
    xtitleStr.setString(titleString);
    XFormattedString[] titleArray = new XFormattedString[] { xtitleStr };
    xtitle.setText(titleArray);

    return xtitle;
  }

  public XControlContainer getControlContainer()
  {
    return this.contXContainer;
  }

  public XWindow getDialogWindow()
  {
    return this.modalBaseDialogWindow;
  }

  private XWindowPeer createModalBaseDialog(XToolkit toolkit,
      XWindowPeer parentWindow) throws IllegalArgumentException
  {
    com.sun.star.awt.Rectangle rect = new Rectangle();

    XWindow parentXWindow = UNO.XWindow(parentWindow);
    rect.X = parentXWindow.getPosSize().Width / 2;
    rect.Y = parentXWindow.getPosSize().Height / 2;
    rect.Width = parentXWindow.getPosSize().Width / 3;
    rect.Height = parentXWindow.getPosSize().Height / 3;

    WindowDescriptor aWindow = new WindowDescriptor();
    aWindow.Type = WindowClass.TOP;
    aWindow.WindowServiceName = "window";
    aWindow.Parent = parentWindow;
    aWindow.ParentIndex = -1;
    aWindow.Bounds = rect;
    aWindow.WindowAttributes = WindowAttribute.CLOSEABLE
        | WindowAttribute.SIZEABLE | WindowAttribute.MOVEABLE
        | WindowAttribute.BORDER;

    return toolkit.createWindow(aWindow);
  }

}
