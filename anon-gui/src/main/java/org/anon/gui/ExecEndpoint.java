package org.anon.gui;

import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

@PushEndpoint("/execEvent")
public class ExecEndpoint {

	@OnMessage(encoders = {JSONEncoder.class})
    public String onMessage(String data) {
        return StringEscapeUtils.escapeHtml(data);
    }
}
