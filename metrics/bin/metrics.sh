#!/bin/bash

# metrics
# OPTIONS:
#   -f: start in foreground
#   -p <filename>: log the pid to a file (useful to kill it later)



# Use JAVA_HOME if set, otherwise look for java in PATH
if [ -x $JAVA_HOME/bin/java ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA=`which java`
fi

if [ "$JVM_OPTS" == "" ]; then
    JVM_OPTS="-server -XX:+UseParallelOldGC -XX:+HeapDumpOnOutOfMemoryError -Xmx2000m -Xms1000m"
fi

#### optional high-memory use configuration from neuxfr
# remove the config above to use this one.
if [ "$JVM_OPTS" == "" ]; then
    system_memory_in_mb=`free -m | awk '/Mem:/ {print $2}'`
    LT_2G=$((((system_memory_in_mb / 2)) < 2000))
    # take 65% of the memory
    RAW_HEAP_SIZE=`echo $system_memory_in_mb"* .65"|bc -l|sed s/\\\\..*//g`
    MAX_HEAP_SIZE=$RAW_HEAP_SIZE"M"
    # this could be reduced to something like: min(100M * # cores, max_heap * .24)
    NEW_HEAP_SIZE=`echo $RAW_HEAP_SIZE"*.25"|bc -l|sed s/\\\\..*//g`M
    # make the mx & ms the same to avoid pauses
    JVM_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -Xmx$MAX_HEAP_SIZE -Xms$MAX_HEAP_SIZE -Xmn$NEW_HEAP_SIZE"

    ## parallel gc JVM_OPTS="$JVM_OPTS -XX:+UseParallelGC"
    JVM_OPTS="$JVM_OPTS -XX:+UseParNewGC"
    JVM_OPTS="$JVM_OPTS -XX:+UseConcMarkSweepGC"
    JVM_OPTS="$JVM_OPTS -XX:+CMSParallelRemarkEnabled"
    JVM_OPTS="$JVM_OPTS -XX:SurvivorRatio=8"
    JVM_OPTS="$JVM_OPTS -XX:MaxTenuringThreshold=31"
    JVM_OPTS="$JVM_OPTS -XX:+UseCompressedStrings -XX:+UseStringCache"
fi


if [ "$APP_HOME" == "" ]; then
    APP_HOME="."
fi

JVM_OPTS="$JVM_OPTS -Dcom.sun.management.jmxremote.password.file=$APP_HOME/conf/jmxremote.password"


# The java classpath (required)
CLASSPATH=$APP_HOME/conf:$APP_HOME/bin

for jar in $APP_HOME/lib/*.jar; do
    CLASSPATH=$CLASSPATH:$jar
done


launch_service()
{
    pidpath=$1
    foreground=$2
    props=$3
    class=$4
    #parms="-Dlog4j.configuration=log4j-server.properties"
    #parms="-Dcom.prettyprint.cassandra.load_hector_log4j=false"

    if [ "x$pidpath" != "x" ]; then
        parms="$parms -Dpidfile=$pidpath"
    fi

    # set to root
    cd /
    
    # The foreground option will tell Daemon not
    # to close stdout/stderr, but it's up to us not to background.
    if [ "x$foreground" != "x" ]; then
        parms="$parms -Dforeground=yes"
        exec $JAVA $JVM_OPTS $parms -cp $CLASSPATH $props $class
    # Startup Daemon, background it, and write the pid.
    else
        exec $JAVA $JVM_OPTS $parms -cp $CLASSPATH $props $class <&- &
        [ ! -z $pidpath ] && printf "%d" $! > $pidpath
    fi

    return $?
}

# Parse any command line options.
args=`getopt fahp:bD: "$@"`
eval set -- "$args"

## you probably need to fix this ..
classname="biz.neustar.service.metrics.Daemon"

while true; do
    case "$1" in
        -p)
            pidfile="$2"
            shift 2
        ;;
        -f)
            foreground="yes"
            shift
        ;;
        -h)
            echo "Usage: $0 [-f] [-h] [-p pidfile]"
            exit 0
        ;;
        -D)
            properties="$properties -D$2"
            shift 2
        ;;
        --)
            shift
            break
        ;;
        *)
            echo "Error parsing arguments!" >&2
            exit 1
        ;;
    esac
done

# Start up the service
launch_service "$pidfile" "$foreground" "$properties" "$classname"

exit $?

# vi:ai sw=4 ts=4 tw=0 et
