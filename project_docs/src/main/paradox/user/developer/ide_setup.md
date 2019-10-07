## IDE Setup

This section will help you get started with the source code on any of the following operating systems.

## Clone the repo
First clone the latest version of the Dev branch from [github](https://github.com/GoingOK/goingok/tree/develop)

### Working with Windows(7,10), Mac and Linux (Ubuntu Desktop) with IDEA Ultimate

@@@ note { title=Notice }
It is recommended that you use an IDE to work on this project as it will provide many helper functions and build tools that make working with sbt and scala and lot easier to manage.
@@@ 

The recommended IDE is Intellij IDEA ULTIMATE because of its Scala support, and relative ease
of use. If you have any suggestions for alternate ways of working with this project please let us know by opening an issue. Otherwise the following is a guide to getting setup with IntelliJ IDEA Ultimate.

1. When you first open IntelliJ IDEA, We first want to ensure you have the correct plugins. Click configure at the bottom of the start screen and choose Plugins.
 Check that you have the 'Scala' and SBT plugins installed. If this is the first time you have run IntelliJ you most likely won't.
 
2. Click Browse Repositories at the bottom and type in Scala to search for it then click install plugin. Do the same for the plugin called SBT.
Below is the plugins that you want to install.
![Scala](https://i.imgur.com/pKYUqRh.png)
![SBT](https://i.imgur.com/8jUHUYf.png)
 
3. Once you have installed both plugins it will direct you to restart IntelliJ. Once restarted select 'Import Project' and navigate to the directory where you cloned the repo too and choose the build.sbt file.

![demo1](https://i.imgur.com/9kZ3sl9.png)

4. Make sure Download library sources is ticked and that your Project JDK is chosen. If not you will need to select your JDK, You will need the 1.8 JRE and JDK [found here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Then click the New Button > JDK and navigate to your java JRE
In most cases it should be installed to `C:\Program Files\Java\jdk1.8.x` and click ok.
![demo2](https://i.imgur.com/nlutg0q.png)

5. Once you click ok IntelliJ may take a while syncing the project(20-40 minutes for first time) Keep an eye on the progress bar at the bottom and wait until it is complete.

6. Once it has synced open the app folder and then open the view folder. Right click on HomePage and select 'Run Play 2 App' this will automatically generate your run configuration. It may also download a number of files that SBT needs which may take some time.
Once it has compiled it will most likely let you know that you are missing some environment variables.
We will get those setup now.

7. Go to the Run Menu at the top and choose 'Edit Configuration', There are a few things we want to change here.
    1. First we want to check that the URL to open is `http://localhost:9000`
    2. Then we want to enter some custom JVM Options `-Xms512M -Xmx6000M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M` replacing whatever is already there.
    3. Lastly we want to enter some Environment Variables click the folder icon then the + symbol and add the following environment variables. You will need to head over to [google] (https://developers.google.com) to get a client ID and secret. ![env vars](https://i.imgur.com/KE39ptQ.png)
    4. Click Apply then ok.
    
8. Great now you can right click HomePage again and select 'Run Play 2 App' and it should compile and load up the homepage. 

9. Now you need to edit your DB in postgress and make yourself an admin if you want to be able to manage the data. For this i recommend using DataGrip which is also a JetBrains product.

10. Open Datagrip and click the + button to add a new data source, select postgresSQL ![datagrip1](https://i.imgur.com/NdGt5OU.png)

11. You will need to enter in your username and password for the postgres db (default user is 'postgres') and you may also need to install the driver which data grip will prompt you to do.

12. once complete click test and ensure it works correctly. then open the user table inside public > tables and view your user. Assuming you have signed in with google correctly there should be one user there. Tick the boxes for supervisor and admin and then commit your changes.
![datagrip2](https://i.imgur.com/y3PgD1G.png)

13. you should be good to go!


**Document Last Updated on 28/03/2019**