# KBE Anforderungs Dokumentation

# Teil 1
- init two users: mmuster, eschuler
- get userid from auth-header (x)
- jwt baerer auth (x)
- init 10 songs
- all following request needs jwt session key in header --> if not: 403 Forbidden (x)
## Song Reqests
- GET songs on `/songsWS-sakvis/rest/songs` --> 201 | 
- GET song on `/songsWS-sakvis/rest/song`
- POST add song on `/songsWS-sakvis/rest/songs`
- PUT song update on `/songsWS-sakvis/rest/song`
- DELTE song on `/songsWS-sakvis/rest/song`
## User Reqests
- POST auth on `/songsWS-sakvis/rest/auth`: username, password  --> returns token (x)
- POST auth on `/songsWS-sakvis/rest/auth`: username=null, password  --> returns 404
## SongLists Reqests
- add 
- delete 
- get all for current user 
- get all public lists from foreign user

## Error Handling
- add without name
- delete with wrong user
- ...

# Teil 2
- create database structure required for playlists
- init playlist 
   - 1, public -> eschuler
   - 2, privat -> eschuler 
   - 3, public -> mmuster 
   - 4, privat -> mmuster
- create route `/songsWS-sakvis/rest/songLists/`
- create models (x)
- create repo (x)
- create controller (x)

## songLists

### requirements
- get username from token
- add songlist with id 22
- init songlist for eschuler & mmuster
    - private
    - public

## requests
1.) JSON, XML
- GET `rest/songLists?userId=username`: 
    - if username = getUserIdFromToken(auth header) -> all songslists from username; 
    - else --> all public songlists from username; 
    - or (404) Not Found
- GET `rest/songLists/{songlist_id}`: 
    - if getSongList(id).owner_id = getUserIdFromToken(auth header) -> songlist; 
    - else if songlist.public --> songlist; 
    - else if songlist.privat --> (403) Forbidden; 
    - or (404) Not Found

2.) JSON
- POST add songLists `/rest/songLists` with payload:
- in reallfe this would be bad practice (only id of song needed for adding) 
- songs had to be in db --> 201 OK; 
    - if one of them not existing --> 400 Bad Request 
```json
{
 "isPrivate": true,
 "name": "MaximesPrivate",
 "songList": [
    {
       "id": 5,
       "title": "We Built This City",
       "artist": "Starship",
       "label": "Grunt/RCA",
       "released": 1985
    },
    {
       "id": 4,
       "title": "Sussudio",
       "artist": "Phil Collins",
       "label": "Virgin",
       "released": 1985
    }
 ]
 }

```

3.) DELETE

- user can only delete one songs
- user can't delete public playlist of other users --> returns 403 Forbidden


## Tests

- deploy test db
- check status of controller songsList




# Deploy

- deploy on online database heroku
- deploy app (service) on heroku

## Heroku DB

| url      | heroku |
|----------|--------|
| username | abc    |
| password | 1123   | 


## Heroku Tomcat