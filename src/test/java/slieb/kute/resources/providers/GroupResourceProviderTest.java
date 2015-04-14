package slieb.kute.resources.providers;

import org.junit.Test;
import slieb.kute.api.ResourceProvider;
import slieb.kute.resources.implementations.FileResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 4/14/15.
 */
public class GroupResourceProviderTest {


    @Test
    public void createGroupedFileResources() throws Exception {
        List<ResourceProvider<FileResource>> list = new ArrayList<>();
        list.add(new FileResourceProvider(null));
        GroupResourceProvider<FileResource> group = new GroupResourceProvider<>(list);
    }

    @Test
    public void testGetResources() throws Exception {

    }
}