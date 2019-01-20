# Example in Java of requesting the execution of an APP, and requesting to be notified of the executio


# Input Parameters

1. **Address of the Monitoring Server**, for instance "ocalhost:3033"

2. **Address of the Execution Manager**, for instance "localhost:8700"

3. **Address of the Resource Manager**, for instance  "localhost:8600"

   It is mandatory to provide a **TOKEN** !!

   In the example is generated a new token from a user id, such "bob@abc.com" and password "1234".

   BUT it is expected that users will provide a token, and NOT provide their id neither their password.

4. And information for the Deploy Manager in a JSON file, which at least has to contain:

⋅⋅⋅* **Identification of the application** to be requested, for instance "montanana_demo"

⋅⋅⋅* **Identification of the device** where will be run the application, for instance "node01"


## Requesting for an execuion

The example provide performs an http request to http://${server}:${execmanager_port}/register_new_exec

The **Input Parameters** provided wiht the request are: (1) the autentication token, (2) the JSON file with the information required by the Deploy Manager.

```bash
curl -s -H "Authorization: OAuth ${mytoken}" -H "Content-Type: multipart/form-data" -XPOST -F "UploadJSON=@start_exec.json" http://${server}:${execmanager_port}/register_new_exec;
```

The **Response** is:

* when succedd: a header code "200" and the body response contains the execution_id, such "AWSsuxtcSdlX_Zkd11AX".
* when error: a heade code containing the error code, and the body response contains a description of the error.



## JSON contained the requested information by the Deploy Manager

It is still pending to be defined, the current structure is:

```json
{
	"app": "HPC USE CASE",
	"device": "HLRS Raspberry pi3"
}
```

## Suscription for Notifications

NOTICE: Who suscribes will get ONLY notifications from the server of the NEW updates. If you suscribe later than some updates, then you will not get those corressponding notifications.

WARNING: If the websocket connection is broken, then the notifications will be lost. The suscriber needs to perform a new conection and new suscription.

NOTICE: In case of doubt if a notification is lost, the suscriber can always possible to query the status of any particular execution.


##  

The described JAVA application which requires a particualr execution is in the folder "java_requestor_of_execution"

The application which is instrumented with additional code for monintoring, and reporting to the execution manager is on the folder "app_to_be_executed"




## VIDEO

Video demo avaiblable at  [https://youtu.be/6s6AOytHV6I][video]


 

## Acknowledgment
This project is realized through [PHANTOM][phantom].
The PHANTOM project receives funding under the European Union's Horizon 2020 Research and Innovation Programme under grant agreement number 688146.




## Main Contributors
 
**Montanana, Jose Miguel, HLRS**
+ [github/jmmontanana](https://github.com/jmmontanana)
 


## Release History

| Date        | Version | Comment          |
| ----------- | ------- | ---------------- |
| 19-01-2019  | 1.01     | Current version  |

## License
Copyright (C) 2014,2019 University of Stuttgart

[Apache License v2](LICENSE).
 
[video]: https://youtu.be/6s6AOytHV6I
[phantom]: http://www.phantom-project.org
