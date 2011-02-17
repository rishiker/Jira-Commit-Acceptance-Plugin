package com.atlassian.jira.ext.commitacceptance.server.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;

/**
 * Loads and saves the commit acceptance settings and handles the UI related.
 *
 * @see AcceptanceSettings
 *
 * @author <a href="mailto:ferenc.kiss@midori.hu">Ferenc Kiss</a>
 * @version $Id$
 */
public class ConfigureAction extends JiraWebActionSupport {
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ConfigureAction.class);

	/*
	 * Services.
	 */
	private ProjectManager projectManager;
	private AcceptanceSettingsManager acceptanceSettingsManager;

	/**
	 * Key of the selected project or empty string for global settings.
	 */
	private String projectKey = "";
	/**
	 * Commit acceptance settings edited.
	 */
    private AcceptanceSettings settings = new AcceptanceSettings();
    /**
     * Submission identifier value.
     */
    private String submitted;

	public ConfigureAction(ProjectManager projectManager, AcceptanceSettingsManager acceptanceSettingsManager) {
        this.projectManager = projectManager;
		this.acceptanceSettingsManager = acceptanceSettingsManager;
	}

    public String execute() throws Exception {
    	// reject if user has no admin rights
    	if(!isHasPermission(Permissions.ADMINISTER)) {
    		return ERROR;
    	}
    	
        if (submitted == null) {
            // load old settings
        	logger.info("Loading commit acceptance settings for [" + projectKey + "]");
    		settings = acceptanceSettingsManager.getSettings(StringUtils.trimToNull(getProjectKey()));
        } else {
            // save new settings
        	logger.info("Saving commit acceptance settings for [" + projectKey + "]");
           	acceptanceSettingsManager.setSettings(StringUtils.trimToNull(getProjectKey()), settings);
        }
        return SUCCESS;
    }

	public String getProjectKey() {
		return projectKey;
	}

	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}

    public boolean getUseGlobalRules() {
    	return settings.getUseGlobalRules();
    }

    public void setUseGlobalRules(boolean useGlobalRules) {
    	settings.setUseGlobalRules(useGlobalRules);
    }

	public boolean isMustHaveIssue() {
		return settings.isMustHaveIssue();
	}

	public void setMustHaveIssue(boolean mustHaveIssue) {
        settings.setMustHaveIssue(mustHaveIssue);
	}

	public boolean isMustBeUnresolved() {
		return settings.isMustBeUnresolved();
	}

	public void setMustBeUnresolved(boolean mustBeUnresolved) {
        settings.setMustBeUnresolved(mustBeUnresolved);
	}

    public boolean isMustBeAssignedToCommiter() {
		return settings.isMustBeAssignedToCommiter();
	}

	public void setMustBeAssignedToCommiter(boolean mustBeAssignedToCommiter) {
        settings.setMustBeAssignedToCommiter(mustBeAssignedToCommiter);
	}

	public int getAcceptIssuesFor() {
		return settings.getAcceptIssuesFor();
	}

	public void setAcceptIssuesFor(int acceptIssuesFor) {
		settings.setAcceptIssuesFor(acceptIssuesFor);
	}

	public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

	public void setMustMatchCustomField(boolean mustMatchCustomField) {
		settings.setMustMatchCustomField(mustMatchCustomField);
	}
	
	public void setCustomFieldName(String customFieldName) {
		settings.setCustomFieldName(customFieldName);
	}
	
	public boolean getMustMatchCustomField() {
		return settings.isMustMatchCustomField();
	}
	
	public String getCustomFieldName() {
		return settings.getCustomFieldName();
	}
	
	/**
	 * Returns the list of all available projects.
	 */
    public List<Project> getProjects() {
    	List<Project> projects = new ArrayList<Project>(projectManager.getProjectObjects());
    	Collections.sort(projects, new Comparator<Project>() {
    		public int compare(Project o1, Project o2) {
    			return o1.getKey().compareTo(o2.getKey());
    		}
    	});
    	return projects;
    }
}
