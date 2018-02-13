package jsfwf.util;

import org.jboss.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

// Taken from an example by MkYong.
// Supports a component to change languages on the fly.
//
@ManagedBean(name="language")
@SessionScoped
public class LanguageBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LanguageBean.class); 
	
	private String localeCode = "hu";
	
	private static Map<String,Object> countries;
	static{
		countries = new LinkedHashMap<>();
		countries.put("English", Locale.ENGLISH);
		countries.put("Magyar", new Locale("hu"));
	}

	public Map<String, Object> getCountriesInMap() {
		return countries;
	}

	
	public String getLocaleCode() { return localeCode;	}
	public void setLocaleCode(String localeCode) { this.localeCode = localeCode; }


	public void countryLocaleCodeChanged(ValueChangeEvent e){
	
	    LOGGER.debug("countryLocaleCodeChanged()");
                
		String newLocaleValue = e.getNewValue().toString();
		
		// loop a map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            LOGGER.debug("  entry:" + entry.getValue().toString() + " new: " + newLocaleValue);
        	if (entry.getValue().toString().equals(newLocaleValue)){
        	
        	    Locale loc = ((Locale)entry.getValue());
        	    localeCode = loc.getLanguage();
        		FacesContext.getCurrentInstance().getViewRoot().setLocale(loc);
        	}
        }
	}
}
