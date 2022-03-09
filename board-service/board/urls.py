from django.urls import path, include

from board.apis import (
    CategoryCreateReadApi,
    CategoryDetailApi,
    PostCreateReadApi,
    PostDetailApi,
    
)


CategoryCreateReadViewset = CategoryCreateReadApi.as_view({
    'get': 'list',
    'post': 'create'
})

CategoryDetailViewset = CategoryDetailApi.as_view({
    'get': 'retrieve',
    'put': 'update',
    'delete': 'destroy'
})

PostCreateReadViewset = PostCreateReadApi.as_view({
    'get': 'list',
    'post': 'create'
})

PostDetailViewset = PostDetailApi.as_view({
    'get': 'retrieve',
    'post': 'like',
    'put': 'update',
    'delete': 'destroy'
})



urlpatterns = [
    # path('login/', auth.LoginView.as_view(template_name="login.html"), name="login"),
    path('category/<int:cate_id>', CategoryDetailViewset, name="categorydetail"),
    path('category', CategoryCreateReadViewset, name="category"),
    path('category/<int:cate_id>/post', PostCreateReadViewset),
    path('category/<int:cate_id>/post/<int:post_id>', PostDetailViewset),
    
]