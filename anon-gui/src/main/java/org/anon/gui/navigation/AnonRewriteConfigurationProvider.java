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
		
		return ConfigurationBuilder.begin()
				
				.addRule(Join.path("/doLogin")
						.to(NavigationCaseEnum.LOGIN.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/doConnect")
						.to(NavigationCaseEnum.CONNECT.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/addConnection")
						.to(NavigationCaseEnum.ADD_CONNECTION.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/listConnection")
						.to(NavigationCaseEnum.LIST_CONNECTION.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/listTables")
						.to(NavigationCaseEnum.TABLES.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/listColumns")
						.to(NavigationCaseEnum.COLUMNS.getUrl())
						.withInboundCorrection())
				.addRule(Join.path("/doAnonymize")
						.to(NavigationCaseEnum.ANONYMIZE.getUrl())
						.withInboundCorrection());
	}

	@Override
	public int priority() {
		return 10;
	}

}
