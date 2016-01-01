package slieb.kute.providers;

import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import slieb.kute.api.Resource;
import slieb.kute.KuteIO;
import slieb.kute.KuteLambdas;
import slieb.kute.api.ResourcePredicate;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class FilteredResourceProviderTest implements ProviderTestInterface {

    private FilteredResourceProvider provider;

    private ResourcePredicate filter;

    private ConcurrentMapResourceProvider memoryProvider;

    @Before
    public void setUp() throws Exception {
        memoryProvider = new ConcurrentMapResourceProvider();
        filter = new PassFilter("pass");
        provider = new FilteredResourceProvider(memoryProvider, filter);
        createContent("/directory/pass/index.html", "index content");
        createContent("/directory/fail/index.html", "other content");
    }

    private void createContent(String path, String content) throws Exception {
        KuteIO.writeResource(memoryProvider.create(path), content);
    }

    @Override
    @Test
    public void shouldNotProvideDirectoriesInStream() throws Exception {
        Assert.assertFalse(provider.stream().anyMatch(r -> r.getPath().equals("/directory")));
        Assert.assertTrue(provider.stream().anyMatch(r -> r.getPath().equals("/directory/pass/index.html")));
        Assert.assertEquals(1, provider.stream().count());

    }


    @Override
    @Test
    public void shouldNotProvideDirectoriesInGetByPath() throws Exception {
        Assert.assertFalse(provider.getResourceByName("/directory").isPresent());
        Assert.assertFalse(provider.getResourceByName("/directory/pass").isPresent());
    }

    @Override

    @Test
    public void shouldNeverReturnNullOnGetByPath() throws Exception {
        Assert.assertNotNull(provider.getResourceByName("/does"));
        Assert.assertNotNull(provider.getResourceByName("/does/not"));
        Assert.assertNotNull(provider.getResourceByName("/does/not exist!"));
    }

    @Override

    @Test
    public void shouldReturnElementsInStream() throws Exception {
        Assert.assertEquals(1, provider.stream().count());
    }

    @Override

    @Test
    public void shouldReturnPresentOptionalInGetByPath() throws Exception {
        Assert.assertTrue(provider.getResourceByName("/directory/pass/index.html").isPresent());
    }

    @Override

    @Test
    public void shouldReturnResourceWithCorrectContentInStream() throws Exception {
        Assert.assertEquals(
                Sets.newHashSet("index content"),
                provider.stream().map(KuteLambdas.unsafeMap(KuteIO::readResource)).collect(Collectors.toSet()));
    }

    @Override

    @Test
    public void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception {
        Assert.assertEquals(
                "index content",
                KuteIO.readResource(provider.getResourceByName("/directory/pass/index.html").get())
        );
    }

    @Override
    @Test
    public void shouldReturnAllResourcesInStreamInGetByPath() throws Exception {
        Assert.assertTrue(provider.stream()
                .map(Resource::getPath)
                .map(provider::getResourceByName)
                .allMatch(Optional::isPresent));
    }

    @Override
    @Test
    public void shouldBeSerializable() throws Exception {
        Resource.Provider loaded = KuteIO.deserialize(KuteIO.serialize(provider), Resource.Provider.class);
        Assert.assertEquals(provider.toString(), loaded.toString());
        Assert.assertEquals(provider.hashCode(), loaded.hashCode());
        Assert.assertEquals(provider, loaded);
    }
}

class PassFilter implements ResourcePredicate<Resource> {

    private final String value;

    public PassFilter(String value) {
        this.value = value;
    }

    @Override
    public boolean test(Resource resource) {
        return resource.getPath().contains(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PassFilter)) return false;
        PassFilter that = (PassFilter) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PassFilter{" +
                "value='" + value + '\'' +
                '}';
    }
}
