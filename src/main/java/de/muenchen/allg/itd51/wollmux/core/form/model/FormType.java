package de.muenchen.allg.itd51.wollmux.core.form.model;

/**
 * Die Typen von Formularelementen.
 * 
 * @author daniel.sikeler
 *
 */
public enum FormType
{
  /**
   * Ein Textfeld.
   */
  TEXTFIELD,
  /**
   * Eine Textarea.
   */
  TEXTAREA,
  /**
   * Eine Combobox.
   */
  COMBOBOX,
  /**
   * Eine Checkbox.
   */
  CHECKBOX,
  /**
   * Ein Label.
   */
  LABEL,
  /**
   * Ein Separator. Wird je nach Kontext zu {@link FormType#V_SEPARATOR} oder
   * {@link FormType#H_SEPARATOR}.
   */
  SEPARATOR,
  /**
   * Ein vertikaler Separator.
   */
  V_SEPARATOR,
  /**
   * Ein horizontaler Separator.
   */
  H_SEPARATOR,
  /**
   * Ein Abstand. Wird je nach Kontext zu {@link FormType#V_GLUE} oder {@link FormType#H_GLUE}.
   */
  GLUE,
  /**
   * Ein vertikaler Abstand.
   */
  V_GLUE,
  /**
   * Ein horizontaler Abstand.
   */
  H_GLUE,
  /**
   * Ein Button.
   */
  BUTTON,
  /**
   * Ein Menüeintrag.
   */
  MENUITEM,
  /**
   * Eine Listbox.
   */
  LISTBOX;

  /**
   * Ordnet einem String den entsprechende FormType zu.
   * 
   * @param type
   *          Der String.
   * @return Der FormType.
   * @throws FormModelException
   *           Wenn für den String kein FormType existiert.
   */
  public static FormType getType(String type) throws FormModelException
  {
    switch (type)
    {
    case "textfield":
      return FormType.TEXTFIELD;
    case "textarea":
      return FormType.TEXTAREA;
    case "combobox":
      return FormType.COMBOBOX;
    case "checkbox":
      return FormType.CHECKBOX;
    case "label":
      return FormType.LABEL;
    case "separator":
      return FormType.SEPARATOR;
    case "v-separator":
      return FormType.V_SEPARATOR;
    case "h-separator":
      return FormType.H_SEPARATOR;
    case "glue":
      return FormType.GLUE;
    case "v-glue":
      return FormType.V_GLUE;
    case "h-glue":
      return FormType.H_GLUE;
    case "button":
      return FormType.BUTTON;
    case "menuitem":
      return FormType.MENUITEM;
    case "listbox":
      return FormType.LISTBOX;
    default:
      throw new FormModelException("Unbekannte TYPE-Angabe für ein Formularelement.");
    }
  }

  /**
   * Ordnet einem FormType den entsprechende String zu.
   * 
   * @param type
   *          Der FormType.
   * @return Der String.
   * @throws FormModelException
   *           Wenn für den FormType kein String existiert.
   */
  public static String getString(FormType type) throws FormModelException
  {
    switch (type)
    {
    case TEXTFIELD:
      return "textfield";
    case TEXTAREA:
      return "textarea";
    case COMBOBOX:
      return "combobox";
    case CHECKBOX:
      return "checkbox";
    case LABEL:
      return "label";
    case SEPARATOR:
    case H_SEPARATOR:
    case V_SEPARATOR:
      return "separator";
    case GLUE:
    case H_GLUE:
    case V_GLUE:
      return "glue";
    case BUTTON:
      return "button";
    case MENUITEM:
      return "menuitem";
    case LISTBOX:
      return "listbox";
    default:
      throw new FormModelException("Unbekannte TYPE-Angabe für ein Formularelement.");
    }
  }
}
