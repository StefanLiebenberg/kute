package org.slieb.kute.providers;

@SuppressWarnings("unused")
public interface ProviderTestInterface {

    void shouldNotProvideDirectoriesInStream() throws Exception;

    void shouldNotProvideDirectoriesInGetByPath() throws Exception;

    void shouldNeverReturnNullOnGetByPath() throws Exception;

    void shouldReturnElementsInStream() throws Exception;

    void shouldReturnPresentOptionalInGetByPath() throws Exception;

    void shouldReturnResourceWithCorrectContentInStream() throws Exception;

    void shouldReturnResourceWithCorrectContentInGetByPath() throws Exception;

    void shouldReturnAllResourcesInStreamInGetByPath() throws Exception;
}
