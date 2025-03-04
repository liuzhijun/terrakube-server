package org.azbuilder.registry.plugin.storage.azure;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.azbuilder.registry.plugin.storage.StorageService;
import org.azbuilder.registry.service.git.GitService;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.IOException;

@Slf4j
@Builder
public class AzureStorageServiceImpl implements StorageService {

    private static final String CONTAINER_NAME = "registry";

    @NonNull
    BlobServiceClient blobServiceClient;

    @NonNull
    GitService gitService;

    @Override
    public String searchModule(String organizationName, String moduleName, String providerName, String moduleVersion, String source, String vcsType, String accessToken) {

        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

        log.info("blobContainerClient.exists {}", blobContainerClient.exists());
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
        String blobName = String.format("%s/%s/%s/%s/module.zip", organizationName, moduleName, providerName, moduleVersion);
        log.info("blobName: {}", blobName);
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            File gitCloneDirectory = gitService.getCloneRepositoryByTag(source, moduleVersion, vcsType, accessToken);
            File moduleZip = new File(gitCloneDirectory.getAbsolutePath() + ".zip");
            ZipUtil.pack(gitCloneDirectory, moduleZip);
            blobClient.uploadFromFile(moduleZip.getAbsolutePath());

            try {
                FileUtils.cleanDirectory(gitCloneDirectory);
                if (moduleZip.delete()) log.info("Successfully delete folder");
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        return blobClient.getBlobUrl();
    }
}
