#! /bin/sh
# There is no need to call this if you set the MULE_HOME in your environment
export MULE_HOME=../../..

# Set your application specific classpath like this
export CLASSPATH=$MULE_HOME/samples/errorhandler/conf:$MULE_HOME/samples/errorhandler/classes

exec $MULE_HOME/bin/mule -config ../conf/mule-config.xml

export CLASSPATH=