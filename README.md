# Board Game Social
CST438-Project3 (Group 1)

## Members:
- Barbara Kondo: [bKondo](https://github.com/bKondo)
- Boris Marin: [BorisMarin8004](https://github.com/BorisMarin8004)
- Chaitanya Parwatkar: [parw8649](https://github.com/parw8649)
- Keyoni McNair: [keyoni](https://github.com/keyoni)

## Tech Stack
Frontend: Android<br>
Backend: Django<br>
Database: Postgres Heroku<br>

## Description:<br>
Board Game Social (working title) will allow users to keep track of their board games and connect with other users who share similar interests. They will be able to share their opinions via a post feed, reviews for games within the app database, and events centered around available games.
<br>
Django tokens will be used to verify valid user activity when sending and receiving data from the Postgres Heroku database.<br>
## ERDs:<br>
![BGS ERDS](https://github.com/parw8649/CST438-Project3/blob/homePost_profile_fragments/docs/Board%20Game%20Social%20ERD.png)

## Initial API Endpoints:<br>
- Login / signup
- Logout
- Get all users
- Edit user
- Get all posts
- Add post
- Get all games
- Get user’s games
- Add game to user’s game collection
- Get all events
- Add event
- Delete user/account

## Mockups:<br>
![BGS mockups](https://github.com/parw8649/CST438-Project3/blob/patch/readme_update/proj03-group01--initial-mockups.png)

## Libraries Used:<br>
- [Retrofit](https://square.github.io/retrofit/#download)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [Picasso](https://square.github.io/picasso/)

## Additional Documentation:<br>

## Routes:
| Route  |  Methods  | Required fields | 
| ------- | ------- | ------- | 
| http://boardgamesocial.herokuapp.com/admin/  |  GET |  - |
| http://boardgamesocial.herokuapp.com/login/  |  POST | username, password |
| http://boardgamesocial.herokuapp.com/sign_up/  |  POST | username, password |
| http://boardgamesocial.herokuapp.com/logout/ | GET | userId|
| http://boardgamesocial.herokuapp.com/api/user/  |  GET, POST, DELETE, PUT | username, password |
| http://boardgamesocial.herokuapp.com/api/event/ | GET, POST, DELETE, PUT | name, hostUserId |
| http://boardgamesocial.herokuapp.com/api/post/ | GET, POST, DELETE, PUT |  userId, postBody, postType |
| http://boardgamesocial.herokuapp.com/api/game/ | GET, POST, DELETE, PUT | gameTitle, genre, minPlayer, maxPlayer |
| http://boardgamesocial.herokuapp.com/api/user_to_user/ | GET, POST, DELETE | userOneId, userTwoId |
| http://boardgamesocial.herokuapp.com/api/message/ | GET, POST, DELETE, PUT | senderId, receiverId, content |
| http://boardgamesocial.herokuapp.com/api/event_to_user/ | GET, POST, DELETE | userId, eventId |
| http://boardgamesocial.herokuapp.com/api/comment/ | GET, POST, DELETE, PUT | userId, postId, content |
| http://boardgamesocial.herokuapp.com/api/game_to_user/ | GET, POST, DELETE, PUT | userId, gameId, private |
| http://boardgamesocial.herokuapp.com/api/hosted_game/ | GET, POST, DELETE, PUT | eventId, gameId, seatsAvailable |
| http://boardgamesocial.herokuapp.com/api/review/ | GET, POST, DELETE, PUT | userId, gameId, content |
| http://boardgamesocial.herokuapp.com/api/hosted_game_to_user/ | GET, POST, DELETE, PUT | userId, gameId |
| http://boardgamesocial.herokuapp.com/api/profile | GET, POST, DELETE|  userId |


## How to use it:

### Authentication:
Backend uses token in order to authenticate requests. Thus for every request the token of the current user must be supplied. It should be in the `header` as:
```
"Authorization": "Token {token}"
```

### Getting, Setting, Deleting data:
Data from the database can be accessed and modified from any of the `http://boardgamesocial.herokuapp.com/api/...` routes

### Body/Query examples:

### login
Request, body:
```
{
    "username": "your-username",
    "password": "your-password"
}
```
Response, body:
```
{
    "token": "token-will-be-here"
}
```
### sign_up
Request, body:
```
{
    "username": "your-username",
    "password": "your-password"
}
```
Response, body:
```
{
    "username": "your-username",
    "password": "your-password"
}
```
### Other API routes
__GET__
This query will filter data in the table and return matches according to field values, they would have to get an exact match for API to return matches, if there is no matches empty array is returned. If there is no query all objects of the given table are returned.
Query(Not in body, use url query parameters):
```
{
    "any-field-name-in-table-entry": "value-to-match",
    ...
}
```
__POST__
This method will allow to to create an entry in DB. Make sure all prerequisites are met before sending a request. For example if there is any FK in DB entry, make sure references by this FK exist. 
Some of the fields are required, refer to the routes table above
Request, body:

__DO NOT MAKE POST WITH PK__
Backend will set pk for you and respond with an object that was created, including pk that was assigned

```
{
    "any-field-name-in-table-entry": "value-to-match",
    ...
}
```
Response, body:
```
{
    data of created object will be here, including id as a pk
}
```
__DELETE__
I recommend to use pk in order to delete specific data, but other fields can also be used. However that can lead to multiple deletions. 
Request, body:
```
{
    "any-field-name-in-table-entry": "value-to-match",
    ...
}
```
Response, body:
```
{
    data of deleted object will be here, including id as a pk
}
```
__PUT__
This endpoint will update the data in DB entry. __PUT__ is not allowed to relationship tables that do not have additional data, refer to routes table on the top. The pk is required.   
Request, body:
```
{
    "id": pk-int-here,
    "any-field-name-in-table-entry": "value-to-match",
    ...
}
```
Response, body:
```
{
    data of updated object will be here, including id as a pk
}
```


## Retrofit Calls:
| Return Type  |  Function Name  | Parameters | Token Protected |
| :-------: | :-------: | :-------: | :-------: |
| Observable\<User> | signUpCall | User | :x: |
| Observable\<Token> | loginCall | User | :x: |
| Observable\<JsonObject> | logoutCall | Map<String, String> | :x: |
| Observable\<JsonArray> | getCall | Class<?>, Map<String, String> | :white_check_mark: |
| Observable\<JsonObject> | postCall | Class<?>, Object | :white_check_mark: |
| Observable\<JsonObject> | putCall | Class<?>, Map<String, String> | :white_check_mark: |
| Observable\<JsonArray> | deleteCall | Class<?>, Map<String, String> | :white_check_mark: |

Call parameters did not change since backend was not changed. The only thing that changed is that now instead of `Call` Retrofit returns `Observable`. Inorder for `Observable` to emit(send request) it needs to have an `Observer`. In order to set `Observer` for `Observable` you can run `subscribe` on `Observable`. In our code it will look like this:
```java
retrofitClient.your_call_here(your_call_params_here).subscribe(your_response_here -> {
            do_something_once_response_received 
        });
```
[Here is a Login example with RxJava](https://github.com/parw8649/CST438-Project3/blob/844ba1ab24385aa1b9c03ef09f01a88de547a86d/Android/app/src/main/java/com/example/boardgamesocial/LoginAndSignUp/Fragments/LoginFragment.java#L76-L89)

### Chain requests
In order to make chain requests or a bit more complex operations than just getting response, you might want to use these [operations](http://reactivex.io/documentation/operators.html)

Here is an example of a chain request for Posts to retrieve private posts from user's friends and all public posts:
```java
RetrofitClient retrofitClient = RetrofitClient.getClient();
Integer userId = 245;
retrofitClient.getCall(UserToUser.class, new HashMap<String, String>(){{
        put("userOneId", String.valueOf(userId));
    }})
    // We use flatMap to make new Observable(request),
    // while consuming emitted data(response) from previous Observable(request)
    .flatMap(jsonArray -> {
        Observable<JsonArray> postsObservable = retrofitClient.getCall(Post.class, new HashMap<String, String>(){{
            put("private", "False");
        }});
        List<UserToUser> usersWithPrivateAccess = getObjectList(jsonArray, UserToUser.class);
        for (UserToUser userWithPrivateAccess : usersWithPrivateAccess) {
            // We use mergeWith in order to merge emitters from Observables(requests) into one Observable,
            // that will emit data from merged Observables(requests) as single emit data(response)
            postsObservable.mergeWith(retrofitClient.getCall(Post.class, new HashMap<String, String>(){{
                put("userId", String.valueOf(userWithPrivateAccess.getUserTwoId()));
                put("private", "True");
            }})).subscribe();
        }
        return postsObservable;
    })
    // We use scan in order to combine all emitted data(responses) into singe object.
    // It is similar to map reduce.
    .scan((cumulativeJsonArray, newJsonArray) -> {
        cumulativeJsonArray.addAll(newJsonArray);
        return cumulativeJsonArray;
    })
    // We subscribe in order to run Observables(send requests), and act on emitted data(response)
    .subscribe(jsonArrayCombined -> Log.i(TAG, String.format("All Posts available for userId 245: %s", jsonArrayCombined)));
```

## MVVM(RecyclerView) implementation

In order to create `RecyclerView`, start with creation of `RecyclerView` instance, like this:
```java
RecyclerView recyclerView = view.findViewById(R.id.your_id_for_recyclerView_in_xml);
recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
```
Then we create a `DataClsAdapter` that will need a `DataClass` that will be used for the list of items in the `RecyclerView`. We also need to pass `ViewHolder` for that `DataClass`, so the android knows how to show it on the screen.
```java
DataClsAdapter<your_DataClass, your_ViewHolder> dataClsAdapter = new DataClsAdapter<>(
                this,
                your_DataClass.class,
                getActivity(),
                R.layout.your_ViewHolder_layout);
recyclerView.setAdapter(dataClsAdapter);
```
Then we will need to get the `DataClsVM` in order to set up mutable data for `RecyclerView`. Once we have an instance of `DataClsVM` we can run `getMutableLiveData` on it and pass it `Observable`(basically getCall) as a parameter. Then `getMediatorLiveData` will return `MediatorLiveData<List<your_data_class_here>>`. We will need to `observe` the changes to the live data and update `RecyclerView` accordingly. 
```java
DataClsVM dataClsVM = DataClsVM.getInstance();
dataClsVM.getMediatorLiveData(RetrofitClient.getClient().getCall(your_DataClass.class, new HashMap<>()), your_DataClass.class)
        .observe(getViewLifecycleOwner(), new_objects_of_your_DataClass -> {
            dataClsAdapter.getObjectList().addAll(new_objects_of_your_DataClass);
            dataClsAdapter.notifyDataSetChanged();
        });
```

[Here is an example with Posts](https://github.com/parw8649/CST438-Project3/blob/844ba1ab24385aa1b9c03ef09f01a88de547a86d/Android/app/src/main/java/com/example/boardgamesocial/MainApp/Fragments/HomePostFragment.java#L79-L95)

### On Click for items in RecyclerView
Any Fragment or Activity that uses RecyclerView needs to implement `DataClsAdapter.OnItemListener`. When implementing `DataClsAdapter.OnItemListener`you will need to override `onItemClick`. This function will define the behaviour once the item of RecyclerView is clicked.

## Tests:
### Backend API
![Screen Shot 2021-10-30 at 20 10 44](https://user-images.githubusercontent.com/56419970/139565541-bb4a83a6-7e12-4b07-83e5-ef48ae2ccbcc.png)
![Screen Shot 2021-10-30 at 20 11 29](https://user-images.githubusercontent.com/56419970/139565557-6619fa7d-afa0-4d50-b79f-7993b5112743.png)
![Screen Shot 2021-10-30 at 20 11 49](https://user-images.githubusercontent.com/56419970/139565565-953a9852-d2db-46ee-98eb-784f70f7ec25.png)
![Screen Shot 2021-10-30 at 20 12 03](https://user-images.githubusercontent.com/56419970/139565574-21aeff21-0f4f-43ee-91d5-aba2ba80c870.png)
![Screen Shot 2021-10-30 at 20 12 48](https://user-images.githubusercontent.com/56419970/139565582-a6de7459-b413-477f-927f-7cb570bdb3ac.png)
![Screen Shot 2021-10-30 at 20 13 06](https://user-images.githubusercontent.com/56419970/139565585-d8f5e9d9-0404-460c-8fcf-4392176a8308.png)
![Screen Shot 2021-10-30 at 20 13 31](https://user-images.githubusercontent.com/56419970/139565589-cb52586d-6e1e-4ae5-9c8a-d6bda44d282a.png)
![Screen Shot 2021-10-30 at 20 13 42](https://user-images.githubusercontent.com/56419970/139565593-2392a2be-d5ee-4b99-a4b5-c956c6678b65.png)
![Screen Shot 2021-10-30 at 20 14 01](https://user-images.githubusercontent.com/56419970/139565597-8aa42817-d70d-4bfc-b46f-6027a696d3ed.png)
![Screen Shot 2021-10-30 at 20 14 18](https://user-images.githubusercontent.com/56419970/139565602-e2e38a40-655a-47df-9259-9eaf85ade940.png)
![Screen Shot 2021-10-30 at 20 14 32](https://user-images.githubusercontent.com/56419970/139565605-1f1c0953-dc1e-4de5-9900-08c0c2a5c699.png)
![Screen Shot 2021-10-30 at 20 14 56](https://user-images.githubusercontent.com/56419970/139565613-37b618b0-e99c-4f0c-83e9-b658e0149339.png)
![Screen Shot 2021-10-30 at 20 15 31](https://user-images.githubusercontent.com/56419970/139565622-84444726-d622-4364-a55d-cf24f7cc84f6.png)

### Each of the DBCalls tests runs test on all 4 respective endpoints
![Screen Shot 2021-11-07 at 16 38 58](https://user-images.githubusercontent.com/56419970/140668484-cd5e3747-29fa-4175-ab0d-6fe93d44bd93.png)
![Screen Shot 2021-11-07 at 16 39 34](https://user-images.githubusercontent.com/56419970/140668485-e299a8b9-587f-4be8-94b5-74382a0098fe.png)
## MVVM
![Screen Shot 2021-11-16 at 17 15 39](https://user-images.githubusercontent.com/56419970/142101377-724793b7-6779-4ccf-a52f-a499a5be5cda.png)