 /* Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.platform.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.core.Activator;

public class OpenDashboardHandler  extends AbstractHandler {
	
	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	static final String INTRO_VIEW_ID = "org.eclipse.ui.internal.introview";
	static final String DASHBOARD_VIEW_ID = "org.wso2.developerstudio.eclipse.dashboard";
	static final String J2EE_PERSPECTIVE_ID = "org.eclipse.jst.j2ee.J2EEPerspective";
	

    public Object execute(ExecutionEvent event) throws ExecutionException {
		  IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	        IWorkbenchPage page = window.getActivePage();
	        try {
	        	hideIntroView();
	        	hideDashboards();
	        	PlatformUI.getWorkbench().showPerspective(J2EE_PERSPECTIVE_ID, window);
				page.openEditor(new NullEditorInput(), DASHBOARD_VIEW_ID);
			} catch (Exception e) {
				log.error("Cannot open dashboard", e);
			}
	    return true;
    }
    
    /**
     * hide eclipse welcome page
     */
    private void hideIntroView(){
    	try {
    		 IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    		 IWorkbenchPage page = window.getActivePage();
    		 IViewReference ref = page.findViewReference(INTRO_VIEW_ID);
    		 page.hideView(ref);
		} catch (Exception e) {
			/* safe to ignore */
		}
    }
    
    /**
     * hide open dashboards
     */
    private void hideDashboards(){
    	try {
    		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			List<IEditorReference> openEditors = new ArrayList<IEditorReference>();
			IEditorReference[] editorReferences = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getEditorReferences();
			for (IEditorReference iEditorReference : editorReferences) {
				if (DASHBOARD_VIEW_ID.equals(iEditorReference.getId())) {
					openEditors.add(iEditorReference);
				}
			}
			if (openEditors.size() > 0) {
				page.closeEditors(openEditors.toArray(new IEditorReference[] {}), false);
			}
		} catch (Exception e) {
			/* safe to ignore */
		}
    }
	
	class NullEditorInput implements IEditorInput {

		public boolean exists() {
		return true;
		}

		public ImageDescriptor getImageDescriptor() {
		return ImageDescriptor.getMissingImageDescriptor();
		}

		public String getName() {
		return "Dashboard";
		}

		public IPersistableElement getPersistable() {
		return null;
		}

		public String getToolTipText() {
		return "Developer Studio Dashboard";
		}

		public Object getAdapter(Class adapter) {
		return null;
		}
		} 

}
