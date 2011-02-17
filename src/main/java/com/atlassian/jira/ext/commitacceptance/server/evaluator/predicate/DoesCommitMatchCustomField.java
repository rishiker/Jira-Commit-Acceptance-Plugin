package com.atlassian.jira.ext.commitacceptance.server.evaluator.predicate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.atlassian.jira.ext.commitacceptance.server.exception.PredicateViolatedException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.opensymphony.user.EntityNotFoundException;

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

	public void evaluate(Set issues) {
		String cause = null;

		for (Iterator it = issues.iterator(); it.hasNext();) {
			try {
				Issue issue = (Issue)it.next();
				List<String> allowedValues;

				// get the allowed branch for the issue if set
				try {
					allowedValues = getAllowedValues(issue, customField);
					if (allowedValues == null) {
						cause = "Issue has no '" + customField.getName() + "' field defined.";
						throw new PredicateViolatedException(cause);
					}
				} catch (EntityNotFoundException e) {
					cause = "Issue [" + issue.getKey() + "] was not found.";
					throw new PredicateViolatedException(cause);
				}
				
				// if at least one issue has an allowed branch but it does not matches this branch.
				if ( !valueIsAllowed(allowedValues, fieldValue)) {
					cause = "This commit can only be made to issues where field \""
						+ customField.getName() + "\" contains \""
						+ allowedValues.toString() + "\".";
					throw new PredicateViolatedException(cause);
				}
			} catch ( Throwable e ) {
				if ( e instanceof PredicateViolatedException ) {
					PredicateViolatedException pe = (PredicateViolatedException) e;
				    throw pe;
				}
				cause = "Unknown error has occured: " + e.getMessage();
				throw new PredicateViolatedException(cause);
			}	
		}
	}

	// Check if the given value is in the list of allowed values or not
    private boolean valueIsAllowed(List<String> allowedValues, String value) {
    	if (allowedValues.contains(value)) {
    		return true;
    	}
    	return false;
    }
    
	// Get the list of values that are allowed by the supplied Jira ticket
    @SuppressWarnings("unchecked")
	private List<String> getAllowedValues(Issue issue, CustomField customField)
	throws EntityNotFoundException {
	    List<String> values = (List<String>) issue.getCustomFieldValue(customField);
	    return values;		
	}
}
