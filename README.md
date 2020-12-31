# FileSplit Utility

Author : Muhammad Ashraf Ishak

Simple Java-based tool for splitting large files and preserving the containing folder structure. 


## Feature

For example, you may have large files in MyInput folder with the following folder structure:

``` 
MyInput
    \Folder1
        \SubFolder1.1
            largefile1.log
            largefile2.log
        \SubFolder1.2
            largefile3.log
    \Folder2
        \SubFolder2.1
            \SubFolder2.1.1
                largefile4.log
```  
 
Then, you specify MyOutput folder as output folder with split size = 100MB. FileSplit tool would perform the following:
1. If file size is larger than split size (100 MB), the tool split the file.
2. Otherwise, the tool would copy the file into its respective output folder.

The resulted folder and file structure would look something like this:

``` 
MyOutput
    \Folder1
        \SubFolder1.1
            largefile1.log_1
            largefile1.log_2
            ...
            ...
            largefile2.log_1
            largefile2.log_2
            ...
            ...
        \SubFolder1.2
            largefile3.log_1
            largefile3.log_2
            ...
            ...
    \Folder2
        \SubFolder2.1
            \SubFolder2.1.1
                largefile4.log_1
                ...
                ...

```

Finally, you can perform further analysis on the files in the output folder (either using grep etc).

## Usage 

1. Make sure you have Java 1.8 and above installed in your machine. 
2. Run the jar file in command prompt / terminal :

``` 
java -jar FileSplit.jar
```

Then, specify the following items:
1. Full path of input folder
2. Full path of output folder 
3. Split size (in MB)

