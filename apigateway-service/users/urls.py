from django.urls import path


from users.apis import (
    GetDeleteUserInfoApi, 
    FindIDApi, 
    ChangePasswordApi, 
    SendPasswordEmailApi,
    ConfirmPasswordEmailApi, 
    CreateUserApi, 
)
    

app_name = 'users'


urlpatterns = [
    path('', GetDeleteUserInfoApi.as_view(), name="getdeleteinfo"),
    path('/new', CreateUserApi.as_view(), name="createuser"),
    path('/id', FindIDApi.as_view(), name="findid"),
    
    path('/password', ChangePasswordApi.as_view(), name="changepw"),
    path('/password/code', SendPasswordEmailApi.as_view(), name="sendpw"),
    path('/password/verifycode', ConfirmPasswordEmailApi.as_view(), name="confirmpw"),
]
