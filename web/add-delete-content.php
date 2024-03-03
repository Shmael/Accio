<?php

error_reporting(E_ALL);

  $action = $_GET['action'];
  $file = $_GET['file'];
  $name = $_GET['name'];

  switch($action){

    case add:
      $fic = fopen("content/$file", "wb") or die("Unable to open file!");
      $fwrite = fwrite($fic, $name) or die("Unable to write in file!");
      fclose($fic);

    case delete:
      chdir(content);
      unlink($file);

    case content:
      chdir(content);
      $content = file_get_contents($file);
      echo $content;

  }

?>
