<?php

error_reporting(E_ALL);

  $action = $_GET['action'];
  $file = $_GET['file'];

  switch($action){

    case delete:
      chdir(content);
      unlink($file);

    case content:
      chdir(content);
      $content = file_get_contents($file);
      echo $content;

}

?>
