/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/service-api
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.ta.reportportal.core.configs;

import com.epam.ta.reportportal.core.widget.content.BuildFilterStrategy;
import com.epam.ta.reportportal.core.widget.content.LoadContentStrategy;
import com.epam.ta.reportportal.core.widget.content.filter.GeneralStatisticsFilterStrategy;
import com.epam.ta.reportportal.core.widget.content.filter.ProjectFilterStrategy;
import com.epam.ta.reportportal.core.widget.content.loader.*;
import com.epam.ta.reportportal.core.widget.content.loader.ProductStatusContentLoader;
import com.epam.ta.reportportal.core.widget.content.loader.ProductStatusFilterGroupedContentLoader;
import com.epam.ta.reportportal.core.widget.content.loader.ProductStatusLaunchGroupedContentLoader;
import com.epam.ta.reportportal.core.widget.content.loader.util.ProductStatusContentLoaderManager;
import com.epam.ta.reportportal.entity.widget.WidgetType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration related to widgets.
 *
 * @author Pavel_Bortnik
 */

@Configuration
public class WidgetConfig implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Bean("contentLoader")
	public Map<WidgetType, LoadContentStrategy> contentLoadingMapping() {
		Map<WidgetType, LoadContentStrategy> mapping = new HashMap<>();
		mapping.put(WidgetType.FLAKY_TEST_CASES, applicationContext.getBean(FlakyCasesTableContentLoader.class));
		mapping.put(WidgetType.OVERALL_STATISTICS, applicationContext.getBean(OverallStatisticsContentLoader.class));
		mapping.put(WidgetType.PASSING_RATE_SUMMARY, applicationContext.getBean(PassingRateSummaryContentLoader.class));
		mapping.put(WidgetType.OLD_LINE_CHART, applicationContext.getBean(LineChartContentLoader.class));
		mapping.put(WidgetType.INVESTIGATED_TREND, applicationContext.getBean(ChartInvestigatedContentLoader.class));
		mapping.put(WidgetType.STATISTIC_TREND, applicationContext.getBean(LineChartContentLoader.class));
		mapping.put(WidgetType.LAUNCH_STATISTICS, applicationContext.getBean(LaunchStatisticsChartContentLoader.class));
		mapping.put(WidgetType.CASES_TREND, applicationContext.getBean(CasesTrendContentLoader.class));
		mapping.put(WidgetType.NOT_PASSED, applicationContext.getBean(NotPassedTestsContentLoader.class));
		mapping.put(WidgetType.UNIQUE_BUG_TABLE, applicationContext.getBean(UniqueBugContentLoader.class));
		mapping.put(WidgetType.BUG_TREND, applicationContext.getBean(BugTrendChartContentLoader.class));
		mapping.put(WidgetType.ACTIVITY, applicationContext.getBean(ActivityContentLoader.class));
		mapping.put(WidgetType.LAUNCHES_COMPARISON_CHART, applicationContext.getBean(LaunchesComparisonContentLoader.class));
		mapping.put(WidgetType.LAUNCHES_DURATION_CHART, applicationContext.getBean(LaunchesDurationContentLoader.class));
		mapping.put(WidgetType.LAUNCHES_TABLE, applicationContext.getBean(LaunchesTableContentLoader.class));
		mapping.put(WidgetType.MOST_FAILED_TEST_CASES, applicationContext.getBean(MostFailedContentLoader.class));
		mapping.put(WidgetType.PASSING_RATE_PER_LAUNCH, applicationContext.getBean(PassedRatePerLaunchContentLoader.class));
		mapping.put(WidgetType.PRODUCT_STATUS, applicationContext.getBean(ProductStatusContentLoaderManager.class));
		mapping.put(WidgetType.CUMULATIVE, applicationContext.getBean(CumulativeTrendChartLoader.class));
		return mapping;
	}

	@Bean("buildFilterStrategy")
	public Map<WidgetType, BuildFilterStrategy> buildFilterStrategyMapping() {
		Map<WidgetType, BuildFilterStrategy> mapping = new HashMap<>();
		mapping.put(WidgetType.OLD_LINE_CHART, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.INVESTIGATED_TREND, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.STATISTIC_TREND, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.LAUNCH_STATISTICS, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.OVERALL_STATISTICS, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.CASES_TREND, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.NOT_PASSED, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.BUG_TREND, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.LAUNCHES_TABLE, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.PASSING_RATE_SUMMARY, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.CUMULATIVE, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.PRODUCT_STATUS, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.UNIQUE_BUG_TABLE, applicationContext.getBean(ProjectFilterStrategy.class));
		mapping.put(WidgetType.ACTIVITY, applicationContext.getBean(ProjectFilterStrategy.class));
		mapping.put(WidgetType.LAUNCHES_COMPARISON_CHART, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.LAUNCHES_DURATION_CHART, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.MOST_FAILED_TEST_CASES, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.PASSING_RATE_PER_LAUNCH, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		mapping.put(WidgetType.FLAKY_TEST_CASES, applicationContext.getBean(GeneralStatisticsFilterStrategy.class));
		return mapping;
	}

	@Bean("productStatusContentLoader")
	public Map<String, ProductStatusContentLoader> productStatusContentLoaderMapping() {
		Map<String, ProductStatusContentLoader> mapping = new HashMap<>();
		mapping.put("launch", applicationContext.getBean(ProductStatusLaunchGroupedContentLoader.class));
		mapping.put("filter", applicationContext.getBean(ProductStatusFilterGroupedContentLoader.class));
		return mapping;
	}

	//		@Bean("groupingStrategy")
	//		public Map<InfoInterval, ProjectInfoGroup> groupMapping() {
	//			Map<InfoInterval, ProjectInfoGroup> mapping = new HashMap<>();
	//			mapping.put(InfoInterval.ONE_MONTH, ProjectInfoGroup.BY_DAY);
	//			mapping.put(InfoInterval.THREE_MONTHS, ProjectInfoGroup.BY_WEEK);
	//			mapping.put(InfoInterval.SIX_MONTHS, ProjectInfoGroup.BY_WEEK);
	//			return mapping;
	//		}

}
