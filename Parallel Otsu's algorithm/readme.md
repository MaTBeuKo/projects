 ## Preview
 
Filters the picture based on it's histogram using  [Otsu's method](https://en.wikipedia.org/wiki/Otsu%27s_method) according to the following mapping:

[0; f1) -> 0

[f1; f2) -> 84

[f2; f3) -> 170

[f3; 255] -> 255

 ## Usage

 Works only with .pgm (P5) file format.

 ### Run:

 otsu \<thread num> \<input filename> \<output filename>

  #### thread num:

  - -1 - multithreading disabled
  -  0 - use all available threads
  -  other value - number of threads

