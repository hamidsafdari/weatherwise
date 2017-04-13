# README #

### What is this repository for? ###

* Setting up the project
* Running the project
* Working on the code

### Setting up the project ###

* Create an account on [bitbucket](https://bitbucket.org)
* Download and install these tools in the order they appear
    * [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
    * [Git](https://git-scm.com/downloads)
    * A Java IDE (only one of these) 
        * [Netbeans (Java SE edition)](https://netbeans.org/downloads/) (easy to use)
        * [Eclipse](https://www.eclipse.org/downloads/?) (moderately difficult) 
        * [Intellij Idea (Community Edition without JDK)](https://www.jetbrains.com/idea/#chooseYourEdition) (needs a lot of learning))

Once you have Git installed (verify by running `git --version` in cmd/shell), clone this repository by running the following somewhere safe on your disk. You will be asked for a username and password. Use the ones you used to create the bitbucket account.

    `git clone https://hamidsafdari@bitbucket.org/hamidsafdari/weatherwise.git`

Let's say you are in `D:\workspace\java`. Once you clone the project there, there should be a folder created inside `D:\workspace\java` called `weatherwise`. Go into that folder.

Now open your IDE and open the project into it. Click on **Build** (not **Run**). The project's compiled classes should now be in `D:\workspace\java\weatherwise\build\classes`.

### Running the project ###

Assuming you are in `D:\workspace\java\weatherwise`, run the following in the command line:

On Windows

    `java -cp 'lib\jade.jar':'build\classes\main' jade.Boot -gui -agents agent1:sau.hw.ai.agents.UIAgent`

On Mac/Linux

    `java -cp 'lib/jade.jar':'build/classes/main' jade.Boot -gui -agents agent1:sau.hw.ai.agents.UIAgent`

If everything goes without a problem, you should see a new window open and your command line should output `this is agent: agent1`

### Working on the code ###

Do your work on the code and make sure everything is saved. Then on the command line, go to the project directory and do:

    `git add .`
    `git commit -m "ENTER A SHORT MESSAGE HERE DESCRIBING WHAT YOU DID"`
    `git pull origin master`
    
If you don't see any errors, then do:

    `git push -u origin master`
