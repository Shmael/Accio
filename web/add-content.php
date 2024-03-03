<?php

error_reporting(E_ALL);

  $file = $_GET['file'];
  $name = $_GET['name'];

  $fic = fopen("content/$file", "wb") or die("Unable to open file!");
  $fwrite = fwrite($fic, $name) or die("Unable to write in file!");
  fclose($fic);

?>
