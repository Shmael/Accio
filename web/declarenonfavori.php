<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
  $conn = mysql_connect("niam.rezel.net","accio","pact53");

    $alimID = $_GET['alimID'];

	$sql = 	"UPDATE aliment SET Favoris='0' WHERE AlimentID='$alimID'";

	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not delete data: ' . mysql_error());
	}
	// echo "Marque rajoutée";
	$q=mysql_query("SELECT * FROM aliment WHERE AlimentID='$alimID'");

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
	

?>