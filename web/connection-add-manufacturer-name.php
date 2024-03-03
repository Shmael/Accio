<?php

error_reporting(E_ALL);
  $manufacturer = $_GET['manufacturer'];

  $file = fopen("manufacturer/$manufacturer", "wb") or die("Unable to open file!");
  fclose($file);

?>
