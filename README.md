Eclipse Instructions
---

* Open the `File->Import` dialog. Select `Existing Maven Projects` and click `Next`.
* Select the `library` directory as the root folder from the `Select Root Folder` dialog. Click `Open`.
* From the `Package Explorer`, select the `library` project.
* From the context (right-click) menu, select `Run As->JUnit Test`.

You should see 229 passing (green) unit tests.

JetBrains IDEA  Instructions
---

* Start IntelliJ IDEA.
* Select `Import Project`.
* From the dialog `Select File or Directory to Import`, select the `library` directory and click `OK`.
* From the dialog `Import Project`, ensure that the radio button `Import project from external model` is selected, and ensure that `Maven` is selected in the list. Click `Next`.
* From the next very detailed dialog, click `Next`.
* From the next dialog, you should see `com.langrsoft:library:0.0.1-SNAPSHOT` selected in the list of Maven projects to import. Click `Next`.
* From the next dialog, select an SDK version (either 1.7 or 1.8).
* From the next dialog, click `Finish`.
* From the IntelliJ IDEA menu, select `Run->Edit Configurations...`.
* From the `Run/Debug Configurations` dialog, press the `+` button (located in the upper left portion of the dialog) to create a new configuration.
* From the `Add New Configuration` popup, select `JUnit`.
* Change the configuration name to `All Tests`.
* Under the `Configuration` tab, change the `Test Kind` dropdown to be `All in package`.
* From the `Search for tests:` radio group, select `In whole project`.
* Click `OK`.
* From the IntelliJ IDEA menu, select `Run->Run 'All Tests'`.

You should see 229 passing (green) unit tests.
