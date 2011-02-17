package com.atlassian.jira.ext.commitacceptance.server.evaluator.predicate;

import java.util.Set;

import com.atlassian.jira.ext.commitacceptance.server.exception.PredicateViolatedException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.project.Project;

/**
 * All issues passed to this predicate must be for the specified project.
 *
 * @author <a href="mailto:prkolbus@unusualcode.com">Peter Kolbus</a>
 * @version $Id$
 */
public class AreIssuesInProjectPredicate extends AbstractPredicate {
	private Project project;

	public AreIssuesInProjectPredicate(Project project) {
		this.project = project;
	}

	public void evaluate(Set<Issue> issues) {
		if (project == null) {
			throw new PredicateViolatedException(getErrorMessageWhenUsedInGlobalContext());
		}

		for (Issue issue : issues) {

			// If it's not equal, reject
			if (!project.equals(issue.getProjectObject())) {
				throw new PredicateViolatedException(getErrorMessageWhenIssueIsNotInProject(issue));
			}
		}
	}
	
	protected String getErrorMessageWhenUsedInGlobalContext() {
		return getI18nHelper().getText("commitAcceptance.predicate.issuesInProject.errorMessageWhenUsedInGlobalContext");
	}
	
	protected String getErrorMessageWhenIssueIsNotInProject(final Issue issue) {
		return getI18nHelper().getText("commitAcceptance.predicate.issuesInProject.errorMessageWhenIssueIsNotInProject",
				project.getKey(),
				issue.getKey());
	}
}
