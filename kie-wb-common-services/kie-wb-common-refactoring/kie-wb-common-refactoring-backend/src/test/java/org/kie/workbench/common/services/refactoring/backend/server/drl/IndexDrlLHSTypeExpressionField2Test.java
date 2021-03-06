/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.services.refactoring.backend.server.drl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.junit.Test;
import org.kie.workbench.common.services.refactoring.backend.server.BaseIndexingTest;
import org.kie.workbench.common.services.refactoring.backend.server.TestIndexer;
import org.kie.workbench.common.services.refactoring.backend.server.query.builder.SingleTermQueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValuePartReferenceIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueReferenceIndexTerm;
import org.kie.workbench.common.services.refactoring.service.PartType;
import org.kie.workbench.common.services.refactoring.service.ResourceType;
import org.uberfire.ext.metadata.engine.Index;
import org.uberfire.java.nio.file.Path;

public class IndexDrlLHSTypeExpressionField2Test extends BaseIndexingTest<TestDrlFileTypeDefinition> {

    @Test
    public void testIndexDrlLHSTypeExpressionField2() throws IOException, InterruptedException {
        //Add test files
        final Path path1 = basePath.resolve( "drl4.drl" );
        final String drl1 = loadText( "drl4.drl" );
        ioService().write( path1,
                           drl1 );

        Thread.sleep( 5000 ); //wait for events to be consumed from jgit -> (notify changes -> watcher -> index) -> lucene index

        final Index index = getConfig().getIndexManager().get( org.uberfire.ext.metadata.io.KObjectUtil.toKCluster( basePath.getFileSystem() ) );

        {
            final Query query = new SingleTermQueryBuilder( new ValueReferenceIndexTerm( "org.kie.workbench.common.services.refactoring.backend.server.drl.classes.Applicant", ResourceType.JAVA ) ).build();
            searchFor(index, query, 1, path1 );
        }

        {
            final Query query = new SingleTermQueryBuilder( new ValueReferenceIndexTerm( "org.kie.workbench.common.services.refactoring.backend.server.drl.classes.Mortgage", ResourceType.JAVA ) ).build();
            searchFor(index, query, 1, path1 );
        }

        {
            final Query query = new SingleTermQueryBuilder( new ValuePartReferenceIndexTerm( "org.kie.workbench.common.services.refactoring.backend.server.drl.classes.Mortgage", "applicant", PartType.FIELD ) ).build();
            searchFor(index, query, 1, path1 );
        }

        {
            final Query query = new SingleTermQueryBuilder( new ValuePartReferenceIndexTerm( "org.kie.workbench.common.services.refactoring.backend.server.drl.classes.Applicant", "age", PartType.FIELD ) ).build();
            searchFor(index, query, 1, path1 );
        }

    }

    @Override
    protected TestIndexer getIndexer() {
        return new TestDrlFileIndexer();
    }

    @Override
    public Map<String, Analyzer> getAnalyzers() {
        return Collections.<String, Analyzer>emptyMap();
    }

    @Override
    protected TestDrlFileTypeDefinition getResourceTypeDefinition() {
        return new TestDrlFileTypeDefinition();
    }

    @Override
    protected String getRepositoryName() {
        return this.getClass().getSimpleName();
    }

}
