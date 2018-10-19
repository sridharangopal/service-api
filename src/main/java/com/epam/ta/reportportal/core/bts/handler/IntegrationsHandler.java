package com.epam.ta.reportportal.core.bts.handler;

import com.epam.ta.reportportal.auth.ReportPortalUser;
import com.epam.ta.reportportal.dao.IntegrationRepository;
import com.epam.ta.reportportal.entity.bts.BugTrackingSystem;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.util.ProjectUtils;
import com.epam.ta.reportportal.ws.converter.builders.BugTrackingSystemBuilder;
import com.epam.ta.reportportal.ws.converter.converters.IntegrationConverter;
import com.epam.ta.reportportal.ws.model.EntryCreatedRS;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.epam.ta.reportportal.ws.model.OperationCompletionRS;
import com.epam.ta.reportportal.ws.model.externalsystem.CreateExternalSystemRQ;
import com.epam.ta.reportportal.ws.model.integration.IntegrationResource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static com.epam.ta.reportportal.ws.model.ErrorType.INTEGRATION_NOT_FOUND;
import static com.google.common.base.Predicates.notNull;

/**
 * @author <a href="mailto:andrei_varabyeu@epam.com">Andrei Varabyeu</a>
 */
public class IntegrationsHandler {

	@Autowired
	private IntegrationRepository integrationRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public IntegrationResource getIntegrationByID(Long projectId, Long id) {
		Integration integration = integrationRepository.findByIdAndProjectId(id, projectId)
				.orElseThrow(() -> new ReportPortalException(ErrorType.INTEGRATION_NOT_FOUND, id));
		return IntegrationConverter.TO_INTEGRATION_RESOURCE.apply(integration);
	}

	public synchronized OperationCompletionRS deleteIntegration(Long id, String projectName, ReportPortalUser user) {
		ReportPortalUser.ProjectDetails projectDetails = ProjectUtils.extractProjectDetails(user, projectName);

		Integration integration = integrationRepository.findByIdAndProjectId(id, projectDetails.getProjectId())
				.orElseThrow(() -> new ReportPortalException(INTEGRATION_NOT_FOUND, id));

		integrationRepository.delete(integration);

		//		eventPublisher.publishEvent(new ExternalSystemDeletedEvent(exist, username));
		return new OperationCompletionRS("ExternalSystem with ID = '" + id + "' is successfully deleted.");
	}

	public synchronized OperationCompletionRS deleteProjectIntegrations(String projectName, ReportPortalUser user) {
		ReportPortalUser.ProjectDetails projectDetails = ProjectUtils.extractProjectDetails(user, projectName);
		List<Integration> btsSystems = integrationRepository.findAllByProjectId(projectDetails.getProjectId());
		if (!CollectionUtils.isEmpty(btsSystems)) {
			integrationRepository.deleteAll(btsSystems);
		}
		//eventPublisher.publishEvent(new ProjectExternalSystemsDeletedEvent(exist, projectName, username));
		return new OperationCompletionRS("All ExternalSystems for project '" + projectName + "' successfully removed");
	}

	@Override
	public EntryCreatedRS createExternalSystem(CreateExternalSystemRQ createRQ, String projectName, ReportPortalUser user) {
		ReportPortalUser.ProjectDetails projectDetails = ProjectUtils.extractProjectDetails(user, projectName);

		ExternalSystemStrategy externalSystemStrategy = strategyProvider.getStrategy(createRQ.getExternalSystemType());
		expect(externalSystemStrategy, notNull()).verify(INTEGRATION_NOT_FOUND, createRQ.getExternalSystemType());

		BugTrackingSystem bugTrackingSystem = new BugTrackingSystemBuilder().addUrl(createRQ.getUrl())
				.addBugTrackingSystemType(createRQ.getExternalSystemType())
				.addBugTrackingProject(createRQ.getProject())
				.addProject(projectDetails.getProjectId())
				.get();

		checkUnique(bugTrackingSystem, projectDetails.getProjectId());

		expect(externalSystemStrategy.connectionTest(externalSystem), equalTo(true)).verify(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, projectName);

		integrationRepository.save(bugTrackingSystem);
		//eventPublisher.publishEvent(new IntegrationCreatedEvent(createOne, username));
		return new EntryCreatedRS(bugTrackingSystem.getId());
	}

	//TODO probably could be handled by database
	private void checkUnique(BugTrackingSystem bugTrackingSystem, Long projectId) {
		integrationRepository.findByUrlAndBtsProjectAndProjectId(
				bugTrackingSystem.getUrl(), bugTrackingSystem.getBtsProject(), projectId)
				.ifPresent(it -> new ReportPortalException(ErrorType.INTEGRATION_ALREADY_EXISTS,
						bugTrackingSystem.getUrl() + " & " + bugTrackingSystem.getBtsProject()
				));
	}

}
