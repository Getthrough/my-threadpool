# my-threadpool
This repo is a simple implementation of thread pool which used java.util.concurrent.LinkedBlockingQueue to store tasks and workers.

It took me almost a day, and it is still kinda a simple implementation.

For example, there's only one interface and one implementation class. And only two optional arguments which are corePoolSize and maxPoolSize, 
I've tried to add something like keepAliveTime, bu I can't figure out what to do in shutdown() method, cause if I can't keep threads alive
for a while, there is nearly no difference with programming without threadpool.

I tried to use BlockingQueue.poll(timeout, timeunit) to keep threads alive, but when I call shutdown(), how to shutdown a thread 
immeditately? Maybe Thread.interrupt() is useful, but it didn't work for me, well...maybe I did something wrong somewhere.

Oh god, it's not really a README, it's actually a HELPME!


