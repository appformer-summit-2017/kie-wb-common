/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.project.backend.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.stunner.backend.service.AbstractDiagramLookupService;
import org.kie.workbench.common.stunner.project.diagram.ProjectDiagram;
import org.kie.workbench.common.stunner.project.diagram.ProjectMetadata;
import org.kie.workbench.common.stunner.project.service.ProjectDiagramLookupService;
import org.kie.workbench.common.stunner.project.service.ProjectDiagramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uberfire.io.IOService;

@ApplicationScoped
@Service
public class ProjectDiagramLookupServiceImpl
        extends AbstractDiagramLookupService<ProjectMetadata, ProjectDiagram>
        implements ProjectDiagramLookupService {

    private static final Logger LOG =
            LoggerFactory.getLogger(ProjectDiagramLookupServiceImpl.class.getName());

    protected ProjectDiagramLookupServiceImpl() {
        this(null,
             null);
    }

    @Inject
    public ProjectDiagramLookupServiceImpl(final @Named("ioStrategy") IOService ioService,
                                           final ProjectDiagramService diagramService) {
        super(ioService,
              diagramService);
    }
}
