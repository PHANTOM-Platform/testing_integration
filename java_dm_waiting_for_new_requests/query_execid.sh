#!/bin/bash
	server="localhost";
	execmanager_port="8700";
	BLUE="\033[0;34m";
	LIGHT_GRAY="\033[0;37m";
	LIGHT_GREEN="\033[1;32m";
	LIGHT_BLUE="\033[1;34m";
	LIGHT_CYAN="\033[1;36m";
	yellow="\033[1;33m";
	WHITE="\033[1;37m";
	RED="\033[0;31m";
	marron="\033[2;33m";
	NO_COLOUR="\033[0m";
	white="\033[0;0m";
	nyellow=$'\E[1;33m';
	cyan=$'\E[36m';
	reset=$'\E[0m';
	BC=$'\e[4m'; #underline
	EC=$'\e[0m'; #not underline

	verify_reponse()
	{
		# $1 server
		# $2 port
		echo "Checking Response on port ${2} ...";
		let "j=0"; 
		HTTP_STATUS=$(curl --silent --output /dev/null --write-out "%{http_code}" http://${1}:${2});
		while [[ ${HTTP_STATUS} != "200" ]] && [ ${j} -lt 1 ] ; do 
			let "j += 1 "; sleep 1; 
			HTTP_STATUS=$(curl --silent --output /dev/null --write-out "%{http_code}" http://${1}:${2});
		done;
		if [[ ${HTTP_STATUS} != "200" ]]; then
			echo "> Server is unreachable on port ${2}. Aborting."
			exit 1;
		fi;
		echo "Done. Response successfully found on port ${2}.";
		echo ;
	}
# 1. ################# CHECK if APP MANAGER server is running ###############
	echo "Checking Exec MANAGER server ...";
	verify_reponse ${server} ${execmanager_port};
# 2. ################## CHECK if Elasticsearch is running ###############
	echo "Checking ElasticSearch ...";
	HTTP_STATUS=$(curl -s http://${server}:${execmanager_port}/verify_es_connection);
	if [[ ${HTTP_STATUS} != "200" ]]; then
		echo "> Not Response from the ElasticSearch Server. Aborting.";
		exit 1;
	fi;
	echo "Done. Response successfully found on ElasticSearch-server address.";
# 6. ################## GET A NEW TOKEN FOR A REGISTERED USER ###################################
	echo -e "\n${LIGHT_BLUE}";
	echo "curl -s -H \"Content-Type: text/plain\" -XGET http://${server}:${execmanager_port}/login?email=\"montana@abc.com\"\&pw=\"new\" --output token.txt\"";
	read -p $'Press [Enter] key to get an authentication \033[1;37mNEW TOKEN\033[1;34m for the example user'; echo -ne "${NO_COLOUR}";
	curl -s -H "Content-Type: text/plain" -XGET http://${server}:${execmanager_port}/login?email="montana@abc.com"\&pw="new" --output token.txt;
# 7. ################## SHOW THE TOKEN IN THE SCREEN ###################################
	echo -e "\n${LIGHT_BLUE}";
	echo "mytoken=\`cat token.txt\`; echo \${mytoken}";
	read -p $'Press [Enter] key to \033[1;37mSEE\033[1;34m the received \033[1;37mTOKEN\033[1;34m'; echo -ne "${NO_COLOUR}";
	mytoken=`cat token.txt;`; echo ${mytoken};
# 8. ################## TEST IF A TOKEN IS VALID OR NOT, this is useful when we not know if the token expired ####### 
	echo -e "\n${LIGHT_BLUE}";
	echo "curl -s -H \"Authorization: OAuth \${mytoken}\" -XGET ${server}:${execmanager_port}/verifytoken";
	read -p $'Press [Enter] key to \033[1;37mCHECK\033[1;34m if the \033[1;37mTOKEN\033[1;34m is valid or not'; echo -ne "${NO_COLOUR}";
	curl -s -H "Authorization: OAuth ${mytoken}" -XGET ${server}:${execmanager_port}/verifytoken;

#####################################################################################	
#10. exec_manager/query_exec exec_id=AWiVZuGkbJ9V_FEcjRGS
echo -e "\n\n query_exec: exec_id=\"${1}\"";
#	curl -XGET 'localhost:9400/manager_db/executions_status/AWiVZuGkbJ9V_FEcjRGS?pretty="true"'
	curl -s  -H "Authorization: OAuth ${mytoken}" -H "Content-Type: text/plain" -XGET http://${server}:${execmanager_port}/query_exec?exec_id="${1}";
