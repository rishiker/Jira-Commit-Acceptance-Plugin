package com.atlassian.jira.ext.commitacceptance.server.evaluator.predicate;

import java.util.Set;

import com.atlassian.jira.ext.commitacceptance.server.exception.PredicateViolatedException;
import com.atlassian.jira.issue.Issue;

/**
 * Predicate interface to implement by concrete predicates.
 *
 * @author <a href="mailto:ferenc.kiss@midori.hu">Ferenc Kiss</a>
 * @version $Id$
 */
public interface JiraPredicate {
	/**
	 * It should simply return if the predicate evaluates to "true"
	 * or throw {@link PredicateViolatedException} otherwise.
	 */
	void evaluate(Set<Issue> issues);
}
