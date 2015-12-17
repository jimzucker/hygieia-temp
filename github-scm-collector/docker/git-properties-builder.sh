#!/bin/bash

# mongo container provides the HOST/PORT
# api container provided DB Name, ID & PWD
PROP_FILE=hygieia-github-scm-collector.properties
LOG=logs/$PROP_FILE.log


# if we are linked, use that info
if [ "$MONGO_PORT" != "" ]; then
  # Sample: MONGO_PORT=tcp://172.17.0.20:27017
  export SPRING_DATA_MONGODB_HOST=`echo $MONGO_PORT|sed 's;.*://\([^:]*\):\(.*\);\1;'`
  export SPRING_DATA_MONGODB_PORT=`echo $MONGO_PORT|sed 's;.*://\([^:]*\):\(.*\);\2;'`
else
	env >>$LOG
	echo "ERROR: MONGO_PORT not defined" > logs/$0.log
	exit 1
fi

echo "SPRING_DATA_MONGODB_HOST: $SPRING_DATA_MONGODB_HOST"
echo "SPRING_DATA_MONGODB_PORT: $SPRING_DATA_MONGODB_PORT"


cat > $PROP_FILE <<EOF
#Database Name
database=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_DATABASE:-dashboard}

#Database HostName - default is localhost
dbhost=${SPRING_DATA_MONGODB_HOST:-10.0.1.1}

#Database Port - default is 27017
dbport=${SPRING_DATA_MONGODB_PORT:-9999}

#Database Username - default is blank
dbusername=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_USERNAME:-db}

#Database Password - default is blank
dbpassword=${HYGIEIA_API_ENV_SPRING_DATA_MONGODB_PASSWORD:-dbpass}

#Collector schedule (required)
github.cron=0 0/5 * * * *

github.host=github.com

#Maximum number of days to go back in time when fetching commits
github.commitThresholdDays=15

EOF

cp $PROP_FILE application.properties

echo "

===========================================
Properties file created:  $PROP_FILE
`cat $PROP_FILE`
===========================================

 " >>$LOG