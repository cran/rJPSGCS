\name{read.pedfile}
\alias{read.pedfile}
\title{
read a LINKAGE pedigree file into a \code{snp.matrix} object
}
\description{
This function 
reads post-MAKEPED pedigree files (used by JPSGCS)
into a \code{snp.matrix} object. 
The post-MAKEPED format includes nine columns of pedigree structure and
subject characteristics (see \bold{Details} below)
before the marker data. By contrast, pre-MAKEPED
pedigree file format has only six column of pedigree and subject data
before the marker data. This function was adapted from the
\code{\link{read.snps.pedfile}} function 
in the \pkg{chopsticks} package (formerly
\pkg{snpMatrix}), which reads pre-MAKEPED pedigree files.
}
\usage{
read.pedfile(file, snp.names=NULL, assign=NULL, missing=NULL, 
X=FALSE, sep=".")
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{file}{
The name of the pedigree file to be read.
}
  \item{snp.names}{
A character vector of SNP names. If \code{NULL} the function will look 
for SNP names in a corresponding \code{.map} or \code{.info} file with 
the same file name root as \code{file} 
(e.g., if \code{file="myfile.ped"} it will look for \code{myfile.map}
or \code{myfile.info}).
}
  \item{assign}{
A list of named mappings from letters to alleles; not currently used.
}
  \item{missing}{
Missing data code in the pedigree file; not currently used.
}
  \item{X}{
Is the pedigree file comprised of X-chromosome SNPs? Default is \code{FALSE}.
}
  \item{sep}{
Character string to separate Family and Member IDs in the row.names of 
the output \code{snp.matrix} object; not used.  
}
}
\details{
JPSGCS pedigree files are in post-MAKEPED format (though much of this information is ignored):
\tabular{ll}{
Column 1: \tab Pedigree number \cr
Column 2: \tab Individual ID number \cr
Column 3: \tab ID of father; 0=no father in pedigree \cr
Column 4: \tab ID of mother; 0=no mother in pedigree \cr
Column 5: \tab First offspring ID; ignored by JPSGCS \cr
Column 6: \tab Next paternal sibling ID; ignored by JPSGCS \cr
Column 7: \tab Next maternal sibling ID; ignored by JPSGCS \cr
Column 8: \tab Sex; 1=male, 2=female \cr 
Column 9: \tab Proband status (1=proband, 0=not); ignored by JPSGCS
}
The pre-MAKEPED format excludes columns 5 - 7. 
\code{read.pedfile} is essentially 
\code{\link{read.snps.pedfile}} for post-MAKEPED 
pedigree files, except that the option \code{low.mem} of
\code{read.snps.pedfile}
has not been implemented.

As in \code{read.snps.pedfile}, when reading in SNP genotype data 
the function looks for a corresponding
\code{.map} or \code{.info} file with information on the SNPs such
as physical map positions. For example, if \code{file="test.ped"},
the function looks for \code{test.map} or \code{test.info} to read
SNP information. Any SNP information that is found is saved in 
the element \code{snp.support} of the output object (see \bold{Value}).
}
\value{
A list with three components:
\item{\code{snp.data}}{a \code{snp.matrix} object holding the genotypes,}
\item{\code{subject.support}}{ a data frame containing the first nine
columns of the pedigree file, and }
\item{\code{snp.support}}{a data frame of SNP information. NB: this is
only meaningful if a \code{.map} or \code{.info} file was found.}
}
\author{
Sigal Blay, Jinko Graham and Brad McNeney
}

\seealso{
\code{\link{write.pedfile}},
\code{\link{read.snps.pedfile}}
}
\examples{
data(exdat)
sdat<-as(exdat$markers,"snp.matrix") #coerce to snp.matrix
#Write pedigree file for unrelated subjects
write.pedfile(pedinf="unrelated",snp.data=sdat,file="test.ped") 
#Read it back into R
sdat2<-read.pedfile("test.ped")
#clean up
unlink("test.ped")
}
\keyword{file}
