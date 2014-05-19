package org.kie.workbench.common.services.refactoring.backend.server.query.standard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;

import org.apache.lucene.search.Query;
import org.drools.workbench.models.datamodel.util.PortablePreconditions;
import org.kie.workbench.common.services.refactoring.backend.server.query.NamedQuery;
import org.kie.workbench.common.services.refactoring.backend.server.query.QueryBuilder;
import org.kie.workbench.common.services.refactoring.model.index.terms.IndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.RuleIndexTerm;
import org.kie.workbench.common.services.refactoring.model.index.terms.valueterms.ValueIndexTerm;

@ApplicationScoped
public class FindRulesQuery implements NamedQuery {

    @Override
    public String getName() {
        return "FindRulesQuery";
    }

    @Override
    public Set<IndexTerm> getTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleIndexTerm() );
        }};
    }

    @Override
    public Set<IndexTerm> getResultTerms() {
        return new HashSet<IndexTerm>() {{
            add( new RuleIndexTerm() );
        }};
    }

    @Override
    public Query toQuery( final Set<ValueIndexTerm> terms,
                          final boolean useWildcards ) {
        PortablePreconditions.checkNotNull( "terms",
                                            terms );
        if ( terms.size() != 1 ) {
            throw new IllegalArgumentException( "Required term has not been provided. Require '" + RuleIndexTerm.TERM + "'." );
        }
        final Map<String, ValueIndexTerm> normalizedTerms = normalizeTerms( terms );
        final ValueIndexTerm ruleTerm = normalizedTerms.get( RuleIndexTerm.TERM );
        if ( ruleTerm == null ) {
            throw new IllegalArgumentException( "Required term has not been provided. Require '" + RuleIndexTerm.TERM + "'." );
        }

        final QueryBuilder builder = new QueryBuilder();
        if ( useWildcards ) {
            builder.useWildcards();
        }
        builder.addTerm( ruleTerm );
        return builder.build();
    }

    private Map<String, ValueIndexTerm> normalizeTerms( final Set<ValueIndexTerm> terms ) {
        final Map<String, ValueIndexTerm> normalizedTerms = new HashMap<String, ValueIndexTerm>();
        for ( ValueIndexTerm term : terms ) {
            normalizedTerms.put( term.getTerm(),
                                 term );
        }
        return normalizedTerms;
    }

}