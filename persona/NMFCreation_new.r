# library(RJSONIO)
library(rjson)
library(rNMF);
library(Matrix)
library(MASS)
#library(jsonlite)

persona_function <- function(persona_number, matrixType){

  path <- paste("testNMF",".txt", sep = "");
  if(matrixType == 1) {
    path <- paste("FaceBook_Matrix",".txt", sep = "");
  }else if(matrixType == 2){
    path <- paste("Twitter_Matrix",".txt", sep = "");
  }else if(matrixType == 3){
    path <- paste("All_Social_Matrix",".txt", sep = "");
  }
  
  print(path);
  myNewMatrix <- read.table(file = path, header=F);
  
  matrixOut <- rnmf(as.matrix(myNewMatrix), k = persona_number, gamma = 0, showprogress = FALSE, my.seed = 1);
  w <- matrixOut$W;
  h <- matrixOut$H;
  
  
  h_matrix_file_path <- paste("h_matrix.json", sep = "");
  write.table(h, file = h_matrix_file_path, sep = " ", col.names = F, row.names = F);
  
  
  w_matrix_file_path <- paste("w_matrix.json", sep = "");
  write.table(w, file = w_matrix_file_path, sep = " ", col.names = F, row.names = F);
  

  return (TRUE);
}