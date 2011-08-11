.onLoad <- function(libname, package) {
  findMem<-function(){
    # This function was used to find out how much memory java can use. 
    # We are not using it now because the messages it generates cause the
    # R package checker to fail
    memList<-c(3584,3072,2560,2048,1536,1024,512)
    for(i in 1:length(memList)) {
     # try starting java with max of memList[i] memory with the system()
     # command. If successful, the first part of the message will be
     # the string "Usage: java [-options] class [args...]". If
     # unsuccessful, java will return some sort of error and R will
     # issue a warning that the sytem command resulted in an error.
     # Break out of the loop the first time we are successful.
     # use suppressWarnings() to hide the warnings from the times
     # we are unsuccessful.
     zz<-file()
     sink(zz)     
     print(suppressWarnings(system(paste("java -Xmx",memList[i],"m",sep=""),intern=TRUE)))
     sink()
     zzz<-readLines(zz, n=1)
     close(zz)
     if (length(grep("Usage: java ", zzz)) == 1)
        return(memList[i])
    }
    warning("Can not start Java virtual machine with at least 512MB memory")
    return(0)
  }
  #mem<-findMem()
  #if(mem>512) { options(java.parameters=paste("-Xmx",mem-512,"m",sep="")) } 
  .jpackage(package, lib.loc = libname)
  library.dynam("rJPSGCS", package)
  methods:::bind_activation(TRUE)
}

.Last.lib <- function(libname, package) {
  methods:::bind_activation(FALSE)
}
