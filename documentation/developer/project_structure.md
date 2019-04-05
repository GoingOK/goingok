## Project Structure

goingok is a SBT app that uses the Play framework.

The majority of the app is contained inside of the /app folder. There are 3 sub directories.

    /controllers
    /models
    /views

The /controllers folder contains all the logic of the app. There are controllers for each of the main functions of the app.
the /models folder contains the structure of the models the database will use.
The /views folder contains the views that the user will see, this includes the home page, help page and any other components these pages use.

/docs is an auto generated folder which contains the compiled documents by paradox. To edit the documentation you can see the @ref:[How to contribute docs](update_docs.md).