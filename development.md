
Media Server Build Instructions
---

**Gradle Installation**

* Install Gradle: http://gradle.org/installation
* Invoke the task: **gradle wrapper**. This task will download the suitable Gradle wrapper to your project. Read more at: https://gradle.org/docs/current/userguide/gradle_wrapper.html

**IntelliJ Integration**

IntelliJ users can skip the installation and import the gradle project with IntelliJ (choose "Use customizable gradle wrapper"). IntelliJ will automatically download the Gradle wrapper.


**Building The Project**

* In gradle.properties, set the path to Wowza home directory
* Use the following tasks
  * **gradle build** compiles the code, builds artifacts and copy them to Wowza lib directory
  * **gradle prepareRelease** builds the distribution (a zip archive with all needed jars)
  * **gradle release -Dusername=your_git_username -Dpassword=your_git_password** prepares the release and uploads it to github
* If you're using the gradle wrapper use **gradlew** instead of **gradle**
* IntelliJ/Eclipse uses are advised to build from the IDE and not from command line
* Mac and Linux users:
  * The task copyJarsToWowzaLibDir will fail if you don't have permissions to write to Wowza home directory
  
  
 **Client API Update**
 - change KalturaGeneratedAPIClientsJava project and follow the README.md to build.
 - copy the new jar to KalturaWowzaServer/build/libs
 - delete gradle cache under [user home]/.gradle/caches/modules-2/files-2.1/com.kaltura/KalturaClientLib/x.x.x/
 - build media-server

 **Remote Debug Troubleshooting**
 - in MediaServer-RemoteDebug -> Edit configuration and verify following settings "search sources using module's class path: media-server"

 **WowzaStreamingEngine API sources**
 - in File -> Project Structure -> select "Global libraries" and add lib using '+' put the WowzaStreamingEngine lib path.
 - in File -> Project Structure -> select "Libraries" and add lib using '+' put the WowzaStreamingEngine lib path.
 example: /Applications/Wowza Streaming Engine 4.6.0/lib


 **Client API Update**
 - change KalturaGeneratedAPIClientsJava project and follow the README.md to build.
 - copy the new jar to KalturaWowzaServer/build/tmp/artifacts/
 - delete gradle cache under [user home]/.gradle/caches/modules-2/files-2.1/com.kaltura/KalturaClientLib/x.x.x/
 - build media-server
 - test new api jar locally, update gradle.build:
 - under maven repository set the local path:
 example:
  repositories {
         mavenCentral()
         maven {
             url uri('/Users/john.jordan/repositories/KalturaGeneratedAPIClientsJava/maven')
         }
     }
 - in addition update the jar version in build.gradle's dependencies section


 **Remote Debug Troubleshooting**
 - in MediaServer-RemoteDebug -> Edit configuration and verify following settings "search sources using module's class path: media-server"

 **WowzaStreamingEngine API sources**
 - in File -> Project Structure -> select "Global libraries" and add lib using '+' put the WowzaStreamingEngine lib path.
 example: /Applications/Wowza Streaming Engine 4.6.0/lib
