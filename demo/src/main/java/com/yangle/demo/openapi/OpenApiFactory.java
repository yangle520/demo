package com.yangle.demo.openapi;

public class OpenApiFactory {

    public static OpenApiResponse callOpenApi(String action, String type, OpenApiRequest request) {

        try {
            Class<?> openApiClass = Class.forName("com.yangle.demo.openapi." + type + "." + action);
            BaseOpenApi api = (BaseOpenApi) openApiClass.newInstance();
            return api.doAction(request);
        } catch (ClassNotFoundException e) {
            return OpenApiResponse.buildByClassNotFoundException(e);
        } catch (Exception e) {
            return OpenApiResponse.buildByException(e);
        }
    }
}
