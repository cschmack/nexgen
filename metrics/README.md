
### project assumes you have:

ant 1.8.1 (at least)
java 1.6.25 (at least)

### pull down all latest dependencies and remove any existing version
ant clean-resolve  

### to run unit & story tests
ant test

### to create the distribution jar
ant dist
