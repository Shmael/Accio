<?php
	error_reporting(E_ALL ^ E_DEPRECATED);
    mysql_connect("niam.rezel.net","accio","pact53");
    mysql_select_db("accio");

		$boiteid = $_GET['boiteid'];

    $q=mysql_query("SELECT * FROM aliment LEFT JOIN marque ON aliment.MarqueID=marque.MarqueID WHERE BoiteID = '$boiteid'");

		//print($q);

		$json = array();

    if(mysql_num_rows($q)){
        while($row=mysql_fetch_row($q)){            
            // cast results to specific data types
            $test_data[]=$row; 
            
        } 
        $json['testData']=$test_data;     
    }

    mysql_close();

    print(json_encode($json));

    //while($e=mysql_fetch_assoc($q))
    //    $output[]=$e;
    //print(json_encode($output));
    //mysql_close();
?>
