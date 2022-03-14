from django.urls import path


from users.apis import (
    GetUserInfoApi, 
    FindIDApi, 
    ChangePasswordApi, 
    SendPasswordEmailApi,
    ConfirmPasswordEmailApi, 
    CreateUserApi, 
    DeleteUserApi
)
    

app_name = 'users'


urlpatterns = [
    path('', GetUserInfoApi.as_view(), name="getinfo"),
    path('', CreateUserApi.as_view(), name="createuser"),
    path('', DeleteUserApi.as_view(), name="deleteuse"),
    path('id', FindIDApi.as_view(), name="findid"),
    
    path('password', ChangePasswordApi.as_view(), name="changepw"),
    path('password/code', SendPasswordEmailApi.as_view(), name="sendpw"),
    path('password/verifycode', ConfirmPasswordEmailApi.as_view(), name="confirmpw"),
]
