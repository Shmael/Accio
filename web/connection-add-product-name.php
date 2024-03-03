<?php

error_reporting(E_ALL);
  $produit = $_GET['product'];

  $file = fopen("product/$produit", "wb") or die("Unable to open file!");
  fclose($file);

?>
