package de.muenchen.allg.itd51.wollmux.core.functions;

import java.util.Map;

import de.muenchen.allg.itd51.wollmux.core.dialog.DialogLibrary;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigurationErrorException;

public class LengthFunction extends CatFunction
{
  public LengthFunction(ConfigThingy conf, FunctionLibrary funcLib,
      DialogLibrary dialogLib, Map<Object, Object> context)
      throws ConfigurationErrorException
  {
    super(conf, funcLib, dialogLib, context);
  }

  @Override
  public String getString(Values parameters)
  {
    String res = super.getString(parameters);
    if (res == FunctionLibrary.ERROR) return FunctionLibrary.ERROR;
    return "" + res.length();
  }

  @Override
  public boolean getBoolean(Values parameters)
  {
    return false;
  }
}