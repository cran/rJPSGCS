\name{write.parfile}
\alias{write.parfile}
\title{
write a LINKAGE parameter file
}
\description{
Use a snp.matrix object and genetic map to create a LINKAGE parameter file.
}
\usage{
write.parfile(snp.data, map, file="out.par")
}
\arguments{
  \item{snp.data}{
a \code{snp.matrix} object of genotypes
}
  \item{map}{
genetic map in centiMorgans
}
  \item{file}{
file name for the output parameter file
}
}
\value{
None. The output is the parameter file.
%%  ~Describe the value returned
%%  If it is a LIST, use
%%  \item{comp1 }{Description of 'comp1'}
%%  \item{comp2 }{Description of 'comp2'}
%% ...
}
\author{
Jinko Graham, Brad McNeney and Annick Nembot-Simo
}
\seealso{
\code{\link{write.pedfile}}
}
\examples{
data(exdat)
sdat<-as(exdat$markers,"snp.matrix") #coerce to snp.matrix
write.parfile(sdat,exdat$map,file="test.par")
unlink("test.par")
}
\keyword{ file }
