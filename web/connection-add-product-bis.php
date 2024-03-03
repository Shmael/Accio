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

	$sql = 	"INSERT INTO aliment ".
       		"(AlimentID, Nom, CodeBarre, BoiteID, Date, MarqueID, CorrespondID) ".
       		"VALUES ".
       		"(NULL,'$nom',NULL,'$boite',CURDATE(),NULL,NULL)";
	mysql_select_db('accio');
	$retval = mysql_query( $sql, $conn );
	if(! $retval )
	{
  	die('Could not enter data: ' . mysql_error());
	}
	echo "Aliment rajouté\n";
	mysql_close($conn);

?>
