## function that creates parameter file in post-MAKEPED format

write.parfile=function(snp.data,map,file="out.par"){

    #snobj: matrix or data frame of snp data, with rows as subjects and 
    #       columns as snp genotypes, coded 0, 1 and 2
    #map: genetic map locations of each snp
    #file: name of the parameter file to be generated

    if(!is.data.frame(snp.data)) snp.data<-data.frame(snp.data)
    
    MAFs <-  snp.MAFs(snp.data)
    nSNPs=ncol(snp.data)
    
    ##adding lines
    
    #n loci, risk locus, sexlinked, program code
    cat(c(nSNPs,0,0,5), file = file,"\n")
    
    #mutation locus, mutation rates male, female, linkage disequil
    cat(c(0, 0.0, 0.0, 1), file = file,"\n",append=T)
    
    #Marker Order
    cat(1:nSNPs, file = file,"\n",append=T)
    
    #snps freq
    freqmat=cbind(1-MAFs,MAFs)
    
    x <- paste("3 2 ",colnames(snp.data),"\n",
    round(freqmat[,1],6)," ",round(freqmat[,2],6),"\n",
    sep="",collapse="")
    
    cat(x,file=file,append=T)

    #sexdifference, interference
    cat(c(0, 0), file = file,"\n",append=T)
    
    #recombination values
    dcm=rep(0,(nSNPs-1))
    for(i in 1:(nSNPs-1)){
        dcm[i]=abs(map[i]-map[i+1])
    }
    rf=0.5*(1-exp(-2*dcm/100))#Haldane mapping function
    
    cat(rf, file = file,"\n",append=T)
    
    #last line
    cat(c(1, 5, 0.2, 0.1 ), file = file, "\n",append=T)
}


snp.MAFs<-function(snp.data) {
  # assumes rows of snp.data are subjects and columns are SNP genotypes
  # SNP genotypes assumed coded as 0, 1, 2 
  nsubj<-nrow(snp.data)
  nsnps<-ncol(snp.data)
  mafs<-rep(NA,nsnps)
  for(i in 1:nsnps) {
    ss<-snp.data[,i]
    aa<-(1*sum(ss==1) + 2*sum(ss==2))/(2*nsubj)
    mafs[i]<-ifelse(aa<0.5,aa,1-aa)
  }
  return(mafs)
}
