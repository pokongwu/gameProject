# Group 3 MobAss

## Game Design Document
- Our GDD can be found here:
- https://www.overleaf.com/read/xpggqbzmvrzp

### Quickstart GUIDE
1. Make sure you have activated GPS, mobile data and don't be inside a building
  * If you are testing on a emulator, you can import the KML-file route-mahr-to-klosterbraeu.kml located in the root directory of the git project
  * To do so, open the preferences of the emulator (three dots next to the emulator) and click the 'LOAD GPX/KML' button in the locations tab
2. Start the app GeoRacer and select the desired rounds via the spinner widget
3. Tap the START GAME button
4. Make sure the players position is displayed correctly on the map. If you can't find the position, tap the 'find location'-button in the top right corner
  * If you are using the KML file, just mark the top of the table and click the play-button until the players position is displayed correctly. Then click the stop-button
5. In order to set the desired destination, tap on any position of the map. The routing will start and render as soon as the network call is finished
  * Take note, that you can't reset the destination during the game. If you don't like the destination, you have to restart the game
  * If you use the KML-file, you should select as destination 'Klosterbräu' (https://goo.gl/maps/ULzDdQTcCRTXuXY49)
6. After the routing is finished, you should see waypoints (blue circles). As soon as you enter one, you have to guess the distance to the landmarks (blue markers)
7. Walk towards your destination and guess all the landmarks for every waypoint (one waypoint after another, you have to stick to the order)
8. As soon as you guessed all waypoints, the finsh screen will appear. Congratulations, you have finished our game! :-)
 

### THIRD PARTY DEPENDENCIES
* MAP-View: google maps (API Key located in res/values/google_maps_api.xml)
* Directions Routing: mapquest (API Key located in res/values/mapquest_directions_api.xml)
* JSON parsing: gson v2.8.2 (gradle dependency)