<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
  $conn = mysql_connect("niam.rezel.net","accio","pact53");

  $nomBoite = $_GET['nomBoite'];
  $cateBoite = $_GET['cateBoite'];

	

	$sql = 	"INSERT INTO boite (BoiteID, Nom, Categorie) VALUES (NULL, '$nomBoite','$cateBoite')";

	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not enter data: ' . mysql_error());
	}
	// echo "Marque rajoutée";

  $q=mysql_query("SELECT * FROM boite WHERE BoiteID=LAST_INSERT_ID()");

  if (!$q) {
    die('Could not query:' . mysql_error());
  }
  
  $json = array();

    if(mysql_num_rows($q)){
        while($row=mysql_fetch_row($q)){
            // cast results to specific data types

            $test_data[]=$row;
        }
        $json['testData']=$test_data;
    }	
  

	mysql_close($conn);
	
	print(json_encode($json));

?>
