<?php

  if(file_exists('/usr/share/nginx/www/pact/need/product')) {
    echo "product";
  }

  else if(file_exists('/usr/share/nginx/www/pact/need/manufacturer')) {
    echo "manufacturer";
  }

?>
