
/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * http://www.carrot2.org/carrot2.LICENSE
 */

package org.carrot2.text.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.carrot2.core.Document;
import org.carrot2.core.LanguageCode;
import org.carrot2.text.linguistic.DefaultLexicalDataFactory;
import org.carrot2.text.linguistic.DefaultStemmerFactory;
import org.carrot2.text.linguistic.DefaultTokenizerFactory;
import org.carrot2.text.linguistic.ILexicalDataFactory;
import org.carrot2.text.linguistic.IStemmerFactory;
import org.carrot2.text.linguistic.ITokenizerFactory;
import org.carrot2.text.linguistic.LanguageModel;
import org.carrot2.util.tests.CarrotTestCase;
import org.junit.Before;

/**
 * Base class for {@link PreprocessingContext} tasks tests.
 */
public class PreprocessingComponentTestBase extends CarrotTestCase
{
    /** Preprocessing context for the component being tested */
    protected PreprocessingContext context;
    protected String query;

    /** Documents each test sets up */
    protected List<Document> documents;

    /** Word image to index mapping */
    protected Map<String, Integer> wordIndices;

    protected LanguageCode language;

    @Before
    public void setUpPreprocessingInfrastructure()
    {
        this.documents = new ArrayList<>();
        this.language = LanguageCode.ENGLISH;
        setupPreprocessingContext(null);
    }

    protected void setupPreprocessingContext(String query)
    {
        this.query = query;
        this.context = createPreprocessingContext();
    }


    private PreprocessingContext createPreprocessingContext()
    {
        return new PreprocessingContext(
            LanguageModel.create(LanguageCode.ENGLISH, createStemmerFactory(),
                createTokenizerFactory(), createLexicalDataFactory()));
    }

    /**
     * Creates the {@link ITokenizerFactory} to be used in tests. This implementation
     * returns a new instance of {@link DefaultTokenizerFactory}, override to use a
     * different factory.
     */
    protected ITokenizerFactory createTokenizerFactory()
    {
        return new DefaultTokenizerFactory();
    }

    /**
     * Creates the {@link IStemmerFactory} to be used in tests. This implementation
     * returns a new instance of {@link DefaultStemmerFactory}, override to use a
     * different factory.
     */
    protected IStemmerFactory createStemmerFactory()
    {
        return new DefaultStemmerFactory();
    }

    /**
     * Creates the {@link ILexicalDataFactory} to be used in tests. This implementation
     * returns a new instance of {@link DefaultLexicalDataFactory}, override to use a
     * different factory.
     */
    protected ILexicalDataFactory createLexicalDataFactory()
    {
        return new DefaultLexicalDataFactory();
    }

    /**
     * A utility method for creating documents for tests. See subclasses for usage
     * examples.
     * 
     * @param fields names of fields to create
     * @param fieldValues values for fields, for each <code>fields.length</code> values,
     *            one document will be created.
     */
    protected void createDocumentsWithFields(String [] fields, String... fieldValues)
    {
        int fieldValuesIndex = 0;
        while (fieldValuesIndex < fieldValues.length)
        {
            Document document = new Document();
            for (String fieldName : fields)
            {
                document.setField(fieldName, fieldValues[fieldValuesIndex++]);

                if (fieldValuesIndex >= fieldValues.length)
                {
                    break;
                }
            }
            documents.add(document);
        }

        Document.assignDocumentIds(documents);
        prepareWordIndices();
    }

    /**
     * Creates documents with {@link #DEFAULT_DOCUMENT_FIELD_NAMES}.
     */
    protected void createDocuments(String... fieldValues)
    {
        createDocumentsWithFields(DEFAULT_DOCUMENT_FIELD_NAMES, fieldValues);
    }

    /**
     * Default field names.
     */
    final static String [] DEFAULT_DOCUMENT_FIELD_NAMES = new String []
    {
        Document.TITLE, Document.SUMMARY
    };

    /**
     * Initializes the map with word image to index.
     */
    protected void prepareWordIndices()
    {
        final Tokenizer temporaryTokenizer = new Tokenizer();
        final CaseNormalizer temporaryCaseNormalizer = new CaseNormalizer();
        final PreprocessingContext temporaryContext = createPreprocessingContext();

        temporaryTokenizer.tokenize(temporaryContext, documents.iterator());
        temporaryCaseNormalizer.normalize(temporaryContext);

        final char [][] images = temporaryContext.allWords.image;
        wordIndices = new HashMap<>();
        for (int i = 0; i < images.length; i++)
        {
            wordIndices.put(new String(images[i]), i);
        }
    }
}
