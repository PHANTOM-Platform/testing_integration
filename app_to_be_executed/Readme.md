# Example of Instrumente app in C

## 1 Compilation

The compilation can be done by running the script:

```bash
bash compile.sh;
```

## 2 Running the application

The application needs to know the address of the Execution Manager and the Monitoring server, 
in addition needs also the authentication token. In order to simplify, these parameters are already in the application code as static strings.

Another required parameter is the ***exec_id*** which has to be provided by the DM. In this preliminar demonstration, the ***exec_id*** is provided manually.

```bash
bash run_pi.sh AWSsuxtcSdlX_Zkd11AX;
```

At the end of the execution, the application uploads the monitoring metrics to the Monitoring server, and also uploads statistics to the Execution Manager, which will trigger the notification to who be subscribed on this completion.

### The authentication token

   It is mandatory to provide a **TOKEN** !!  The token is a text string, its length in the current implementation is about 162 characters.

   In the example is generated a new token from a user id, such "bob@abc.com" and password "1234".

   BUT it is expected that users will provide a token, and NOT provide their id neither their password.

## 3 VIDEO

Video demo avaiblable at  [https://youtu.be/6s6AOytHV6I][video]

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
[video]: https://youtu.be/6s6AOytHV6I
[phantom]: http://www.phantom-project.org
