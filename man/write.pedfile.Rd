\name{write.pedfile}
\alias{write.pedfile}
\title{
write a \code{snp.matrix} object to a LINKAGE pedigree file.
}
\description{
Write a snp.matrix object to a LINKAGE pedigree file, with 
pedigree information followed by genotypes.
This function can be used to write either 
post-MAKEPED or pre-MAKEPED pedigree file formats
(see \bold{Details}). In addition to standard pedigree file formats
the function can optionally write transposed pedigree files, as
required by \code{\link{FitGMLD}}.
}
\usage{
write.pedfile(pedinf, snp.data, file, transpose=FALSE, sep=" ", eol="\n", na="0")
}
%- maybe also 'usage' for other objects documented here.
\arguments{
  \item{pedinf}{
The pedigree information for the pedfile as a matrix or data frame.
Altnerately, users may specify \code{pedinf="unrelated"} for unrelated
subjects. See the \bold{Examples}.
}
  \item{snp.data}{
A \code{snp.matrix} object containing the marker data.
}
  \item{file}{
File name for the output pedfile.
}
  \item{transpose}{
Should the pedigree file be transposed, as required by
\code{\link{FitGMLD}}? Default is \code{FALSE}.
}
  \item{sep}{
Field separator (not currently used).
}
  \item{eol}{
The end-of-line character.
}
  \item{na}{
The missing data code in \code{snp.data}.
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
%%  ~~ If necessary, more details than the description above ~~
}
\value{
A numeric vector comprised of the number of subjects and SNPs written
to the pedigree file.
}
\author{
Sigal Blay, Jinko Graham and Brad McNeney
}
\section{Warning}{
The function makes \bold{NO} attempt to check the pedigree 
information for correctness. It is up to the user to specify
valid pedigree information.
}
\seealso{
\code{\link{read.pedfile}},
\code{\link{read.snps.pedfile}}
}
\examples{
data(exdat)
sdat<-as(exdat$markers,"snp.matrix") #coerce to snp.matrix
#Write a post-MAKEPED pedigree file for unrelated subjects. 
pedinf<-matrix(
 c( 1, 1, 0, 0, 0, 0, 0, 1, 1,
    2, 1, 0, 0, 0, 0, 0, 1, 1,
    3, 1, 0, 0, 0, 0, 0, 1, 1,
    4, 1, 0, 0, 0, 0, 0, 1, 1,
    5, 1, 0, 0, 0, 0, 0, 1, 1),
 byrow=TRUE, ncol=9, nrow=5)
write.pedfile(pedinf,sdat,file="test.ped")
#clean up
unlink("test.ped")
}
\keyword{file}
