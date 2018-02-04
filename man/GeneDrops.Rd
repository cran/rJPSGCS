\name{GeneDrops}
\alias{GeneDrops}
\title{
simulate genotypes on a pedigree
}
\description{
This function is an R wrapper for the JPSGCS multi-locus gene-drop
program. 
}
\usage{
GeneDrops(ld.par = "ld.par", ped = "in.ped", n, complete.data=FALSE,
gd.ped = "gd.ped", compress.pedfiles=FALSE)
}
\arguments{
  \item{ld.par}{
Name of a LINKAGE parameter file or an LD model file output by \code{\link{FitGMLD}}.
}
  \item{ped}{
Name of the post-MAKEPED pedfile (see the explanation in the 
documentation for \code{\link{read.pedfile}})
giving the pedigree relationships and 
marker data availability to simulate.
}
  \item{n}{
Number of pedigrees to simulate.
}
  \item{complete.data}{
Should complete data be simulated; i.e., should we ignore the marker
data availability in \code{ped}? Default is \code{FALSE}.
}
  \item{gd.ped}{
Name of the output file of simulated marker data in post-MAKEPED pedigree
file format.
}
  \item{compress.pedfiles}{
Should the output pedigree file be compressed? Default is \code{FALSE}.
}
}
\value{
No value. The output is in the file named by \code{gd.ped}.
}
\author{
Sigal Blay, Jinko Graham and Brad McNeney
}
\note{
This function can require large amounts of memory
for large data sets (e.g. whole chromosomes of SNPs at the marker
density of the HapMap project). The computations
are done in Java, using functions from Alun
Thomas' suite of Java Programs for Statistical Genetics and
Computational Statistics (JPSGCS). The JPSGCS Java programs are accessed
by R-wrappers provided by the \pkg{rJPSGCS} package, which 
initializes the Java virtual machine (JVM) if not already 
done so by another package. 
To over-ride the default amount of 
memory Java allocates for heap space,
initialize the JVM \emph{before} loading \pkg{rJPSGCS} as
follows:

\preformatted{
   options(java.parameters="-Xmx2048m") #set max heap space to 2GB
   library(rJava)
   .jinit() #initialize the JVM
   library(rJPSGCS) # now load rJPSGCS
   }
}
\seealso{
\code{\link{FitGMLD}}
}
\examples{
# 1. Gene-drop assuming independent loci in the founders, using allele 
# frequencies and map information from the example data set. 
data(exdat)
# Print out the data to examine missing data pattern.
exdat$markers
#Coerce to snp.matrix
sdat<-as(exdat$markers,"snp.matrix") 
# Need LINKAGE parameter and pedigree files as input.
# For independent loci in the founders use a standard LINKAGE parameter file.
write.parfile(snp.data=sdat, map=exdat$map, file="indep.par")
# To construct the pedigree file for unrelated individuals, use
# the pedinf="unrelated" option.
write.pedfile(pedinf="unrelated",snp.data=sdat,file="unrel.ped")
GeneDrops(ld.par="indep.par",ped="unrel.ped",n=10,gd.ped="gd.ped")
# Read the SNP data back into R. 
simdat<-read.pedfile("gd.ped")$snp.data
# Look at the simulated data: need to coerce to numeric matrix to see
# values of individual genotypes, rather than the usual snp.matrix summary
as(simdat,"numeric")

# 2. Simulate data at 5 independent SNPs 0.1 cM apart on a parent-child trio.
map=(1:5)/10
# Marker data on each individual as follows:
mom=c(1,2,1,1,1) # complete data
dad=c(0,NA,0,1,0) # missing data at SNP 2
child=c(NA,1,1,2,NA) #missing data at SNPs 1 and 5
sdat<-rbind(mom,dad,child)
colnames(sdat)<-paste("SNP",1:5,sep="")
sdat<-as(sdat,"snp.matrix")
write.parfile(snp.data=sdat, map=map, file="trio.par")
# Block of code setting up pedigree information for mom, dad and child.
# See the Details section of the write.pedfile help file for 
# information on the file format.
trioinf<-matrix(c(
    1, 1, 0, 0, 0, 0, 0, 2, 1, #mom: ID=1, no parents in pedigree
    1, 2, 0, 0, 0, 0, 0, 1, 1, #dad: ID=2, no parents in pedigree
    1, 3, 2, 1, 0, 0, 0, 2, 1),#child: ID=3, dadID=2, momID=1
    byrow=TRUE, ncol=9, nrow=3)
write.pedfile(pedinf=trioinf, snp.data=sdat,file="trio.ped")
GeneDrops(ld.par="trio.par",ped="trio.ped",n=10,gd.ped="gd.ped")
#Read the SNP data back into R. 
simdat<-read.pedfile("gd.ped")$snp.data
# Look at the missing data patterns in simdat
as(simdat,"numeric")

\dontrun{
# 3. Repeat gene drop for the trio, but this time use an LD model fit 
# by FitGMLD. FitGMLD requires a LINKAGE parameter file and a transposed
# pedigree file as input. Can use the parameter file trio.par again.
# The transposed pedigree file can be generated as follows:
write.pedfile(pedinf=trioinf,snp.data=sdat,file="f.ped",transpose=TRUE)
FitGMLD("trio.par","f.ped","out.ld.par")
#Call to GeneDrops uses the output of FitGMLD as the parameter file and
# the regular (not transposed) pedigree file 
GeneDrops(ld.par="out.ld.par",ped="trio.ped",n=10,gd.ped="gd.ped")
simdat<-read.pedfile("gd.ped")$snp.data
unlink(c("f.ped","out.ld.par"))
}
unlink(c("unrel.ped","trio.ped","indep.par","trio.par", "gd.ped"))
}

\keyword{ datagen }
