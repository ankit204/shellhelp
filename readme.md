# shellhelp

A command-line tool that indexes Linux manual pages for command retrieval.  

## Getting Started
`TODO`

### Prerequisites
`TODO`

### Installing
* Clone the project.
* Place `man1` (manual directory to be indexed) in `data`.
* Run:
`./make`

## Running the tests
Run: `./make test`

Example output:
```
1	copi file local
1 Q0 scp.3 1 9.088690757751465
1 Q0 scp.3 2 9.088690757751465
2	copi file server
2 Q0 ftp.A 1 8.525014877319336
2 Q0 ftp.A 2 8.525014877319336
4	run process background
4 Q0 imptrace.d 1 11.128750801086426
4 Q0 imptrace.d 1 11.128750801086426
5	print current time
5 Q0 date.n 1 8.923357963562012
5 Q0 date.n 2 8.923357963562012
6	chang login password
6 Q0 chpass.s 1 14.052783012390137
6 Q0 chpass.s 2 14.052783012390137
8	remov directori
8 Q0 rmdir.p 1 8.941874504089355
```
### Break down into end to end tests
`TODO`


## Deployment
 `TODO`


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
