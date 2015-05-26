package org.anon.gui;

import org.anon.exec.GuiNotifier;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GuiNotifierImpl implements GuiNotifier{

	@Override
	public void refreshExecGui(String message){
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish("/execEvent", "event");
	}
	
//	@Override
//	public void refreshReductionExecGui() {
//		EventBus eventBus = EventBusFactory.getDefault().eventBus();
//		eventBus.publish("/reductionExecEvent", "event");
//	}
}
