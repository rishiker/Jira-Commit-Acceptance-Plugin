package com.atlassian.jira.ext.commitacceptance.server.evaluator.predicate;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.I18nHelper;

public abstract class AbstractPredicate implements JiraPredicate {
	
	protected JiraAuthenticationContext getJiraAuthenticationContext() {
		return ComponentManager.getInstance().getJiraAuthenticationContext();
	}

	protected I18nHelper getI18nHelper() {
		return getJiraAuthenticationContext().getI18nHelper();
	}
	
}
