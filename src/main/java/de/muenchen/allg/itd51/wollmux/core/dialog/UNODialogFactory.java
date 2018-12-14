package de.muenchen.allg.itd51.wollmux.core.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.star.awt.Rectangle;
import com.sun.star.awt.WindowAttribute;
import com.sun.star.awt.WindowClass;
import com.sun.star.awt.WindowDescriptor;
import com.sun.star.awt.XControl;
import com.sun.star.awt.XControlModel;
import com.sun.star.awt.XToolkit;
import com.sun.star.awt.XWindow;
import com.sun.star.awt.XWindowPeer;
import com.sun.star.chart2.XFormattedString;
import com.sun.star.chart2.XTitle;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XFramesSupplier;
import com.sun.star.uno.Exception;
import com.sun.star.uno.UnoRuntime;
import de.muenchen.allg.afid.UNO;

public class UNODialogFactory
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(UNODialogFactory.class);

  private XWindow modalBaseDialogWindow = null;
  
  public XWindow createDialog(int width, int height, int backgroundColor) throws Exception
  {
    Object cont = UNO.createUNOService("com.sun.star.awt.UnoControlContainer");
    XControl dialogControl = UnoRuntime.queryInterface(XControl.class, cont);

    Object unoControlContainerModelO = UNO
        .createUNOService("com.sun.star.awt.UnoControlContainerModel");
    XControlModel unoControlContainerModel = UnoRuntime
        .queryInterface(XControlModel.class, unoControlContainerModelO);
    dialogControl.setModel(unoControlContainerModel);

    XWindow contXWindow = UNO.XWindow(dialogControl);

    Object toolkit = UNO.xMCF.createInstanceWithContext(
        "com.sun.star.awt.Toolkit", UNO.defaultContext);
    XToolkit xToolkit = UnoRuntime.queryInterface(XToolkit.class, toolkit);

    XWindow currentWindow = UNO.desktop.getCurrentFrame().getContainerWindow();
    XWindowPeer currentWindowPeer = UNO.XWindowPeer(currentWindow);
    XWindowPeer modalBaseDialog = createModalBaseDialog(xToolkit,
        currentWindowPeer, width, height);
    this.modalBaseDialogWindow = UNO.XWindow(modalBaseDialog);

    Object testFrame = UNO.xMCF.createInstanceWithContext(
        "com.sun.star.frame.Frame", UNO.defaultContext);

    XFrame xFrame = UNO.XFrame(testFrame);
    xFrame.initialize(this.modalBaseDialogWindow);
    XFramesSupplier creator = UNO.desktop.getCurrentFrame().getCreator();
    xFrame.setCreator(creator);
    xFrame.activate();
    dialogControl.createPeer(xToolkit, modalBaseDialog);
    XWindowPeer testPeer = dialogControl.getPeer();
    testPeer.setBackground(backgroundColor);

    boolean isSuccessfullySet = xFrame.setComponent(contXWindow, null);

    if (!isSuccessfullySet)
    {
      LOGGER.error(
          "UNODialogExample: createDialog: XFrame has not been set successfully.");
      return contXWindow;
    }

    return contXWindow;
  }
  
  public void showDialog() {
    if(this.modalBaseDialogWindow == null) {
      LOGGER.error("Es wurde kein exestierendes Dialog-Fenster gefunden. Ein Dialog muss zuvor erstellt werden.");
      return;
    }
    
    this.modalBaseDialogWindow.setEnable(true);
    this.modalBaseDialogWindow.setVisible(true);
  }
  
  public void closeDialog() {
    if(this.modalBaseDialogWindow == null) {
      LOGGER.error("Es wurde kein exestierendes Dialog-Fenster gefunden. Ein Dialog muss zuvor erstellt werden.");
      return;
    }
    
    //this.modalBaseDialogWindow.setEnable(false);
    //this.modalBaseDialogWindow = null;
    //this.modalBaseDialogWindow.dispose();
    this.modalBaseDialogWindow.setVisible(false);
  }

  public static XTitle createTitle(String titleString)
  {
    XTitle xtitle = UnoRuntime.queryInterface(XTitle.class,
        "com.sun.star.chart2.Title");

    if (xtitle == null)
    {
      return null;
    }

    XFormattedString xtitleStr = UnoRuntime.queryInterface(
        XFormattedString.class, "com.sun.star.chart2.FormattedString");
    if (xtitleStr == null)
    {
      return null;
    }
    xtitleStr.setString(titleString);
    XFormattedString[] titleArray = new XFormattedString[] { xtitleStr };
    xtitle.setText(titleArray);

    return xtitle;
  }

  private XWindowPeer createModalBaseDialog(XToolkit toolkit,
      XWindowPeer parentWindow, int width, int height)
  {
    com.sun.star.awt.Rectangle rect = new Rectangle();

    XWindow parentXWindow = UNO.XWindow(parentWindow);
    rect.X = (parentXWindow.getPosSize().Width / 2) - (width / 2);
    rect.Y = (parentXWindow.getPosSize().Height / 2) - (height / 2);
    rect.Width = width;
    rect.Height = height;

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
