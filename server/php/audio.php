<?php

// Turn off error reporting
error_reporting(1);

// Report runtime errors
error_reporting(E_ERROR | E_WARNING | E_PARSE);

// Report all errors
error_reporting(E_ALL);

// Same as error_reporting(E_ALL);
ini_set("error_reporting", E_ALL);

$target_dir = "uploads/";
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);

    if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
        echo "The file ". basename($_FILES["fileToUpload"]["name"]). " has been uploaded. <br>";
    } else {
        echo "Sorry, there was an error uploading your file. <br>";
    }

echo "Dir:" . $target_dir . "<br>";
echo "File:" . $target_file . "<br>";

echo 'Here is some more debugging info:';
print_r($_FILES);

?> 
