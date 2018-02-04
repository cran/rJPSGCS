#include <R.h>
#include <Rinternals.h>
#include <stdlib.h> // for NULL
#include <R_ext/Rdynload.h>

/* FIXME: 
   Check these declarations against the C/Fortran source code.
*/

/* .C calls */
extern void write_as_pedfile(void *, void *, void *, void *, void *, void *, void *, void *, void *, void *);
extern void write_as_transposed_pedfile(void *, void *, void *, void *, void *, void *, void *, void *, void *, void *);

/* .Call calls */
extern SEXP read_pedfile(SEXP, SEXP, SEXP, SEXP, SEXP);

static const R_CMethodDef CEntries[] = {
    {"write_as_pedfile",            (DL_FUNC) &write_as_pedfile,            10},
    {"write_as_transposed_pedfile", (DL_FUNC) &write_as_transposed_pedfile, 10},
    {NULL, NULL, 0}
};

static const R_CallMethodDef CallEntries[] = {
    {"read_pedfile", (DL_FUNC) &read_pedfile, 5},
    {NULL, NULL, 0}
};

void R_init_rJPSGCS(DllInfo *dll)
{
    R_registerRoutines(dll, CEntries, CallEntries, NULL, NULL);
    R_useDynamicSymbols(dll, FALSE);
}
