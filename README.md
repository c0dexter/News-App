News App - Project Specification
=====================================

[![N|Solid](http://i.imgur.com/NFM4POKm.png)](http://i.imgur.com/NFM4POKm.png) [![N|Solid](http://i.imgur.com/xxYdsthm.png)](http://i.imgur.com/xxYdsthm.png) [![N|Solid](http://i.imgur.com/PoVAS2Wm.png)](http://i.imgur.com/PoVAS2Wm.png) [![N|Solid](http://i.imgur.com/H9PZJ4am.png)](http://i.imgur.com/H9PZJ4am.png)

----

# YouTube video-clips with short presentations #

### Presentation of application ###
[![Watch the video](http://i.imgur.com/6bOvifHl.png)](https://youtu.be/Ks8cjbaIp6Y)

#Layout
------------


# Main Screen
App contains a main screen which displays multiple news stories



# List Item Contents
- Each list item on the main screen displays relevant text and information about the story.
- Required fields include the title of the article and the name of the section that it belongs to.
- If available, author name and date published should be included. Please note not all responses will contain these pieces of data, but it is required to include them if they are present.


# Layout Best Practices
The code adheres to all of the following best practices:
- Text sizes are defined in sp
- Lengths are defined in dp
- Padding and margin is used appropriately, such that the views are not crammed up against each other.



#Functionality
-------------


# Main Screen Updates
Stories shown on the main screen update properly whenever new news data is fetched from the API.


# Errors
The code runs without errors.


# Story Intents
Clicking on a story opens the story in the user’s browser.


# Api Query
App queries the content.guardianapis.com api to fetch news stories related to the topic chosen by the student, using either the ‘test’ api key or the student’s key.


# Use of Loaders
Networking operations are done using a Loader rather than an AsyncTask.


# External Libraries and Packages
The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries for the core functionality will not be permitted to complete this project.




#Code Readability
-----------------


# Readability
Code is easily readable such that a fellow programmer can understand the purpose of the app.


# Naming conventions
All variables, methods, and resource IDs are descriptively named such that another developer reading the code can easily understand their function.


# Format
The code is properly formatted i.e. there are no unnecessary blank lines; there are no unused variables or methods; there is no commented out code.
The code also has proper indentation when defining variables and methods.
