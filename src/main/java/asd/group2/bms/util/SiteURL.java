package asd.group2.bms.util;

import javax.servlet.http.HttpServletRequest;

public class SiteURL {

  public static String getSiteURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }

}
