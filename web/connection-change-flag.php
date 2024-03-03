<?php

error_reporting(E_ALL);
  $flag = $_GET['flag'];

  switch($flag){

    case none:
      chdir(need);
      unlink("manufacturer");
      unlink("product");
      break;
    case product:
      chdir(need);
      $f = fopen("$flag","wb") or die("Unable to open file!");
      fclose($f);
      echo $flag;
      break;
    case manufacturer:
      chdir(need);
      //echo getcwd();
      $f = fopen("$flag","wb") or die("Unable to open file!");
      fclose($f);
      echo $flag;
      break;

  }

?>
