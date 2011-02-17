package com.atlassian.jira.ext.commitacceptance.server.evaluator.predicate;

import java.util.List;
import java.util.Set;

import com.atlassian.jira.ext.commitacceptance.server.exception.PredicateViolatedException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;

/**
 * All issues passed to this predicate should have a value in the custom field 
 * which matches the passed value.
 *
 * @author <a href="mailto:rneale@palantir.com">Rich Neale</a>
 * @author <a href="mailto:cmyers@palantir.com">Carl Myers</a>
 */
public class DoesCommitMatchCustomField implements JiraPredicate {
	private String fieldValue;
	private final CustomField customField;
	

	public DoesCommitMatchCustomField(CustomField customField, String fieldValue) {
		this.fieldValue = fieldValue;
		this.customField = customField;
	}

	@SuppressWarnings("unchecked")
	public void evaluate(Set<Issue> issues) {
		String cause = null;

		for (Issue issue : issues) {
			try {
				List<String> allowedValues;

				// get the allowed custom field value for the issue if set
				allowedValues = (List<String>) issue.getCustomFieldValue(customField);
				if (allowedValues == null) {
					cause = "Issue has no '" + customField.getName() + "' field defined.";
					throw new PredicateViolatedException(cause);
				}
				
				// if at least one issue has an allowed custom field value but it does not matche this value.
				if (!allowedValues.contains(fieldValue)) {
					cause = "This commit can only be made to issues where field \""
						+ customField.getName() + "\" contains \""
						+ allowedValues.toString() + "\".";
					throw new PredicateViolatedException(cause);
				}
			} catch (Throwable e) {
				if (e instanceof PredicateViolatedException) {
					PredicateViolatedException pe = (PredicateViolatedException) e;
				    throw pe;
				}
				cause = "Unknown error has occured: " + e.getMessage();
				throw new PredicateViolatedException(cause);
			}	
		}
	}
}
