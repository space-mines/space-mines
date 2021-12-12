# Space Mines

Full stack Gradle app that can be deployed to Heroku

## Running Locally

Make sure you have Java installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ git clone https://github.com/space-mines/space-mines.git
$ cd space-mines
$ ./gradlew build
$ heroku local web
```

Your app should now be running on [localhost:5000](http://localhost:5000/).

### API

It utilizes a very GETful API with JSON formatted information on the game.

#### JSON body
```json
{
  "state": "PLAY",
  "size": 4,
  "sectors": [
    {
      "id": 0,
      "marked": false,
      "radiation": -1,
      "x": 0,
      "y": 0,
      "z": 0
    },
    {
      "id": 63,
      "marked": false,
      "radiation": -1,
      "x": 3,
      "y": 3,
      "z": 3
    }
  ]
}
```

#### Endpoints
| method | path                      | description                                            |
|--------|---------------------------|--------------------------------------------------------|
| GET    | `/game`                   | returns a new game                                     |
| GET    | `/game/reveal?sectorId=2` | returns game data after revealing sector with `id=2`   | |
| GET    | `/game/mark?sectorId=3`   | returns game data after marking sector with `id=3`     |

#### Game state

| value  | description                      |
|--------|----------------------------------|
| `PLAY` | active game                      |
| `WIN`  | current game has ended with win  |
| `LOSE` | current game has ended with loss |  

#### Radiation
| value   | description                |
|---------|----------------------------|
| `-1`    | Unknown                    |
| `0`     | No mines are adjacent      |
| `1..24` | 1 to 24 mines are adjacent |
| `25`    | Mine in this sector        |

#### Sector colors
| radiation | color  |
|-----------|--------|
| `-1`      | gray   |
| `0`       | none   |
| `1`       | blue   |
| `2`       | green  |
| `3`       | yellow |
| `4`       | orange |
| `5`       | red    |

## Database NOT required yet

When we use a database, ensure you have a local `.env` file that reads something like this:

```
DATABASE_URL=postgres://localhost:5432/gradle_database_name
```

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku main
$ heroku open
```

## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)