package org.slieb.kute.service.providers;


import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.api.ResourceProvider;

import java.io.IOException;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static slieb.kute.Kute.*;


public class IndexProviderTest {

    @Test
    public void resourceProviderCanOverrideIndexes() throws IOException {
        Resource.Readable resource = stringResource("/index.html", "override content");
        ResourceProvider<Resource.Readable> provider = providerOf(resource);
        IndexProvider index = new IndexProvider(provider);
        assertNotNull(index.getResourceByName("/"));
        assertEquals(readResource(resource), readResource(index.getResourceByName("/").get()));
    }

    @Test
    public void testResourceProviderReturnsAResourceProviderForAllDirectoryLevels() {
        Resource.Readable level1 = stringResource("/script.min.js", "alert('boo');");
        Resource.Readable level2 = stringResource("/css/style.min.css", ".color { red; }");
        Resource.Readable level3 = stringResource("/images/venice/yacht.jpg", "!!!!");
        ResourceProvider<Resource.Readable> provider = providerOf(level1, level2, level3);
        IndexProvider index = new IndexProvider(provider);
        assertEquals(Sets.newHashSet("/", "/css", "/images", "/images/venice"),
                     index.stream().map(Resource::getPath).collect(toSet()));
    }


    private boolean containsLinkTo(Document document, String path) {
        return document.getElementsByTag("a").stream().anyMatch(e -> e.attr("href").equals(path));
    }

    @Test
    public void testIndexProducesNavigatableHtml() throws IOException {
        Resource.Readable level1 = stringResource("/script.min.js", "alert('boo');");
        Resource.Readable level2_style = stringResource("/css/style.min.css", ".color { red; }");
        Resource.Readable level2_ie = stringResource("/css/ie.min.css", ".color { red; }");
        Resource.Readable level2_print = stringResource("/css/print.min.css", ".color { red; }");
        Resource.Readable level3 = stringResource("/images/venice/yacht.jpg", "!!!!");
        ResourceProvider<Resource.Readable> provider = providerOf(level1, level2_style, level2_ie, level2_print,
                                                                  level3);
        IndexProvider index = new IndexProvider(provider);

        Resource.Readable level2Index = index.getResourceByName("/css").get();
        assertNotNull(level2Index);

        Document document = Jsoup.parse(readResource(level2Index));
        assertTrue("Contains a link to parent.", containsLinkTo(document, "/"));
        assertTrue("Contains a link to style.min.css file.", containsLinkTo(document, "/css/style.min.css"));
        assertTrue("Contains a link to ie.min.css file.", containsLinkTo(document, "/css/ie.min.css"));
        assertTrue("Contains a link to print.min.css file.", containsLinkTo(document, "/css/print.min.css"));
        assertFalse("Does not contain a link to script.min.js file.", containsLinkTo(document, "/script.min.js"));
        assertFalse("Does not contain a link to yacht.jpg file.", containsLinkTo(document, "/images/venic/yacht.jpg"));

    }

}