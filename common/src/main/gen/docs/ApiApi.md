# ApiApi

All URIs are relative to *http://localhost:19099*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createHub**](ApiApi.md#createHub) | **POST** /api/hubs |  |


<a name="createHub"></a>
# **createHub**
> ApiHubs1069940362 createHub(authorization, apiHubs275928953)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ApiApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost:19099");

    ApiApi apiInstance = new ApiApi(defaultClient);
    String authorization = "Bearer {AccessToken}"; // String | AccessToken
    ApiHubs275928953 apiHubs275928953 = new ApiHubs275928953(); // ApiHubs275928953 | 
    try {
      ApiHubs1069940362 result = apiInstance.createHub(authorization, apiHubs275928953);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ApiApi#createHub");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **authorization** | **String**| AccessToken | |
| **apiHubs275928953** | [**ApiHubs275928953**](ApiHubs275928953.md)|  | [optional] |

### Return type

[**ApiHubs1069940362**](ApiHubs1069940362.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | 200 |  -  |

