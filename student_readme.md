# Student README   
## Some new features in addition to the requirements:  
1. **NEW CLIENT COMMAND-LINE OPTIONS:**  
  a. **use the `--raw` flag** with parameter `y` or `yes` to make a raw request to the server.  
   b. **use the `--save` flag** followed by the filename of your choice to save the response to the disk.  
2. **NEW CLIENT BEHAVIORS:**  
  a. Client adds a leading slash if the user does not supply one (i.e. `testing/testing/123` becomes `/testing/testing/123`)  
3. **NEW SERVER BEHAVIORS:**  
  a. When the server receives requests ending in .txt, it will attempt to send the file by that name from the `resources/server` directory. If not found, it will respond with `File Not Found`.  
  
## Reflection  
  
This is _your_ README. Use this space to share how you approached the problems of this project. This need not be a diary, but should contain information about decisions you made, challenges you faced, and your understanding of the content of the project.     
    
- What challenges did you face while completing the project? How did you address them?  
   - **I faced several challenges throughout the project, namely that of accounting for backslashed spaces. At first, I was not sure how to address this issue because of my use of the** `split` **method (splitting by spaces) on the request string. However, a bit of googling led me to the Java documentation, where I found out that the method has the option for a second argument to limit the number of times the** `String` **is split. I was able to solve most of my issues through use of programming forums (e.g. Stack Overflow answers credited in my code) and the Java docs.**  
- How did you test your code? Explicitly describe examples of your test cases.  
   - **I tested my code in a number of ways. I mainly tested different requests via the command line to check that my code worked. I went through the description and requirements of the protocol in the README, and from that I created specific requests that tested each facet of the protocol. (This was especially easy to do since I added an option to make a raw requests with the --raw argument.) For example, the README says that requests must start with** `GET` **and have a request after that separated by exactly one space. Thus, I sent it some requests that did not start with** `GET` **(something else instead of that or nothing at all) (EXAMPLE: `JFKCN /test`) and some requests that had multiple spaces between the** `GET` **and the rest of the request (EXAMPLE: `GET  /test`). To test the rule of escaped spaces (backslash before space), I ran several tests with and without backslashes (randomly in some cases). Aside from these tests, I ran general tests to make sure that the program did not produce errors. When it did, I made sure to capture them as exceptions and return them as invalid.**  
- (Bonus 2 pts.) Provide suggestions of how you would improve the documentation, sample code, testing, or other aspects of the project (up to 2 points extra credit available for noteworthy suggestions here, e.g., actual descriptions of how you would change things, sample code, code for tests, etc.) You will not receive bonus points for simply reporting an issue - you should provide actionable suggestions on how to improve things
	1. **Several places in `README.md`, namely in the description of the protocol, talked about the correct usage of backslashes (e.g. no consecutive backslashes). However, this should have said *forward* slashes. I corrected these errors in the main `README` file.**
	2. **I did not have time to do this, but I had another idea for the client requirements. It might make the process easier for the end user if the client has an option to automatically escape the spaces in their request. Perhaps they could supply an argument like `--autocorrect` and the client would do something like this:** `requestPath = requestPath.replace(" ", "\\ ");` **That way, if users wanted, the client could save them the trouble of escaping each space with a backslash.**

*By the way, I formatted this `README` with the help of [StackEdit](https://stackedit.io/app). I know some basic markup rules but StackEdit was a big help.*