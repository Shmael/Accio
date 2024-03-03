<?php

	$dbhost = 'niam.rezel.net';
	$dbuser = 'accio';
	$dbpass = 'pact53';
	$conn = mysql_connect($dbhost, $dbuser, $dbpass);
	if(! $conn )
	{
  	die('Could not connect: ' . mysql_error());
	}

  $nom = $_GET['nom'];
	$boite = $_GET['boite'];

	$sql = 	"DELETE FROM aliment ".
       		"WHERE Nom = '$nom' AND BoiteID = '$boite' LIMIT 1";
	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not enter data: ' . mysql_error());
	}
	echo "Aliment supprimé\n";
	mysql_close($conn);

?>
