package org.anon.gui.navigation;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;


@RewriteConfiguration
public class AnonRewriteConfigurationProvider extends HttpConfigurationProvider {

	@Override
	public Configuration getConfiguration(ServletContext arg0) {
		
		ConfigurationBuilder cfgBuilder = ConfigurationBuilder.begin();
		
		for(NavigationCaseEnum nav : NavigationCaseEnum.values()) {
			
			cfgBuilder.addRule(Join.path(nav.getPath())
					.to(nav.getUrl())
					.withInboundCorrection());
		}
		
		return cfgBuilder;
	}

	@Override
	public int priority() {
		return 10;
	}

}
