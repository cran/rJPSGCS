\name{exdat}
\alias{exdat}
\docType{data}
\title{
example data for rJPSGCS
}
\description{
Example data, comprised of simulated genotypes for 10 SNPs 
on the same chromosome, for five
unrelated individuals, along with a genetic map for the SNPs.
The fifth subject has missing genotypes at SNPs 3 and 4.
}
\usage{data(exdat)}
\format{
A list with the following elements:
\tabular{rl}{
\code{$markers} \tab a numeric matrix of SNP genotypes coded as 0, 1 or 2 copies of an index allele \cr
\code{$map} \tab a numeric vector containing the genetic map positions for the SNPs in centiMorgans \cr
}
}
\examples{
data(exdat)
}
\keyword{datasets}
