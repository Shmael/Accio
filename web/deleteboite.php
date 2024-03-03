<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
  $conn = mysql_connect("niam.rezel.net","accio","pact53");

    $boiteID = $_GET['boiteID'];

	

	$sql = 	"DELETE FROM boite WHERE BoiteID='$boiteID'";

	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not delete data: ' . mysql_error());
	}
	// echo "Marque rajoutée";

  	mysql_close($conn);
	

?>
