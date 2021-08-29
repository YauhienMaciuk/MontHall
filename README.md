
All descriptions of APIs are available by http://localhost:8080/monty-hall-swagger-ui/index.html

The terms of playing the MontyHall game:
1) create game (
        http method: POST,
        url: http://localhost:8080/games,
        request body:
    
            {
                "numberOfBoxes": "3"
            }
    
    );
2) pick a box (
        http method: PUT,
        url: http://localhost:8080/games/{gameId}/boxes/{boxIdWhichYouPick}
    );
3) you can find statistic about probability of winning whether you decide to change the choice of 
    the box or stick to the previously selected (
        http method: GET,
        url: http://localhost:8080/statistics?numberOfBoxes=3&numberOfGames=1000000;
    );
    
    or create new statistic (
        http method: POST,
        url: http://localhost:8080/statistics,
        request body:
        
            {
                "numberOfBoxes": "3",
                "numberOfGames": "1000000"
            }
            
    );
4) create a game result (
        http method: POST,
        url: http://localhost:8080/games/{gameId}/game-results,
        request body if you want to change the original choice:
    
            {
                "changePickedBox": true
            }
    
        request body if you want to stick to origin choice:
    
            {
                "changePickedBox": false
            }
);