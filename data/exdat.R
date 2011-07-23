# Small example data set with 5 SNPs on 10 subjects. Also a small
# example marker map.

# Simulated marker data: rows are individuals, columns are SNP genotypes coded
# as 0, 1, 2.
set.seed(42);nsnps<-10;nsubj<-5
afs<-runif(nsnps,min=.1,max=.9)
mm<-matrix(NA,nrow=nsubj,ncol=nsnps) 
for(i in 1:nsnps) {
  mm[,i]<-rbinom(nsubj,2,afs[i])
}
# Put in a few missing values
mm[5,3:4]<-NA
colnames(mm)<-paste("SNP",1:nsnps,sep="")
rownames(mm)<-paste("subject",1:nsubj,sep="")

# Example marker map.
genmap<-(1:nsnps)/2 # evenly-spaced, half cM apart

# Bundle markers and map together and tidy up
exdat<-list(markers=mm,map=genmap)
rm(mm,genmap,i,nsnps,nsubj,afs)


