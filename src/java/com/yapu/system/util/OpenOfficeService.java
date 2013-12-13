package com.yapu.system.util;

import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class OpenOfficeService {
	private static XComponentContext[] mxRemoteContext = null;
	private static int num = 1;

	public static void main(String[] args) {
		initOoo(1);
	}

	public static void initOoo(int n) {
		num = n;
		mxRemoteContext = new XComponentContext[num];
		for (int i = 0; i < num; i++) {
			try {
				// 创建XComponentContext对象
				XComponentContext xLocalContext = com.sun.star.comp.helper.Bootstrap
						.createInitialComponentContext(null);

				XMultiComponentFactory xLocalServiceManager = xLocalContext
						.getServiceManager();

				Object urlResolver = xLocalServiceManager
						.createInstanceWithContext(
								"com.sun.star.bridge.UnoUrlResolver",
								xLocalContext);
				XUnoUrlResolver xUnoUrlResolver = (XUnoUrlResolver) UnoRuntime
						.queryInterface(XUnoUrlResolver.class, urlResolver);
				// 连接open office url 
				String unoUrl = "uno:socket,host=127.0.0.1,port="
						+ (Integer.parseInt("8100") + i)
						+ ";urp;StarOffice.ServiceManager";
				Object initialObject = null;
				initialObject = xUnoUrlResolver.resolve(unoUrl);
				XPropertySet xPropertySet = (XPropertySet) UnoRuntime
						.queryInterface(XPropertySet.class, initialObject);
				Object context = xPropertySet
						.getPropertyValue("DefaultContext");
				mxRemoteContext[i] = (XComponentContext) UnoRuntime
						.queryInterface(XComponentContext.class, context);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Connection openoffice port fail!");
			}
		}
	}

}
