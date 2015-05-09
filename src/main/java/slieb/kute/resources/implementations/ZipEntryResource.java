package slieb.kute.resources.implementations;


import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipEntryResource extends InputStreamResource {


    public ZipEntryResource(ZipFile zipFile, ZipEntry zipEntry) {
        super(zipEntry.getName(), (SupplierWithIO<InputStream>) () -> zipFile.getInputStream(zipEntry));
    }

}
