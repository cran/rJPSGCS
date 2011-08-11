\name{FitGMLD}
\alias{FitGMLD}
\title{
estimate a graphical model for linkage disequilibrium
}
\description{
This function is an R wrapper for the JPSGCS program to estimate
a graphical model for linkage disequilibrium (LD) from a 
sample of genotypes.
The genetic markers are assumed to be on the same chromosome.
}
\usage{
FitGMLD(par = "input.par", fped = "flipped.ped", out.ld.par = "out.ld.par")
}
\arguments{
  \item{par}{
Name of a LINKAGE parameter file (also called a DATAFILE: see 
\url{http://linkage.rockefeller.edu/soft/linkage/})
that contains marker and genetic map information.
}
  \item{fped}{
Name of a LINKAGE pedigree file ``flipped'' so that rows correspond to loci and columns correspond to individuals.
}
  \item{out.ld.par}{
Name of the file in the current working directory to contain the fitted LD model.
}
}
\details{
The input pedigree file \code{fped}
is not the standard format where rows correspond to
individuals and columns to loci. Rather, it has been transposed so that
rows correspond to loci. 
Transposed LINKAGE pedigree files may be written by the 
\code{\link{write.pedfile}} function
with the option \code{transpose=TRUE}.

Further information on the graphical model is available from the
program's description on the JPSGCS website:
\url{http://balance.med.utah.edu/wiki/index.php/FitGMLD}
}
\value{
None. The result of the function call is the output file \code{out.ld.par}.
}
\references{
\url{http://balance.med.utah.edu/wiki/index.php/FitGMLD}
}
\author{
Sigal Blay, Jinko Graham and Brad McNeney
}
\note{
This function can require large amounts of memory
for large data sets (e.g. whole chromosomes of SNPs at the marker
density of the HapMap project). The computation for fitting the 
LD model is done in java, using functions from Alun
Thomas' suite of Java Programs for Statistical Genetics and
Computational Statistics (JPSGCS). The JPSGCS java programs are accessed
by R-wrappers provided by the \pkg{rJPSGCS} package, which 
initializes the java virtual machine (JVM) if not already 
done so by another package. 
To over-ride the default amount of 
memory java allocates for heap space,
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
\code{\link{write.parfile}},
\code{\link{write.pedfile}}
}
\examples{
data(exdat)
sdat<-as(exdat$markers,"snp.matrix") #coerce to snp.matrix
# Write a LINKAGE parameter file 
write.parfile(snp.data=sdat,map=exdat$map,file="test.par")
# Write a "flipped" pedfile for the SNP data on unrelated subjects.
write.pedfile(pedinf="unrelated",snp.data=sdat,file="ftest.ped",transpose=TRUE)
FitGMLD(par="test.par",fped="ftest.ped",out.ld.par="test.ld.par")
# Clean up
unlink(c("test.par","ftest.ped","test.ld.par"))
}
\keyword{ file }
\keyword{ models }
