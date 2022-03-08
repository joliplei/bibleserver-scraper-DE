# bibleserver-scraper-DE

This tool can package all German bibles from bibleserver.com nicely:
1. as MYBIBLE-Files for [MySword Bible](https://www.mysword.info)
2. as SQLITE-File for [OpenLP Presenter Software](https://openlp.org/)

**Please respect the copyright and don't publish bible texts anywhere without consent of the publishers!**

## Usage

Start the released JAR using Java 11 or above with an (empty) directory as parameter, e.g.

`java -jar bibleserver-scraper-1.0.0-SNAPSHOT.jar PATH/TO/TARGET/DIRECTORY`

It will show you on the standard output what it does. If all goes well, it will create nine German bible modules
(ELB, EU, GNB, HFA, LUT, NEÜ, NLB, SLT, and ZB) for MySword (filetype: .mybible) and OpenLP (.sqlite).

## MySword Bible modules

You have to copy the resulting files to your device, either by mailing them to yourself or via Android Debug Bridge:

`adb push *.mybible /storage/emulated/0/mysword/bibles`

## OpenLP Bible modules

In order to use exported bibles in OpenLP you have to copy SQLITE-Files to your OpenLP-Data-Folder.

On Windows-System this folder can be found at C:\Users\USERNAME\AppData\Roaming\openlp\data\bibles
On Linux-Systems it is /home/<user>/.config/OpenLP/
