package de.muenchen.allg.itd51.wollmux.core.functions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import de.muenchen.allg.itd51.wollmux.core.dialog.DialogLibrary;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.core.parser.ConfigurationErrorException;

public class CatFunction extends MultiFunction
{
  public CatFunction(Collection<Function> subFunction)
  {
    super(subFunction);
  }

  public CatFunction(ConfigThingy conf, FunctionLibrary funcLib,
      DialogLibrary dialogLib, Map<Object, Object> context)
      throws ConfigurationErrorException
  {
    super(conf, funcLib, dialogLib, context);
  }

  @Override
  public String getString(Values parameters)
  {
    Iterator<Function> iter = subFunction.iterator();
    StringBuffer res = new StringBuffer();
    while (iter.hasNext())
    {
      Function func = iter.next();
      String str = func.getString(parameters);
      if (str == FunctionLibrary.ERROR) return FunctionLibrary.ERROR;
      res.append(str);
    }
    return res.toString();
  }
}