# Description

This is an application that downloads a JSON file from the server by request
```sh
https://www.roxiemobile.ru/careers/test/orders.json
```
then it takes it apart and displays on the screen a list with information about active orders in the taxi service, 
sorted in descending order of date

In each list item should be displayed:

  - Start Adderess
  - End Adress
  - Date of ride
  - Cost of ride

You can also:

Press on each element of the list on main activity to see some additional information that will contain:

  - Car model
  - Car number
  - Driver id information
  - Car photo

This information was also received in JSON format by request
```sh
https://www.roxiemobile.ru/careers/test/images/{imageName}
```
Car photo images will be stored in cache for 10 min

### Development

  - The app supports Android 5.0+
  - Designed in accordance with material Design guide
  - Programming language - Kotlin 1.3+

### TODO

I spent 17 hours developing this app, so here is a lot of things to do

  - Design (Better to use List view at the first screen)
  - 100% this code can be optimized and refactored
  - Some of the algorithms can be put in separate functions

The goal was to make a working application that meets the basic requirements of the test task
