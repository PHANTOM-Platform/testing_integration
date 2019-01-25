# Example in Java of requesting for notification of new PENDING executions 

## 1 Requesting for notification of new PENDING executions 

The requester must know the address of the servers:

 **Address of the Execution Manager**, for instance "localhost:8700"


 The provided example performs:
 
 1. websocket connection to ***ws://${exec_manager_address}:${execmanager_port}***
 
 2. a subscrition for notifications of ***new*** pending exectutions by sending a message for each subscription like ***{"user":"bob@abc.com","execution_id":"any"}*** to the openned websocket connection. 
 
 

NOTICE: Who suscribes will get ONLY notifications from the server of the NEW updates. If you suscribe later than some updates, then you will not get those corresponding notifications.

WARNING: If the websocket connection is broken, then the notifications will be lost. The subscriber needs to perform a new connection and new subscription.

NOTICE: In case of doubt if a notification is lost, the subscriber can always possible to query the status of any particular execution.

NOTICE: The Application has to be instrumented with the MF-Library in order to report of the completion to the Execution Manager.
 
 

### 2 The notification

The notification is in JSON format, it is in format like the next JSON data structure :

```json
{
	"app": "HPC USE CASE",
	"device": "HLRS Raspberry pi3",
	"execution_id": "1234"
}
```

Notice that it is responsibility of the receiver to parse the JSON data.
Notice that it is still pending to be defined additional fields.

## 3 Running the example

After you update the appropriate paths of the servers in the java file,
you can compile and run it with the script


```bash
bash test_client_dm_ws_suscriber_execmanager.sh;
```

later, run the other script who requests and execution to see the RECEPTION OF notification on new peding exec.


```bash
bash test_client_ws_suscriber_execmanager.sh;
```



### 3.1 VIDEO

Video demo avaiblable at  [https://youtu.be/n7GHXamoIcg][video]

<a href="http://www.youtube.com/watch?feature=player_embedded&v=6s6AOytHV6I
" target="_blank"><img src="http://img.youtube.com/vi/6s6AOytHV6I/0.jpg" 
alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /></a>

 

## Acknowledgment
This project is realized through [PHANTOM][phantom].
The PHANTOM project receives funding under the European Union's Horizon 2020 Research and Innovation Programme under grant agreement number 688146.




## Main Contributors

**Montanana, Jose Miguel, HLRS**
+ [github/jmmontanana](https://github.com/jmmontanana)



## Release History

| Date        | Version | Comment          |
| ----------- | ------- | ---------------- |
| 19-01-2019  | 1.01    | Current version  |

## License
Copyright (C) 2014,2019 University of Stuttgart

[Apache License v2](LICENSE).

[output]: https://github.com/PHANTOM-Platform/testing_integration/blob/master/app_output/exec_stats.json
[video]: https://youtu.be/n7GHXamoIcg
[phantom]: http://www.phantom-project.org
